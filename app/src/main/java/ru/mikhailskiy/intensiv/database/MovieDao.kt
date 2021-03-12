package ru.mikhailskiy.intensiv.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.movie_feed_model.Movie

@Dao
interface MovieDao {
    @Insert
    fun saveMoviesList(movies: List<Movie>): Completable

    @Query("DELETE FROM Movies")
    fun deleteAllMovies(): Completable

    @Query("SELECT * FROM Movies WHERE movie_id = :id")
    fun getMovieById(id: Int): Single<Movie>

    @Query("SELECT * FROM Movies WHERE type= :type")
    fun getMoviesByCategory(type: String): Observable<List<Movie>>
}