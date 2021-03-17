package ru.mikhailskiy.intensiv.domain.usecase

import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.extensions.threadSwitch
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.TvShowsRepository

class TvShowsFragmentUseCase(private val repository: TvShowsRepository) {

    fun getTvShows(): Single<List<Movie>> {
        return repository.getTvShows().threadSwitch()
    }
}