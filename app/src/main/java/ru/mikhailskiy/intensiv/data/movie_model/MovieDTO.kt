package ru.mikhailskiy.intensiv.data.movie_model

import com.google.gson.annotations.SerializedName

data class MovieDTO(
    @SerializedName("poster_path")
    val posterPath: String? = "",
    val adult: Boolean = false,
    val overview: String = "",
    @SerializedName("release_date")
    val releaseDate: String = "",
    @SerializedName("genre_ids")
    val genreIds: List<Int> = mutableListOf(),
    val id: Int = 0,
    @SerializedName("original_title")
    val originalTitle: String = "",
    @SerializedName("original_language")
    val originalLanguage: String = "",
    val title: String = "",
    @SerializedName("backdrop_path")
    val backdropPath: String? = "",
    val popularity: Double = 0.0,
    @SerializedName("vote_count")
    val voteCount: Int = 0,
    val video: Boolean = false,
    @SerializedName("vote_average")
    val voteAverage: Double = 0.0
)