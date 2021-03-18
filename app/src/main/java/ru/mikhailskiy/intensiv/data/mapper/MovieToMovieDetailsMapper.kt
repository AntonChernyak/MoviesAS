package ru.mikhailskiy.intensiv.data.mapper

import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.data.vo.MovieDetails

class MovieToMovieDetailsMapper : ViewObjectMapper<MovieDetails, Movie> {
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