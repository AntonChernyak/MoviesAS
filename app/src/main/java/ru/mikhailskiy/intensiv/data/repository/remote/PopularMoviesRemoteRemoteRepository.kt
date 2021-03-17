package ru.mikhailskiy.intensiv.data.repository.remote

import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.extensions.toMoviesList
import ru.mikhailskiy.intensiv.data.network.MovieApiClient
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.MoviesRepository
import ru.mikhailskiy.intensiv.presentation.feed.FeedFragment

class PopularMoviesRemoteRemoteRepository : MoviesRepository {

    override fun getMovies(): Single<List<Movie>> {
        return MovieApiClient
            .apiClient
            .getPopularMovies()
            .map {
                it.toMoviesList(FeedFragment.MovieType.POPULAR.name)
            }
    }
}