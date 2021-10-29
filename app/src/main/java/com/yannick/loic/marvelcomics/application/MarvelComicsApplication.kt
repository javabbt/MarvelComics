package com.yannick.loic.marvelcomics.application

import com.yannick.loic.feature_home.di.DaggerMarvelComicsAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

//ceci est la toute premiere classe qui s'execute au lancement de l'application
class MarvelComicsApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerMarvelComicsAppComponent.builder().application(this).build()
    }
}