package ru.mikhailskiy.intensiv.data.dto.movie_details

import com.google.gson.annotations.SerializedName

data class MovieDetailsDto(
    @SerializedName("genres")
    val genreDtos: List<GenreDto>,
    val id: Int,
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanyDtos: List<CompanyDto>,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("vote_average")
    val voteAverage: Double
)