package ru.mikhailskiy.intensiv.data.providers

import io.reactivex.Single

interface SingleCacheProvider<T> {

    fun getSingle(type: RepositoryAccess): Single<T> {
        return createSingle(type)
    }

    private fun createSingle(type: RepositoryAccess): Single<T> {
        return when (type) {

            RepositoryAccess.OFFLINE -> createOfflineSingle()

            RepositoryAccess.REMOTE -> createRemoteSingle()

            RepositoryAccess.REMOTE_FIRST -> {
                return createRemoteSingle()
                    .onErrorResumeNext(createOfflineSingle())
            }

            RepositoryAccess.OFFLINE_FIRST -> {
                val remoteObservable = createRemoteSingle()

                return createOfflineSingle()
                    .onErrorResumeNext(remoteObservable)

            }
        }
    }

    fun createRemoteSingle(): Single<T>

    fun createOfflineSingle(): Single<T>
}