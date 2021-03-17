package ru.mikhailskiy.intensiv.data.repository.remote

import io.reactivex.Single
import ru.mikhailskiy.intensiv.data.mappers.MovieDtoMapper
import ru.mikhailskiy.intensiv.data.network.MovieApiClient
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.TvShowsRepository

class TvShowsRemoteRepository : TvShowsRepository {
    override fun getTvShows(): Single<List<Movie>> {
        return MovieApiClient.apiClient.getPopularTvShowsList().map {
            it.results?.let { it1 -> MovieDtoMapper().toViewObject(it1) }
        }
    }
}