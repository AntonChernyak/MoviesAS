package ru.mikhailskiy.intensiv.extensions

import ru.mikhailskiy.intensiv.data.movie_feed_model.Movie
import ru.mikhailskiy.intensiv.data.movie_feed_model.MovieDtoToVoConverter
import ru.mikhailskiy.intensiv.data.movie_feed_model.MovieResponse

fun Double.voteAverageToRating(): Float = this.div(2).toFloat()

fun MovieResponse.toMoviesList(type: String = ""): List<Movie>? = this.results?.let { dtoList ->
    dtoList.map {
        return@map MovieDtoToVoConverter().toViewObject(it).apply { this.type = type }
    }
}