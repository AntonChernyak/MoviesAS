package ru.mikhailskiy.intensiv.data

import ru.mikhailskiy.intensiv.data.movie_model.MovieVO

object MockRepository {

    fun getMovies(): List<MovieVO> {

        val actorsList = ArrayList<Actor>()
        for (actor in 0..4) {
            actorsList.add(
                Actor(
                    "Tobey",
                    "Maguire 1",
                    "https://2fan.ru/upload/000/u1/3/6/36b62c70.png"
                )
            )
            actorsList.add(
                Actor(
                    "Tobey",
                    "Maguire 2",
                    "https://s.tcdn.co/8a1/9aa/8a19aab4-98c0-37cb-a3d4-491cb94d7e12/48.png"
                )
            )
            actorsList.add(
                Actor(
                    "Tobey",
                    "Maguire 3",
                    "https://memegenerator.net/img/images/16704817.jpg"
                )
            )
        }

        val moviesList = mutableListOf<MovieVO>()
        for (x in 0..10) {
            val movie = MovieVO(
                title = "Spider-Man $x",
                rating = (10.0 - x).toFloat(),
                posterUrl = "https://m.media-amazon.com/images/M/MV5BYTk3MDljOWQtNGI2My00OTEzLTlhYjQtOTQ4ODM2MzUwY2IwXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_.jpg"
            )
            moviesList.add(movie)
        }

        return moviesList
    }

    fun getTvShows(): List<MovieVO> {

        val tvShowsList = mutableListOf<MovieVO>()
        for (x in 0..10) {
            val movie = MovieVO(
                title = "Stranger Things $x",
                rating = (10.0 - x).toFloat(),
                posterUrl = "https://i.ytimg.com/vi/Qgcj0QJDYEw/maxresdefault.jpg"
            )
            tvShowsList.add(movie)
        }

        return tvShowsList
    }

}