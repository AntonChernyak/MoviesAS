package ru.mikhailskiy.intensiv.data.mapper

import ru.mikhailskiy.intensiv.data.dto.movie.MovieDto
import ru.mikhailskiy.intensiv.data.extension.voteAverageToRating
import ru.mikhailskiy.intensiv.data.vo.Movie

class MovieDtoMapper : ViewObjectMapper<Movie, MovieDto> {
    override fun toViewObject(dto: MovieDto): Movie {
        return Movie(
            id = dto.id,
            title = dto.title,
            rating = dto.voteAverage.voteAverageToRating(),
            posterUrl = dto.posterPath,
            backdropUrl = dto.backdropPath,
            releaseYear = if (dto.releaseDate.length >= 4) dto.releaseDate.substring(0, 4) else "",
            description = dto.overview,
            voteCount = dto.voteCount
        )
    }
}