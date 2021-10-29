package com.yannick.loic.home.domain.entities

import com.jakewharton.rxrelay2.BehaviorRelay
import javax.inject.Inject
import javax.inject.Singleton

//differents etats des repertoires
data class RepositoryState(
    val status: Status,
    val msg: String? = null
) {

    enum class Status {
        RUNNING,
        SUCCESS_LOADED,
        SUCCESS_EMPTY,
        FAILED,
        DISCONNECTED,
        CONNECTED,
        DB_ERROR,
        DB_LOADED
    }

    companion object {

        val EMPTY = RepositoryState(Status.SUCCESS_EMPTY)
        val LOADED = RepositoryState(Status.SUCCESS_LOADED)
        val LOADING = RepositoryState(Status.RUNNING)
        val ERROR = RepositoryState(Status.FAILED)
        val DB_ERROR = RepositoryState(Status.DB_ERROR)
        val DB_LOADED = RepositoryState(Status.DB_LOADED)
        val CONNECTED = RepositoryState(Status.CONNECTED)
        val DISCONNECTED = RepositoryState(Status.DISCONNECTED)
        fun error(msg: String?) = RepositoryState(Status.FAILED, msg)
    }
}

@Singleton
class RepositoryStateRelay @Inject constructor() {
    val relay: BehaviorRelay<RepositoryState> = BehaviorRelay.create()
}