package com.yannick.loic.feature_home.utils

import com.yannick.loic.remotedata.api.ComicsService
import com.yannick.loic.remotedata.models.Comic
import com.yannick.loic.remotedata.models.DataWrapper
import io.reactivex.Single


class FakeComicsService : ComicsService {
    override fun getComicsListStartsWithTitle(
        md5Digest: String,
        timestamp: Long,
        offset: Int?,
        limit: Int?,
        titleStartsWith: String
    ): Single<DataWrapper<List<Comic>>> {
        return Single.just(dataWrapper)
    }
}