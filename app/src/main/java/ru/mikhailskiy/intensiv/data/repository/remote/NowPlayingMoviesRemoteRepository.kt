package ru.mikhailskiy.intensiv.data.repository.remote

import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.extension.toMoviesList
import ru.mikhailskiy.intensiv.data.network.MovieApiInterface
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.MoviesRepository

class NowPlayingMoviesRemoteRepository(private val movieApi: MovieApiInterface) : MoviesRepository {

    override fun getMovies(): Single<List<Movie>> {
        return movieApi
            .getNowPlayingMovie()
            .map {
                it.toMoviesList(MoviesRepository.MovieType.NOW_PLAYING.name)
            }
    }
}