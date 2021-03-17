package ru.mikhailskiy.intensiv.data.mapper

import ru.mikhailskiy.intensiv.data.dto.movie_details.MovieDetailsDto
import ru.mikhailskiy.intensiv.data.extension.voteAverageToRating
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
            year = if (dto.releaseDate.length >= 4) dto.releaseDate.substring(0, 4) else "",
            genres = dto.genreDtos.joinToString { it.name },
            productionCompanies = dto.productionCompanyDtos.joinToString { it.name }
        )
    }
}