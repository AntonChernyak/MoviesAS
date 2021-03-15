package ru.mikhailskiy.intensiv.domain.usecase

import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.extensions.threadSwitch
import ru.mikhailskiy.intensiv.data.providers.RepositoryAccess
import ru.mikhailskiy.intensiv.data.providers.SingleCacheProvider
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.MoviesRepository
import ru.mikhailskiy.intensiv.presentation.feed.FeedFragment

class FeedFragmentUseCase(
    private val repositories: HashMap<FeedFragment.MovieType, MoviesRepository>
) : SingleCacheProvider<Map<FeedFragment.MovieType, List<Movie>>> {


    fun getMovies(): Single<Map<FeedFragment.MovieType, List<Movie>>> {
        return getSingle(RepositoryAccess.OFFLINE_FIRST)
            .threadSwitch()
    }

    override fun createRemoteSingle(): Single<Map<FeedFragment.MovieType, List<Movie>>> {
        return Single.zip(
            repositories[FeedFragment.MovieType.TOP_RATED]?.getMovies(),
            repositories[FeedFragment.MovieType.POPULAR]?.getMovies(),
            repositories[FeedFragment.MovieType.NOW_PLAYING]?.getMovies(),
            repositories[FeedFragment.MovieType.UPCOMING]?.getMovies(),
            object : Function4<List<Movie>, List<Movie>, List<Movie>, List<Movie>, Map<FeedFragment.MovieType, List<Movie>>> {
                override fun invoke(p1: List<Movie>, p2: List<Movie>, p3: List<Movie>, p4: List<Movie>): Map<FeedFragment.MovieType, List<Movie>> {
                    //  clearDatabase()
                    return HashMap<FeedFragment.MovieType, List<Movie>>()
                        .plus(FeedFragment.MovieType.TOP_RATED to p1)
                        .plus(FeedFragment.MovieType.POPULAR to p2)
                        .plus(FeedFragment.MovieType.NOW_PLAYING to p3)
                        .plus(FeedFragment.MovieType.UPCOMING to p4)
                }
            })
    }

    override fun createOfflineSingle(): Single<Map<FeedFragment.MovieType, List<Movie>>> {
        return Single.zip(
            repositories[FeedFragment.MovieType.TOP_RATED]?.getMovies(),
            repositories[FeedFragment.MovieType.POPULAR]?.getMovies(),
            repositories[FeedFragment.MovieType.NOW_PLAYING]?.getMovies(),
            repositories[FeedFragment.MovieType.UPCOMING]?.getMovies(),
            object : Function4<List<Movie>, List<Movie>, List<Movie>, List<Movie>, Map<FeedFragment.MovieType, List<Movie>>> {
                override fun invoke(p1: List<Movie>, p2: List<Movie>, p3: List<Movie>, p4: List<Movie>): Map<FeedFragment.MovieType, List<Movie>> {
                    //  clearDatabase()
                    return HashMap<FeedFragment.MovieType, List<Movie>>()
                        .plus(FeedFragment.MovieType.TOP_RATED to p1)
                        .plus(FeedFragment.MovieType.POPULAR to p2)
                        .plus(FeedFragment.MovieType.NOW_PLAYING to p3)
                        .plus(FeedFragment.MovieType.UPCOMING to p4)
                }
            })
    }

}