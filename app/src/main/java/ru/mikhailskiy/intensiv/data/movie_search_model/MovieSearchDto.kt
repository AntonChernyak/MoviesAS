package ru.mikhailskiy.intensiv.data.movie_search_model;

import com.google.gson.annotations.SerializedName

data class MovieSearchDto(
    @SerializedName("poster_path")
    val posterPath: String?,
    val id: Int,
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String
)