package ru.mikhailskiy.intensiv.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_watchlist.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetails
import ru.mikhailskiy.intensiv.database.MovieDatabase
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment

class WatchlistFragment : Fragment() {

    val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movies_search_recycler_view.layoutManager = GridLayoutManager(context, 4)
        movies_search_recycler_view.adapter = adapter.apply { this.clear() }

        val moviesList =
            MovieDatabase.get(requireActivity()).getFavoriteMovieDao().getAllFavoriteMovies()
                .map {
                    MoviePreviewItem(it) { movies -> openMovieDetails(movies) }
                }

        movies_search_recycler_view.adapter = adapter.apply { addAll(moviesList) }
    }

    private fun openMovieDetails(movie: MovieDetails) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putInt(FeedFragment.ARG_MOVIE_ID, movie.id)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    companion object {
        @JvmStatic
        fun newInstance() = WatchlistFragment()
    }
}