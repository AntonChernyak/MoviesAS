package ru.mikhailskiy.intensiv.data.repository.remote

import android.util.Log
import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.mappers.MovieDtoMapper
import ru.mikhailskiy.intensiv.data.network.MovieApiClient
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.MoviesRepository

class UpcomingMoviesRemoteRemoteRepository : MoviesRepository {

    override fun getMovies(): Single<List<Movie>> {

        Log.d("TAGGG_UP", "inside")
        val a = MovieApiClient.apiClient.getUpcomingMovies().map { movieResponse ->
            Log.d("TAGGG_UP", "response = $movieResponse")
            MovieDtoMapper()
                .toViewObject(movieResponse.results ?: throw IllegalStateException())
        }

        Log.d("TAGGG_UP", "a = ${a}")
        return a
    }
}