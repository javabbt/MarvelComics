package com.yannick.loic.localdata.datasource

import androidx.paging.DataSource
import com.yannick.loic.home.data.repository.LocalRepository
import com.yannick.loic.home.domain.entities.ComicsEntity
import com.yannick.loic.home.domain.usecases.GetComicsListAction
import com.yannick.loic.localdata.db.ComicsListDAO
import com.yannick.loic.localdata.mapper.ComicsLocalMapper
import io.reactivex.Completable
import java.util.*
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val comicsLocalMapper: ComicsLocalMapper,
    private val comicsListDAO: ComicsListDAO
) : LocalRepository {
    override fun insert(
        comicsEntityList: List<ComicsEntity>
    ): Completable {
        return comicsListDAO.insert(comicsEntityList.map { comicsLocalMapper.to(it) })
    }

    override fun getComicsListDatasourceFactory(param: GetComicsListAction.Params): DataSource.Factory<Int, ComicsEntity> {
        return comicsListDAO.getComicsList(
            "%${param.searchKey.toUpperCase(Locale.getDefault()).replace(' ', '%')}%"
        ).map { comicsLocalMapper.from(it) }
    }
}
