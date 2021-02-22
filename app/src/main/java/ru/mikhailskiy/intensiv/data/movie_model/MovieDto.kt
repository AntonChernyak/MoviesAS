package ru.mikhailskiy.intensiv.data.movie_model

import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("poster_path")
    val posterPath: String?,
    val id: Int,
    val title: String,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)