package ru.mikhailskiy.intensiv.data

import ru.mikhailskiy.intensiv.data.movie_model.Movie

object MockRepository {

    fun getMovies(): List<Movie> {
        val moviesList = mutableListOf<Movie>()
        for (x in 0..10) {
            val movie = Movie(
                title = "Spider-Man $x",
                rating = (10.0 - x).toFloat(),
                posterUrl = "https://m.media-amazon.com/images/M/MV5BYTk3MDljOWQtNGI2My00OTEzLTlhYjQtOTQ4ODM2MzUwY2IwXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_.jpg"
            )
            moviesList.add(movie)
        }
        return moviesList
    }

}