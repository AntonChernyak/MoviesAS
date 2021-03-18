package ru.mikhailskiy.intensiv.data.repository.local

import io.reactivex.Completable
import ru.mikhailskiy.intensiv.data.database.MovieDao
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.MoviesStoreRepository

class MoviesStoreRepository(
    private val movieDao: MovieDao
) : MoviesStoreRepository {

    override fun saveMovies(moviesList: List<Movie>): Completable {
        return movieDao.saveMoviesList(moviesList)
    }

    override fun deleteAllMovies(): Completable {
        return movieDao.deleteAllMovies()
    }
}