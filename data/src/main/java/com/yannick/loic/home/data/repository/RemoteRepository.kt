package com.yannick.loic.home.data.repository

import com.yannick.loic.home.domain.entities.ComicsEntity
import io.reactivex.Single

//l'interface du repertoire en ligne
interface RemoteRepository {
    fun getComicsList(
        query: String, offset: Int, limit: Int
    ): Single<List<ComicsEntity>>

}