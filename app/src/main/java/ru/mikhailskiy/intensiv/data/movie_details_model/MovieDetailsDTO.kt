package ru.mikhailskiy.intensiv.data.movie_details_model

import com.google.gson.annotations.SerializedName

data class MovieDetailsDTO(
    @SerializedName("genres")
    val genres: List<Genre>,
    val id: Int,
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("production_companies")
    val productionCompanies: List<Company>,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("vote_average")
    val voteAverage: Double
)