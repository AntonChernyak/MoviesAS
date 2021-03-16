package ru.mikhailskiy.intensiv.data.repository.remote

import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.mappers.MovieDtoMapper
import ru.mikhailskiy.intensiv.data.network.MovieApiClient
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.MoviesRepository

class NowPlayingMoviesRemoteRemoteRepository : MoviesRepository {

    override fun getMovies(): Single<List<Movie>> {
        return MovieApiClient.apiClient.getNowPlayingMovie().map { movieResponse ->
            MovieDtoMapper().toViewObject(movieResponse.results ?: throw IllegalStateException())
        }
    }
}