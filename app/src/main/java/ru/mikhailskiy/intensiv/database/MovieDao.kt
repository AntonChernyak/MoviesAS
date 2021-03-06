package ru.mikhailskiy.intensiv.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.mikhailskiy.intensiv.data.movie_feed_model.Movie

@Dao
interface MovieDao {

    @Insert
    fun saveMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)

    @Query("SELECT * FROM Favorite_Movies")
    fun getAllFavoriteMovies(): List<Movie>

    @Query("DELETE FROM Favorite_Movies")
    fun deleteAllFavoriteMovies()
}