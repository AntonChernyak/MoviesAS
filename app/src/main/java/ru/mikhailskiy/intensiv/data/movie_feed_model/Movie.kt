package ru.mikhailskiy.intensiv.data.movie_feed_model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Movies")
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val id: Int = 0,
    val title: String? = "",
    @Ignore
    val rating: Float = 0.0f,
    @ColumnInfo(name = "poster_url")
    val posterUrl: String? = "",
    @Ignore
    val backdropUrl: String? = "",
    @ColumnInfo(name = "release_year")
    val releaseYear: String = "",
    @Ignore
    val description: String = "",
    @Ignore
    val voteCount: Int = 0
)