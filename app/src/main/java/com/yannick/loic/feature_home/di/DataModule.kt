package com.yannick.loic.feature_home.di

import com.yannick.loic.home.data.repository.ComicsDataRepositoryImpl
import com.yannick.loic.home.domain.repositories.ComicsDataRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {
    @Binds
    abstract fun bindsRepository(
        repoImpl: ComicsDataRepositoryImpl
    ): ComicsDataRepository

}