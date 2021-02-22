package ru.mikhailskiy.intensiv.data.movie_model

import ru.mikhailskiy.intensiv.data.ViewObjectMapper
import ru.mikhailskiy.intensiv.data.voteAverageToRating

class MovieDtoToVoConverter : ViewObjectMapper<Movie, MovieDto> {
    override fun toViewObject(dto: MovieDto): Movie {
        return Movie(
            id = dto.id,
            title = dto.title,
            rating = dto.voteAverage.voteAverageToRating(),
            posterUrl = dto.posterPath,
            backdpopUrl = dto.backdropPath,
            voteCount = dto.voteCount
        )
    }
}