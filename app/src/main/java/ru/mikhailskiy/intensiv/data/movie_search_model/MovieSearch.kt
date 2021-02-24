package ru.mikhailskiy.intensiv.data.movie_search_model

data class MovieSearch(
    val id: Int = 0,
    val title: String? = "",
    val rating: Float = 0.0f,
    val posterUrl: String? = "",
    val releaseYear: String = "",
    val description: String = ""
)