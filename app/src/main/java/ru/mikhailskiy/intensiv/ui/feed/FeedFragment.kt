package ru.mikhailskiy.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function4
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.movie_feed_model.Movie
import ru.mikhailskiy.intensiv.data.movie_feed_model.MovieDtoToVoConverter
import ru.mikhailskiy.intensiv.data.movie_feed_model.MovieResponse
import ru.mikhailskiy.intensiv.extensions.addLoader
import ru.mikhailskiy.intensiv.extensions.afterTextChanged
import ru.mikhailskiy.intensiv.extensions.threadSwitch
import ru.mikhailskiy.intensiv.network.MovieApiClient
import timber.log.Timber

class FeedFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()
    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Добавляем recyclerView
        movies_search_recycler_view.layoutManager = LinearLayoutManager(context)
        movies_search_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        search_toolbar.search_edit_text.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > 3) {
                openSearch(it.toString())
            }
        }

        getDataFromNet()
    }

    private fun getDataFromNet() {
        compositeDisposable.add(Single.zip(
            MovieApiClient.apiClient.getTopRatedMovies(),
            MovieApiClient.apiClient.getPopularMovies(),
            MovieApiClient.apiClient.getNowPlayingMovie(),
            MovieApiClient.apiClient.getUpcomingMovies(),
            Function4<MovieResponse, MovieResponse, MovieResponse, MovieResponse, List<MovieResponse>> { topRated, popular, nowPlaying, upcoming ->
                return@Function4 listOf(topRated, popular, nowPlaying, upcoming)
            })
            .map { list ->
                return@map list.map { movieResponse ->
                    movieResponse.results?.let { dtoList ->
                        MovieDtoToVoConverter().toViewObject(dtoList)
                    }
                }
            }
            .threadSwitch()
            .addLoader(feed_progress_bar)
            .subscribe({
                it[0]?.let { movieVoList ->
                    addMovieListToAdapter(movieVoList, R.string.top_rated, 2000)
                }
                it[1]?.let { movieVoList ->
                    addMovieListToAdapter(movieVoList, R.string.popular, 500)
                }
                it[2]?.let { movieVoList ->
                    addMovieListToAdapter(movieVoList, R.string.now_playing, 0)
                }
                it[3]?.let { movieVoList ->
                    addMovieListToAdapter(movieVoList, R.string.upcoming, 0)
                }
            }, { e ->
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error) + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            })
        )
    }

    private fun addMovieListToAdapter(
        moviesVoList: List<Movie>,
        @StringRes label: Int,
        voteCount: Int
    ) {
        val moviesItemList = listOf(moviesVoList
            .filter {
                it.voteCount >= voteCount
            }.map { movieVo ->
                MovieItem(movieVo) {
                    openMovieDetails(movieVo)
                }
            }.let { MainCardContainer(label, it) })

        adapter.apply { addAll(moviesItemList) }
    }

    private fun openMovieDetails(movie: Movie) {
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

    private fun openSearch(searchText: String) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putString(ARG_SEARCH, searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
    }

    override fun onStop() {
        super.onStop()
        search_toolbar.clear()
        compositeDisposable.clear()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    companion object {
        const val ARG_MOVIE_ID = "arg movie id"
        const val ARG_SEARCH = "arg search"
    }
}