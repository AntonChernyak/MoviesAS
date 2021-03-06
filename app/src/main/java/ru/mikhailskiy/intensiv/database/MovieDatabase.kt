package ru.mikhailskiy.intensiv.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class MovieDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao

    companion object {
        private var instance: MovieDatabase? = null

        @Synchronized
        fun get(context: Context): MovieDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    MovieDatabase::class.java,
                    "movie-db"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}