package com.yannick.loic.home.domain.repositories

import com.yannick.loic.home.domain.usecases.GetComicsListAction
import com.yannick.loic.home.domain.usecases.GetComicsListAction.GetComicsListActionResult

interface ComicsDataRepository {

    fun getComicsList(
        query: GetComicsListAction.Params
    ): GetComicsListActionResult

    fun clean()

}