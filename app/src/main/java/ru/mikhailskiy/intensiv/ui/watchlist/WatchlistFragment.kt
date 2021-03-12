package ru.mikhailskiy.intensiv.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_watchlist.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetails
import ru.mikhailskiy.intensiv.database.MovieDatabase
import ru.mikhailskiy.intensiv.extensions.threadSwitch
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment
import ru.mikhailskiy.intensiv.ui.movie_details.MovieDetailsFragment

class WatchlistFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var moviesList: MutableList<MoviePreviewItem>
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
        getFavoriteMoviesFromDb()
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
        bundle.putString(
            FeedFragment.ARG_DB_TYPE,
            MovieDetailsFragment.TableType.FAVORITE_MOVIE.name
        )
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    private fun getFavoriteMoviesFromDb() {
        compositeDisposable.add(MovieDatabase
            .get(requireActivity())
            .getFavoriteMovieDao()
            .getAllFavoriteMovies()
            .map { movies ->
                movies.map { movie ->
                    MoviePreviewItem(
                        movie,
                        { openMovieDetails(movie) },
                        { position ->
                            adapter.remove(moviesList[position])
                            deleteMovieFromDb(movie)
                        }
                    )
                }
            }
            .threadSwitch()
            .subscribe { list ->
                moviesList = list.toMutableList()
                movies_search_recycler_view.adapter = adapter.apply {
                    clear()
                    addAll(moviesList)
                }
            }
        )
    }

    private fun deleteMovieFromDb(movie: MovieDetails) {
        compositeDisposable.add(
            MovieDatabase
                .get(requireActivity())
                .getFavoriteMovieDao()
                .deleteFavoriteMovie(movie)
                .threadSwitch()
                .subscribe {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.remove_from_favorite),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        )
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    companion object {
        @JvmStatic
        fun newInstance() = WatchlistFragment()
    }
}