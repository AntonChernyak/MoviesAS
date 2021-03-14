package ru.mikhailskiy.intensiv.data.mappers

import ru.mikhailskiy.intensiv.data.dto.movies_dto.MovieDto
import ru.mikhailskiy.intensiv.data.extensions.voteAverageToRating
import ru.mikhailskiy.intensiv.data.vo.Movie

class MovieDtoMapper : ViewObjectMapper<Movie, MovieDto> {
    override fun toViewObject(dto: MovieDto): Movie {
        return Movie(
            id = dto.id,
            title = dto.title,
            rating = dto.voteAverage.voteAverageToRating(),
            posterUrl = dto.posterPath,
            backdropUrl = dto.backdropPath,
            releaseYear = dto.releaseDate.substring(0..3),
            description = dto.overview,
            voteCount = dto.voteCount
        )
    }
}