package ru.mikhailskiy.intensiv.data.movie_feed_model

import ru.mikhailskiy.intensiv.data.ViewObjectMapper
import ru.mikhailskiy.intensiv.extensions.voteAverageToRating

class MovieDtoToVoConverter : ViewObjectMapper<Movie, MovieDto> {
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