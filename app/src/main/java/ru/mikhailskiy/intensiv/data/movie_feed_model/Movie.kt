package ru.mikhailskiy.intensiv.data.movie_feed_model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.mikhailskiy.intensiv.data.movie_feed_model.Movie.Companion.MOVIES_TABLE_NAME

@Entity(tableName = MOVIES_TABLE_NAME)
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val dbId: Int = 0,
    @ColumnInfo(name = "movie_id")
    val id: Int = 0,
    val title: String? = "",
    val rating: Float = 0.0f,
    @ColumnInfo(name = "poster_url")
    val posterUrl: String? = "",
    val backdropUrl: String? = "",
    @ColumnInfo(name = "release_year")
    val releaseYear: String = "",
    val description: String = "",
    val voteCount: Int = 0,
    var type: String = ""
) {

    companion object {
        const val MOVIES_TABLE_NAME = "movies"
    }
}