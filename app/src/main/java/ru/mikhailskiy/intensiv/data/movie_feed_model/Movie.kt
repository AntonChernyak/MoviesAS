package ru.mikhailskiy.intensiv.data.movie_feed_model

data class Movie(
    val id: Int = 0,
    val title: String? = "",
    val rating: Float = 0.0f,
    val posterUrl: String? = "",
    val backdropUrl: String? = "",
    val releaseYear: String = "",
    val description: String = "",
    val voteCount: Int = 0
)