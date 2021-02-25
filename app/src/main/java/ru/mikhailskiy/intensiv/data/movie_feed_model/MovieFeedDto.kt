package ru.mikhailskiy.intensiv.data.movie_feed_model

import com.google.gson.annotations.SerializedName

data class MovieFeedDto(
    @SerializedName("poster_path")
    val posterPath: String?,
    val id: Int,
    @SerializedName("title", alternate = ["name"])
    val title: String,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    val overview: String,
    @SerializedName("release_date", alternate = ["first_air_date"])
    val releaseDate: String
)