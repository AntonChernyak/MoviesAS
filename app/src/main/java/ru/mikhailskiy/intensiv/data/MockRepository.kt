package ru.mikhailskiy.intensiv.data

object MockRepository {

    fun getMovies(): List<Movie> {

        val  actorsList = ArrayList<Actor>()
        for (actor in 0..4) {
            actorsList.add(Actor("Tobey" ,"Maguire 1", "https://2fan.ru/upload/000/u1/3/6/36b62c70.png"))
            actorsList.add(Actor("Tobey" ,"Maguire 2", "https://s.tcdn.co/8a1/9aa/8a19aab4-98c0-37cb-a3d4-491cb94d7e12/48.png"))
            actorsList.add(Actor("Tobey" ,"Maguire 3", "https://memegenerator.net/img/images/16704817.jpg"))
        }

        val moviesList = mutableListOf<Movie>()
        for (x in 0..10) {
            val movie = Movie(
                title = "Spider-Man $x",
                voteAverage = 10.0 - x,
                description = "Питер Паркер – обыкновенный школьник. Однажды он отправился с классом на экскурсию," +
                        " где его кусает странный паук-мутант. Через время парень почувствовал в себе нечеловеческую" +
                        " силу и ловкость в движении, а главное – умение лазать по стенам и метать стальную паутину." +
                        " Свои способности он направляет на защиту слабых. Так Питер становится настоящим супергероем" +
                        " по имени Человек-паук, который помогает людям и борется с преступностью. Но там, где есть" +
                        " супергерой, рано или поздно всегда объявляется и суперзлодей.",
                studio = "Columbia/Sony",
                genre = listOf("Action", "Adventure", "Sci-Fi"),
                year = "2002",
                actors = actorsList,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BYTk3MDljOWQtNGI2My00OTEzLTlhYjQtOTQ4ODM2MzUwY2IwXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_.jpg"
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
                voteAverage = 10.0 - x,
                posterUrl = "https://i.ytimg.com/vi/Qgcj0QJDYEw/maxresdefault.jpg"
            )
            tvShowsList.add(movie)
        }

        return tvShowsList
    }

}