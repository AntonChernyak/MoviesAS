package ru.mikhailskiy.intensiv.domain.usecase

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.Function4
import ru.mikhailskiy.intensiv.data.extensions.threadSwitch
import ru.mikhailskiy.intensiv.data.providers.RepositoryAccess
import ru.mikhailskiy.intensiv.data.providers.SingleCacheProvider
import ru.mikhailskiy.intensiv.data.repository.local.DbRepository
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.MoviesRepository
import ru.mikhailskiy.intensiv.presentation.feed.FeedFragment

class FeedFragmentUseCase(
    private val remoteRepositories: HashMap<FeedFragment.MovieType, MoviesRepository>,
    private val localRepositories: HashMap<FeedFragment.MovieType, MoviesRepository>,
    private val dbRepository: DbRepository
) : SingleCacheProvider<Map<FeedFragment.MovieType, List<Movie>>> {


    fun getMovies(): Single<Map<FeedFragment.MovieType, List<Movie>>> {
        return getSingle(RepositoryAccess.REMOTE_FIRST).threadSwitch()
    }

    fun saveMovies(moviesList: List<Movie>): Completable {
        return dbRepository
            .saveMovies(moviesList)
            .threadSwitch()
    }

    override fun createRemoteSingle(): Single<Map<FeedFragment.MovieType, List<Movie>>> {
        return Single.zip(
            remoteRepositories[FeedFragment.MovieType.TOP_RATED]?.getMovies(),
            remoteRepositories[FeedFragment.MovieType.POPULAR]?.getMovies(),
            remoteRepositories[FeedFragment.MovieType.NOW_PLAYING]?.getMovies(),
            remoteRepositories[FeedFragment.MovieType.UPCOMING]?.getMovies(),
            Function4<List<Movie>, List<Movie>, List<Movie>, List<Movie>, Map<FeedFragment.MovieType, List<Movie>>> { topRated, popular, nowPlaying, upcoming ->

                return@Function4 HashMap<FeedFragment.MovieType, List<Movie>>()
                    .plus(FeedFragment.MovieType.TOP_RATED to topRated)
                    .plus(FeedFragment.MovieType.POPULAR to popular)
                    .plus(FeedFragment.MovieType.NOW_PLAYING to nowPlaying)
                    .plus(FeedFragment.MovieType.UPCOMING to upcoming)
            })
    }

    override fun createOfflineSingle(): Single<Map<FeedFragment.MovieType, List<Movie>>> {
        return Single.zip(
            localRepositories[FeedFragment.MovieType.TOP_RATED]?.getMovies(),
            localRepositories[FeedFragment.MovieType.POPULAR]?.getMovies(),
            localRepositories[FeedFragment.MovieType.NOW_PLAYING]?.getMovies(),
            localRepositories[FeedFragment.MovieType.UPCOMING]?.getMovies(),
            Function4<List<Movie>, List<Movie>, List<Movie>, List<Movie>, Map<FeedFragment.MovieType, List<Movie>>> { topRated, popular, nowPlaying, upcoming ->

                return@Function4 HashMap<FeedFragment.MovieType, List<Movie>>()
                    .plus(FeedFragment.MovieType.TOP_RATED to topRated)
                    .plus(FeedFragment.MovieType.POPULAR to popular)
                    .plus(FeedFragment.MovieType.NOW_PLAYING to nowPlaying)
                    .plus(FeedFragment.MovieType.UPCOMING to upcoming)
            })
    }

}