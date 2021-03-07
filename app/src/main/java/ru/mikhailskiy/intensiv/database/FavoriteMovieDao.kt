package ru.mikhailskiy.intensiv.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetails

@Dao
interface FavoriteMovieDao {
    @Insert
    fun saveFavoriteMovie(movie: MovieDetails): Completable

    @Delete
    fun deleteFavoriteMovie(movie: MovieDetails): Completable

    @Query("SELECT * FROM Favorite_Movies")
    fun getAllFavoriteMovies(): Observable<List<MovieDetails>>

    @Query("SELECT EXISTS (SELECT 1 FROM Favorite_Movies WHERE movie_id = :id)")
    fun exists(id: Int): Single<Boolean>

    @Query("SELECT * FROM Favorite_Movies WHERE movie_id = :id")
    fun getFavoriteMovieById(id: Int): Single<MovieDetails>
}