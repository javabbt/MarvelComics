package com.yannick.loic.home.data.repository

import androidx.paging.DataSource
import com.yannick.loic.home.domain.entities.ComicsEntity
import com.yannick.loic.home.domain.usecases.GetComicsListAction
import io.reactivex.Completable

//l'interface du repertoire local
interface LocalRepository {
    fun insert(
        comicsEntityList: List<ComicsEntity>
    ): Completable

    fun getComicsListDatasourceFactory(param: GetComicsListAction.Params): DataSource.Factory<Int, ComicsEntity>

}