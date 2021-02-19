package ru.mikhailskiy.intensiv.data

import ru.mikhailskiy.intensiv.data.movie_model.MovieDTO
import ru.mikhailskiy.intensiv.data.movie_model.MovieVO

object DtoToVoConverter {

    fun MovieListDtoConverter(movieDto: MovieDTO): MovieVO {
        return MovieVO(
            title = movieDto.title,
            //  description = movieDto.overview,
            voteAverage = movieDto.voteAverage,
            //  actors = ArrayList(),
            posterUrl = movieDto.posterPath
            //  genre = movieDto.genreIds,
            //  year = movieDto.releaseDate,
            // studio = ""
        )
    }
}