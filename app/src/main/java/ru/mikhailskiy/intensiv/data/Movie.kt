package ru.mikhailskiy.intensiv.data

class Movie(
    var title: String? = "",
    var voteAverage: Double = 0.0,
    val posterUrl: String = "",
    val description: String = "",
    val actors: ArrayList<Actor> = ArrayList(),
    val studio: String = "",
    val genre: List<String> = ArrayList(),
    val year: String = ""
) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
