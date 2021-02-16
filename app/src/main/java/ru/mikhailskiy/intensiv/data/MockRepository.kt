package ru.mikhailskiy.intensiv.data

object MockRepository {

    fun getMovies(): List<Movie> {

        val moviesList = mutableListOf<Movie>()
        for (x in 0..10) {
            val movie = Movie(
                title = "Spider-Man $x",
                voteAverage = 10.0 - x
            )
            moviesList.add(movie)
        }

        return moviesList
    }

    fun getTvShows(): List<Movie> {

        val tvShowsList = mutableListOf<Movie>()
        for (x in 0..10) {
            val movie = Movie(
                title = "Stranger Things $x",
                voteAverage = 10.0 - x
            )
            tvShowsList.add(movie)
        }

        return tvShowsList
    }

}