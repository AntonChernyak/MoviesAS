package ru.mikhailskiy.intensiv.data.movie_details_model

import ru.mikhailskiy.intensiv.data.ViewObjectMapper
import ru.mikhailskiy.intensiv.extensions.voteAverageToRating

class MovieDetailsDtoToVoConverter : ViewObjectMapper<MovieDetails, MovieDetailsDto> {
    override fun toViewObject(dto: MovieDetailsDto): MovieDetails {
        return MovieDetails(
            id = dto.id,
            title = dto.title,
            overview = dto.overview,
            posterPath = dto.posterPath,
            voteCount = dto.voteCount,
            rating = dto.voteAverage.voteAverageToRating(),
            year = dto.releaseDate.substring(0, 4),
            genres = dto.genres.joinToString { it.name },
            productionCompanies = dto.productionCompanies.joinToString { it.name }
        )
    }
}