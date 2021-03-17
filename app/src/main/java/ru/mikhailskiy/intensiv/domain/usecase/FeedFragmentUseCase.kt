package ru.mikhailskiy.intensiv.domain.usecase

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function4
import ru.mikhailskiy.intensiv.data.extension.threadSwitch
import ru.mikhailskiy.intensiv.data.providers.RepositoryAccess
import ru.mikhailskiy.intensiv.data.providers.SingleCacheProvider
import ru.mikhailskiy.intensiv.data.repository.local.MoviesStoreRepository
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.MoviesRepository

class FeedFragmentUseCase(
    private val remoteRepositories: HashMap<MoviesRepository.MovieType, MoviesRepository>,
    private val localRepositories: HashMap<MoviesRepository.MovieType, MoviesRepository>,
    private val dbRepository: MoviesStoreRepository
) : SingleCacheProvider<Map<MoviesRepository.MovieType, List<Movie>>> {

    override fun createRemoteSingle(): Single<Map<MoviesRepository.MovieType, List<Movie>>> {
        return Single.zip(
            remoteRepositories[MoviesRepository.MovieType.TOP_RATED]?.getMovies(),
            remoteRepositories[MoviesRepository.MovieType.POPULAR]?.getMovies(),
            remoteRepositories[MoviesRepository.MovieType.NOW_PLAYING]?.getMovies(),
            remoteRepositories[MoviesRepository.MovieType.UPCOMING]?.getMovies(),
            Function4<List<Movie>, List<Movie>, List<Movie>, List<Movie>, Map<MoviesRepository.MovieType, List<Movie>>> { topRated, popular, nowPlaying, upcoming ->

                clearDatabase()

                return@Function4 HashMap<MoviesRepository.MovieType, List<Movie>>()
                    .plus(MoviesRepository.MovieType.TOP_RATED to topRated)
                    .plus(MoviesRepository.MovieType.POPULAR to popular)
                    .plus(MoviesRepository.MovieType.NOW_PLAYING to nowPlaying)
                    .plus(MoviesRepository.MovieType.UPCOMING to upcoming)
            })
    }

    override fun createOfflineSingle(): Single<Map<MoviesRepository.MovieType, List<Movie>>> {
        return Single.zip(
            localRepositories[MoviesRepository.MovieType.TOP_RATED]?.getMovies(),
            localRepositories[MoviesRepository.MovieType.POPULAR]?.getMovies(),
            localRepositories[MoviesRepository.MovieType.NOW_PLAYING]?.getMovies(),
            localRepositories[MoviesRepository.MovieType.UPCOMING]?.getMovies(),
            Function4<List<Movie>, List<Movie>, List<Movie>, List<Movie>, Map<MoviesRepository.MovieType, List<Movie>>> { topRated, popular, nowPlaying, upcoming ->

                clearDatabase()

                return@Function4 HashMap<MoviesRepository.MovieType, List<Movie>>()
                    .plus(MoviesRepository.MovieType.TOP_RATED to topRated)
                    .plus(MoviesRepository.MovieType.POPULAR to popular)
                    .plus(MoviesRepository.MovieType.NOW_PLAYING to nowPlaying)
                    .plus(MoviesRepository.MovieType.UPCOMING to upcoming)
            })
    }

    fun getMovies(): Single<Map<MoviesRepository.MovieType, List<Movie>>> {
        return getSingle(RepositoryAccess.OFFLINE_FIRST).threadSwitch()
    }

    fun saveMovies(moviesList: List<Movie>): Completable {
        return dbRepository
            .saveMovies(moviesList)
            .threadSwitch()
    }

    private fun clearDatabase(): Disposable {
        return dbRepository
            .deleteAllMovies()
            .threadSwitch()
            .subscribe()
    }
}