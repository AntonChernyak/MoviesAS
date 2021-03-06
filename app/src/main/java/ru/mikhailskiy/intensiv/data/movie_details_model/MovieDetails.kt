package ru.mikhailskiy.intensiv.data.movie_details_model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite_Movies")
data class MovieDetails(
    val genres: String,
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val id: Int = 0,
    @ColumnInfo(name = "description")
    val overview: String? = "",
    @ColumnInfo(name = "poster_path")
    val posterPath: String? = "",
    @ColumnInfo(name = "production_companies")
    val productionCompanies: String,
    val year: String = "",
    val title: String = "",
    @ColumnInfo(name = "vote_count")
    val voteCount: Int = 0,
    val rating: Float = 0.0f,
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false
)