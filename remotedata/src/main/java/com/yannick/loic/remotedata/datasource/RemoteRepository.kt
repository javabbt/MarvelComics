package com.yannick.loic.remotedata.datasource

import com.yannick.loic.home.data.repository.RemoteRepository
import com.yannick.loic.home.domain.entities.ComicsEntity
import com.yannick.loic.home.domain.qualifiers.Background
import com.yannick.loic.remotedata.api.ComicsService
import com.yannick.loic.remotedata.mapper.ComicsRemoteMapper
import com.yannick.loic.remotedata.qualifiers.PrivateKey
import com.yannick.loic.remotedata.qualifiers.PublicKey
import com.yannick.loic.utils.HashGenerator
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val comicsService: ComicsService,
    private val comicsRemoteMapper: ComicsRemoteMapper,
    private val hashGenerator: HashGenerator,
    @PublicKey private val publicKey: String,
    @PrivateKey private val privateKey: String,
    @Background private val backgroundThread: Scheduler

) : RemoteRepository {
    override fun getComicsList(
        query: String,
        offset: Int,
        limit: Int
    ): Single<List<ComicsEntity>> {
        val timestamp = System.currentTimeMillis()
        val hash = "$timestamp$privateKey$publicKey"
        return  comicsService.getComicsListStartsWithTitle(
            titleStartsWith = query,
            offset = offset*limit,
            limit = limit,
            timestamp = timestamp,
            md5Digest = hashGenerator.buildMD5Digest(hash)
        ).subscribeOn(backgroundThread)
            .map {
                it.data.results.map { comic ->
                    comicsRemoteMapper.from(
                        comic
                    )
                }
            }

    }
}