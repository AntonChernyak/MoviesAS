package ru.mikhailskiy.intensiv.data.movie_model

data class Movie(
    val id: Int = 0,
    val title: String? = "",
    val rating: Float = 0.0f,
    val voteCount: Int = 0,
    val posterUrl: String? = "",
    val backdpopUrl: String? = ""
)