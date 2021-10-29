package com.yannick.loic.home.data.repository

import androidx.paging.PagedList
import com.yannick.loic.home.domain.entities.ComicsEntity
import com.yannick.loic.home.domain.entities.RepositoryState
import com.yannick.loic.home.domain.entities.RepositoryStateRelay
import com.yannick.loic.home.domain.qualifiers.Background
import com.yannick.loic.home.domain.qualifiers.Foreground
import com.yannick.loic.home.domain.repositories.ComicsDataRepository
import com.yannick.loic.home.domain.usecases.GetComicsListAction
import com.yannick.loic.home.domain.usecases.GetComicsListAction.GetComicsListActionResult
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

//implementation des differents repertoires de comics
@Singleton
class ComicsDataRepositoryImpl @Inject constructor(
    val disposable: CompositeDisposable,
    val localRepository: LocalRepository,
    val remoteRepository: RemoteRepository,
    val repositoryStateRelay: RepositoryStateRelay,
    @Foreground val foregroundScheduler: Scheduler,
    @Background val backgroundScheduler: Scheduler
) : ComicsDataRepository {


    override fun getComicsList(query: GetComicsListAction.Params): GetComicsListActionResult {
        return GetComicsListActionResult(
            localRepository.getComicsListDatasourceFactory(query),
            ComicsListBoundaryCallback(query)
        )
    }

    override fun clean() {
        disposable.dispose()
    }

    inner class ComicsListBoundaryCallback(
        private val query: GetComicsListAction.Params
    ) : PagedList.BoundaryCallback<ComicsEntity>() {

        private var lastRequestedPage = 0


        private var isRequestInProgress = false

        override fun onZeroItemsLoaded() = requestAndSaveData(query)

        override fun onItemAtEndLoaded(itemAtEnd: ComicsEntity) =
            requestAndSaveData(query)

        private fun requestAndSaveData(query: GetComicsListAction.Params) {

            if (isRequestInProgress) {
                return
            }
            repositoryStateRelay.relay.accept(RepositoryState.LOADING)
            println("Request de la page$lastRequestedPage")
            isRequestInProgress = true
            disposable.add(
                remoteRepository.getComicsList(
                    query.searchKey,
                    lastRequestedPage,
                    query.limit
                ).subscribeOn(backgroundScheduler)
                    .doOnSubscribe { disposable.add(it) }
                    .observeOn(backgroundScheduler)
                    .retry(1)
                    .subscribe(
                        { comicsList ->
                            println("Loaded page$lastRequestedPage offset:${lastRequestedPage * query.limit}")
                            updateDB(comicsList)
                        },
                        { error ->
                            reportRepoState(RepositoryState.error(error.localizedMessage))
                            isRequestInProgress = false
                        })
            )

        }

        private fun updateDB(comicsList: List<ComicsEntity>) {
            localRepository.insert(comicsList)
                .subscribeOn(backgroundScheduler)
                .observeOn(backgroundScheduler)
                .doOnSubscribe { disposable.add(it) }
                .andThen {
                    lastRequestedPage++
                    isRequestInProgress = false
                    reportRepoState(RepositoryState.LOADED)
                }
                .doOnError {
                    reportRepoState(RepositoryState.DB_ERROR)
                }.subscribe()
        }

        private fun reportRepoState(repositoryState: RepositoryState) {
            Observable.fromCallable {
                repositoryStateRelay.relay.accept(repositoryState)
            }.doOnSubscribe { disposable.add(it) }.subscribeOn(foregroundScheduler)
                .subscribe()
        }
    }
}