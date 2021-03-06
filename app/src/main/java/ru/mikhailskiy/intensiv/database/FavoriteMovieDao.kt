package ru.mikhailskiy.intensiv.database

import androidx.room.*
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetails

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveFavoriteMovie(movie: MovieDetails)

    @Delete
    fun deleteFavoriteMovie(movie: MovieDetails)

    @Query("SELECT * FROM Favorite_Movies")
    fun getAllFavoriteMovies(): List<MovieDetails>

    @Query("DELETE FROM Favorite_Movies")
    fun deleteAllFavoriteMovies()

    @Query("SELECT EXISTS (SELECT 1 FROM Favorite_Movies WHERE movie_id = :id)")
    fun exists(id: Int): Boolean
}