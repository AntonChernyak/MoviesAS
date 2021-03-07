package ru.mikhailskiy.intensiv.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetails
import ru.mikhailskiy.intensiv.data.movie_feed_model.Movie

@Database(entities = [Movie::class, MovieDetails::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
    abstract fun getFavoriteMovieDao(): FavoriteMovieDao

    companion object {
        private var instance: MovieDatabase? = null

        @Synchronized
        fun get(context: Context): MovieDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie-db"
                )
                    .build()
            }
            return instance!!
        }
    }
}