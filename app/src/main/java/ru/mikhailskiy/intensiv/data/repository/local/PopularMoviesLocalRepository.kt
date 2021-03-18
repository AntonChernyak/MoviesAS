package ru.mikhailskiy.intensiv.data.repository.local

import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.database.MovieDao
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.MoviesRepository

class PopularMoviesLocalRepository(private val movieDao: MovieDao) : MoviesRepository {

    override fun getMovies(): Single<List<Movie>> {
        return movieDao
            .getMoviesByCategory(MoviesRepository.MovieType.POPULAR.name)
            .flatMap {
                if (it.isEmpty()) throw IllegalStateException()
                else Single.just(it)
            }
    }
}