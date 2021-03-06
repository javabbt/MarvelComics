package com.yannick.loic.feature_home.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yannick.loic.home.presentation.factory.ComicsViewModelFactory
import com.yannick.loic.home.presentation.viewmodels.HomeVM
import com.yannick.loic.feature_home.di.qualifiers.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [PresentationModule.Binders::class])
class PresentationModule {
    @Module
    interface Binders {

        @Binds
        fun bindsViewModelFactory(
            factory: ComicsViewModelFactory
        ): ViewModelProvider.Factory

        @Binds
        @IntoMap
        @ViewModelKey(HomeVM::class)
        fun bindsHomeViewModel(homeVM: HomeVM): ViewModel
    }
}