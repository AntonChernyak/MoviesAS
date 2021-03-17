package ru.mikhailskiy.intensiv.data.repository.local

import android.content.Context
import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.database.MovieDatabase
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.MoviesRepository

class UpcomingMoviesLocalRepository(val context: Context) : MoviesRepository {

    override fun getMovies(): Single<List<Movie>> {
        return MovieDatabase
            .get(context)
            .getMovieDao()
            .getMoviesByCategory(MoviesRepository.MovieType.UPCOMING.name)
            .flatMap {
                if (it.isEmpty()) throw IllegalStateException()
                else {
                    MovieDatabase
                        .get(context)
                        .getMovieDao()
                        .getMoviesByCategory(MoviesRepository.MovieType.UPCOMING.name)
                }
            }
    }
}