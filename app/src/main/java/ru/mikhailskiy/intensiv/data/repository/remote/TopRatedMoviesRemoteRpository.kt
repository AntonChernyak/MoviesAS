package ru.mikhailskiy.intensiv.data.repository.remote

import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.extension.toMoviesList
import ru.mikhailskiy.intensiv.data.network.MovieApiClient
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.MoviesRepository

class TopRatedMoviesRemoteRpository : MoviesRepository {

    override fun getMovies(): Single<List<Movie>> {
        return MovieApiClient
            .apiClient
            .getTopRatedMovies()
            .map {
                it.toMoviesList(MoviesRepository.MovieType.TOP_RATED.name)
            }
    }
}