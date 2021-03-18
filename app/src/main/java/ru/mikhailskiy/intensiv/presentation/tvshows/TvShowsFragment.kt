package ru.mikhailskiy.intensiv.presentation.tvshows

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.network.MovieApiClient
import ru.mikhailskiy.intensiv.data.repository.remote.TvShowsRemoteRepository
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.usecase.TvShowsFragmentUseCase
import ru.mikhailskiy.intensiv.presentation.feed.FeedFragment.Companion.ARG_MOVIE_ID

class TvShowsFragment : Fragment(), TvShowsPresenter.TvShowsView {

    private val compositeDisposable = CompositeDisposable()
    private val movieApi by lazy {
        MovieApiClient.apiClient
    }
    private val tvShowsPresenter by lazy {
        TvShowsPresenter(TvShowsFragmentUseCase(TvShowsRemoteRepository(movieApi)))
    }
    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.tv_shows_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvShowsPresenter.attachView(this)
        tvShowsPresenter.getTvShows()
    }

    override fun onStop() {
        super.onStop()
        tvShowsPresenter.detachView()
        compositeDisposable.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun showTvShows(list: List<Movie>) {
        val tvShowsItems = list.map { tvShow ->
            TvShowItem(tvShow) { openTvShowDetails(tvShow) }
        }
        tv_shows_recycler_view.adapter = adapter.apply { addAll(tvShowsItems) }
    }

    override fun showLoading() {
        tv_show_progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        tv_show_progress_bar.visibility = View.GONE
    }

    override fun showEmptyMovies() {
        tv_shows_recycler_view.adapter = adapter.apply { addAll(mutableListOf()) }
    }

    private fun openTvShowDetails(movie: Movie) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putInt(ARG_MOVIE_ID, movie.id)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

}