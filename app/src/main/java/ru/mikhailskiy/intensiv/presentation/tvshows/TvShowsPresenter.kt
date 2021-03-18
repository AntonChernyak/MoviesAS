package ru.mikhailskiy.intensiv.presentation.tvshows

import android.annotation.SuppressLint
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.usecase.TvShowsFragmentUseCase
import ru.mikhailskiy.intensiv.presentation.base.BasePresenter
import timber.log.Timber


class TvShowsPresenter(private val useCase: TvShowsFragmentUseCase) : BasePresenter<TvShowsPresenter.TvShowsView>() {

    @SuppressLint("CheckResult")
    fun getTvShows() {
        useCase.getTvShows()
            .doOnSubscribe { view?.showLoading() }
            .doFinally { view?.hideLoading() }
            .subscribe(
                {
                    view?.showTvShows(it)
                },
                { t ->
                    Timber.e(t, t.toString())
                    view?.showEmptyMovies()
                })
    }

    interface TvShowsView {
        fun showTvShows(list: List<Movie>)
        fun showLoading()
        fun hideLoading()
        fun showEmptyMovies()
    }
}
