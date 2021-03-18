package ru.mikhailskiy.intensiv.data.repository.remote

import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.mapper.MovieDtoMapper
import ru.mikhailskiy.intensiv.data.network.MovieApiInterface
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.TvShowsRepository

class TvShowsRemoteRepository(private val movieApi: MovieApiInterface) : TvShowsRepository {
    override fun getTvShows(): Single<List<Movie>> {
        return movieApi
            .getPopularTvShowsList()
            .map {
                it.results?.let { it1 -> MovieDtoMapper().toViewObject(it1) }
            }
    }
}