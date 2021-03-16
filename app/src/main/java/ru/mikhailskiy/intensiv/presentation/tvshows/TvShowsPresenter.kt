package ru.mikhailskiy.intensiv.presentation.tvshows

import android.annotation.SuppressLint
import android.util.Log
import ru.mikhailskiy.intensiv.data.vo.Movie
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
                    Log.d("TAGGG", "map = $it")
                    view?.showTvShows(it)
                },
                { t ->
                    Log.d("TAGGG", "error")
                    Timber.e(t, t.toString())
                    view?.showEmptyMovies()
                })
    }

    interface TvShowsView {
        fun showTvShows(list: List<Movie>)
        fun showLoading()
        fun hideLoading()
        fun showEmptyMovies()
        //fun showError()
    }
}