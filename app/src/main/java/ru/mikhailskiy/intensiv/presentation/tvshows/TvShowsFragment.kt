package ru.mikhailskiy.intensiv.presentation.tvshows

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.extensions.addLoader
import ru.mikhailskiy.intensiv.data.extensions.threadSwitch
import ru.mikhailskiy.intensiv.data.mappers.MovieDtoMapper
import ru.mikhailskiy.intensiv.data.network.MovieApiClient
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.presentation.feed.FeedFragment.Companion.ARG_MOVIE_ID

class TvShowsFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()
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

        compositeDisposable.add(
            MovieApiClient.apiClient.getPopularTvShowsList()
                .map { it.results?.let { it1 -> MovieDtoMapper().toViewObject(it1) } }
                .threadSwitch()
                .addLoader(tv_show_progress_bar as ProgressBar)
                .subscribe({ tvShowsVOList ->
                    val tvShowsItems = tvShowsVOList?.map { tvShow ->
                        TvShowItem(tvShow) { openTvShowDetails(tvShow) }
                    }

                    tv_shows_recycler_view.adapter =
                        adapter.apply { tvShowsItems?.let { addAll(it) } }
                }, { e ->
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.error) + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                })
        )
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    companion object {
        @JvmStatic
        fun newInstance(movie: Movie) =
            TvShowsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_MOVIE_ID, movie.id)
                }
            }
    }
}