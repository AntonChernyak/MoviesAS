package ru.mikhailskiy.intensiv.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.movie_feed_model.Movie
import ru.mikhailskiy.intensiv.data.movie_feed_model.Movie.Companion.MOVIES_TABLE_NAME

@Dao
interface MovieDao {
    @Insert
    fun saveMoviesList(movies: List<Movie>): Completable

    @Query("DELETE FROM $MOVIES_TABLE_NAME")
    fun deleteAllMovies(): Completable

    @Query("SELECT * FROM $MOVIES_TABLE_NAME WHERE movie_id = :id")
    fun getMovieById(id: Int): Single<Movie>

    @Query("SELECT * FROM $MOVIES_TABLE_NAME WHERE type= :type")
    fun getMoviesByCategory(type: String): Observable<List<Movie>>
}