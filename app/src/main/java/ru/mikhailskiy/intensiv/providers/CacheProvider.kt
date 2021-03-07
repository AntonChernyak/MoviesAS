package ru.mikhailskiy.intensiv.providers

import io.reactivex.Single

interface CacheProvider<T> {

    fun getObservable(type: RepositoryAccess): Single<T> {
        return createObservable(type)
    }

    private fun createObservable(type: RepositoryAccess): Single<T> {
        return when (type) {

            RepositoryAccess.OFFLINE -> createOfflineObservable()

            RepositoryAccess.REMOTE_FIRST -> createRemoteObservable()

            RepositoryAccess.OFFLINE_FIRST -> {
                val remoteObservable = createRemoteObservable()
                createOfflineObservable()
                    .onErrorResumeNext(remoteObservable)
            }
        }
    }

    fun createRemoteObservable(): Single<T>

    fun createOfflineObservable(): Single<T>
}

enum class RepositoryAccess {
    OFFLINE,
    REMOTE_FIRST,
    OFFLINE_FIRST
}