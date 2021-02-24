package ru.mikhailskiy.intensiv.data.movie_search_model

import ru.mikhailskiy.intensiv.data.ViewObjectMapper
import ru.mikhailskiy.intensiv.data.voteAverageToRating

class MovieSearchDtoToVoConverter : ViewObjectMapper<MovieSearch, MovieSearchDto> {
    override fun toViewObject(dto: MovieSearchDto): MovieSearch {
        return MovieSearch(
            id = dto.id,
            title = dto.title,
            rating = dto.voteAverage.voteAverageToRating(),
            posterUrl = dto.posterPath,
            releaseYear = dto.releaseDate.substring(0..3),
            description = dto.overview
        )
    }
}