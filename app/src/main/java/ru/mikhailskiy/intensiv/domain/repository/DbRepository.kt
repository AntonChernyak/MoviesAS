package ru.mikhailskiy.intensiv.domain.repository

import io.reactivex.Completable
import ru.mikhailskiy.intensiv.data.vo.Movie

interface DbRepository {
    fun saveMovies(moviesList: List<Movie>): Completable
}