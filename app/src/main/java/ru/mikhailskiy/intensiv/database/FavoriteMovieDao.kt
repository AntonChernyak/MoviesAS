package ru.mikhailskiy.intensiv.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetails

@Dao
interface FavoriteMovieDao {
    @Insert
    fun saveFavoriteMovie(movie: MovieDetails)

    @Delete
    fun deleteFavoriteMovie(movie: MovieDetails)

    @Query("SELECT * FROM Favorite_Movies")
    fun getAllFavoriteMovies(): List<MovieDetails>

    @Query("DELETE FROM Favorite_Movies")
    fun deleteAllFavoriteMovies()
}