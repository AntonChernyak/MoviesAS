package ru.mikhailskiy.intensiv.data.repository.local

import android.content.Context
import io.reactivex.Completable
import ru.mikhailskiy.intensiv.data.database.MovieDatabase
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.MoviesStoreRepository

class MoviesStoreRepository(
    private val context: Context
) : MoviesStoreRepository {

    private val movieDao by lazy {
        MovieDatabase.get(context).getMovieDao()
    }

    override fun saveMovies(moviesList: List<Movie>): Completable {
        return movieDao.saveMoviesList(moviesList)
    }

    override fun deleteAllMovies(): Completable {
        return movieDao.deleteAllMovies()
    }
}