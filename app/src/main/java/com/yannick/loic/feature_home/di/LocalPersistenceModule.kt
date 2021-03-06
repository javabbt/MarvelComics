package com.yannick.loic.feature_home.di

import android.app.Application
import com.yannick.loic.home.data.repository.LocalRepository
import com.yannick.loic.localdata.datasource.LocalDataSourceImpl
import com.yannick.loic.localdata.db.ComicsDB
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [LocalPersistenceModule.Binders::class])
class LocalPersistenceModule {

    @Module
    interface Binders {

        @Binds
        fun bindsLocalDataSource(
            localDataSourceImpl: LocalDataSourceImpl
        ): LocalRepository

    }

    @Provides
    @Singleton
    fun providesDatabase(
        application: Application
    ) = ComicsDB.getInstance(application.applicationContext)

    @Provides
    @Singleton
    fun providesComicsDAO(
        comicsDB: ComicsDB
    ) = comicsDB.getComicsListDao()


}
