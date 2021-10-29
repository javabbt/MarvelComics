package com.yannick.loic.home.domain.usecases

import com.yannick.loic.home.domain.repositories.ComicsDataRepository
import javax.inject.Inject

class CleanAction @Inject constructor(
    private val comicsDataRepository: ComicsDataRepository
) {
    fun execute() {
        comicsDataRepository.clean()
    }
}