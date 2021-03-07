package ru.mikhailskiy.intensiv.providers

import io.reactivex.Observable

interface ObservableCacheProvider<T> {

    fun getObservable(type: RepositoryAccess): Observable<T> {
        return createObservable(type)
    }

    private fun createObservable(type: RepositoryAccess): Observable<T> {
        return when (type) {

            RepositoryAccess.OFFLINE -> createOfflineObservable()

            RepositoryAccess.REMOTE_FIRST -> createRemoteObservable()

            RepositoryAccess.OFFLINE_FIRST -> {
                val remoteObservable = createRemoteObservable()

                return createOfflineObservable()
                    .onErrorResumeNext(remoteObservable)
                    .switchIfEmpty(remoteObservable)
            }
        }
    }

    fun createRemoteObservable(): Observable<T>

    fun createOfflineObservable(): Observable<T>
}