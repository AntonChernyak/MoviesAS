package ru.mikhailskiy.intensiv.data.movie_feed_model

import ru.mikhailskiy.intensiv.data.ViewObjectMapper
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetails

class MovieToMovieDetailsConverter : ViewObjectMapper<MovieDetails, Movie> {
    override fun toViewObject(dto: Movie): MovieDetails {
        return MovieDetails(
            id = dto.id,
            title = dto.title ?: "",
            overview = dto.description,
            posterPath = dto.posterUrl,
            voteCount = dto.voteCount,
            rating = dto.rating,
            year = "",
            genres = "",
            productionCompanies = ""
        )
    }
}