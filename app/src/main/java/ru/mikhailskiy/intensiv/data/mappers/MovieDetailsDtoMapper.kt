package ru.mikhailskiy.intensiv.data.mappers

import ru.mikhailskiy.intensiv.data.dto.movies_details_dto.MovieDetailsDto
import ru.mikhailskiy.intensiv.data.extensions.voteAverageToRating
import ru.mikhailskiy.intensiv.data.vo.MovieDetails

class MovieDetailsDtoMapper : ViewObjectMapper<MovieDetails, MovieDetailsDto> {
    override fun toViewObject(dto: MovieDetailsDto): MovieDetails {
        return MovieDetails(
            id = dto.id,
            title = dto.title,
            overview = dto.overview,
            posterPath = dto.posterPath,
            voteCount = dto.voteCount,
            rating = dto.voteAverage.voteAverageToRating(),
            year = dto.releaseDate.substring(0, 4),
            genres = dto.genreDtos.joinToString { it.name },
            productionCompanies = dto.productionCompanyDtos.joinToString { it.name }
        )
    }
}