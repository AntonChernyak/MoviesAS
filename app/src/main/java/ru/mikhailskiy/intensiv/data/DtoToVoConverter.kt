package ru.mikhailskiy.intensiv.data

import ru.mikhailskiy.intensiv.data.credits_model.ActorDto
import ru.mikhailskiy.intensiv.data.credits_model.ActorVo
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetailsDTO
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetailsVO
import ru.mikhailskiy.intensiv.data.movie_model.MovieDTO
import ru.mikhailskiy.intensiv.data.movie_model.MovieVO

object DtoToVoConverter {

    fun movieDtoConverter(movieDto: MovieDTO): MovieVO {
        return MovieVO(
            id = movieDto.id,
            title = movieDto.title,
            rating = movieDto.voteAverage.div(2).toFloat(),
            posterUrl = movieDto.posterPath,
            backdpopUrl = movieDto.backdropPath,
            voteCount = movieDto.voteCount
        )
    }

    fun movieDetailsConverter(movieDetailsDto: MovieDetailsDTO): MovieDetailsVO {
        return MovieDetailsVO(
            id = movieDetailsDto.id,
            title = movieDetailsDto.title,
            overview = movieDetailsDto.overview,
            posterPath = movieDetailsDto.posterPath,
            voteCount = movieDetailsDto.voteCount,
            rating = movieDetailsDto.voteAverage.div(2).toFloat(),
            year = movieDetailsDto.releaseDate.substring(0, 4),
            genres = movieDetailsDto.genres.joinToString { it.name },
            productionCompanies = movieDetailsDto.productionCompanies.joinToString { it.name }
        )
    }

    fun actorConverter(actorsDto: ActorDto): ActorVo {
        return ActorVo(
            id = actorsDto.id,
            firstName = actorsDto.name.substringBefore(" "),
            lastName = actorsDto.name.substringAfter(" "),
            posterPath = actorsDto.profilePath
        )
    }
}