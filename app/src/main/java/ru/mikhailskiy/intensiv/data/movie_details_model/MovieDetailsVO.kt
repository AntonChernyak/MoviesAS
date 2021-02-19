package ru.mikhailskiy.intensiv.data.movie_details_model

data class MovieDetailsVO(
    val genres: String,
    val id: Int = 0,
    val overview: String? = "",
    val posterPath: String? = "",
    val productionCompanies: String,
    val year: String = "",
    val title: String = "",
    val voteCount: Int = 0,
    val rating: Float = 0.0f,
    var isFavorite: Boolean = false
)