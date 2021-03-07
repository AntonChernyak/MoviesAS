package ru.mikhailskiy.intensiv.providers

import io.reactivex.Observable
import ru.mikhailskiy.intensiv.extensions.threadSwitch

interface CacheProvider<T> {

    fun getObservable(type: RepositoryAccess): Observable<T> {
        return createObservable(type)
            .threadSwitch()
    }

    private fun createObservable(type: RepositoryAccess): Observable<T> {
        when (type) {

            RepositoryAccess.OFFLINE -> return createOfflineObservable()

            RepositoryAccess.REMOTE_FIRST -> return createRemoteObservable()

            RepositoryAccess.OFFLINE_FIRST -> {
                val remoteObservable = createRemoteObservable()

                return createOfflineObservable()
                    .onErrorResumeNext(remoteObservable)
                    .switchIfEmpty(remoteObservable)
            }

            else -> {
                val remoteObservable = createRemoteObservable()

                return createOfflineObservable()
                    .onErrorResumeNext(remoteObservable)
                    .concatWith(remoteObservable)
            }
        }
    }

    fun createRemoteObservable(): Observable<T>

    fun createOfflineObservable(): Observable<T>
}

enum class RepositoryAccess {
    OFFLINE,
    REMOTE_FIRST,
    OFFLINE_FIRST
}