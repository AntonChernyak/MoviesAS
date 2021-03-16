package ru.mikhailskiy.intensiv.presentation.feed

import android.annotation.SuppressLint
import android.util.Log
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.usecase.FeedFragmentUseCase
import ru.mikhailskiy.intensiv.presentation.base.BasePresenter
import timber.log.Timber

class FeedPresenter(private val useCase: FeedFragmentUseCase) :
    BasePresenter<FeedPresenter.FeedView>() {

    @SuppressLint("CheckResult")
    fun getMovies() {
        useCase.getMovies()
            .doOnSubscribe { view?.showLoading() }
            .doFinally { view?.hideLoading() }
            .subscribe(
                {
                    Log.d("TAGGG", "map = $it")
                    view?.showMovies(it)
                },
                { t ->
                    Log.d("TAGGG", "error")
                    Timber.e(t, t.toString())
                    view?.showEmptyMovies()
                })
    }

    fun saveMovies(moviesList: List<Movie>) {
        useCase
            .saveMovies(moviesList)
            .subscribe()
    }

    interface FeedView {
        fun showMovies(map: Map<FeedFragment.MovieType, List<Movie>>)
        fun showLoading()
        fun hideLoading()
        fun showEmptyMovies()
        //fun showError()
    }
}