package ru.mikhailskiy.intensiv.data.movie_model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieVO(
    var title: String? = "",
    var voteAverage: Double = 0.0,
    val posterUrl: String? = ""
    //  val description: String = "",
    //  val actors: ArrayList<Actor> = ArrayList(),
    //   val studio: String = "",
    // val genre: List<Int> = ArrayList(),
    // val year: String = "",
    // var isFavorite: Boolean = false
) : Parcelable {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
