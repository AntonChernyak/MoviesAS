package ru.mikhailskiy.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_fragment.movies_search_recycler_view
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.movie_feed_model.Movie
import ru.mikhailskiy.intensiv.database.MovieDatabase
import ru.mikhailskiy.intensiv.extensions.addLoader
import ru.mikhailskiy.intensiv.extensions.afterTextChanged
import ru.mikhailskiy.intensiv.extensions.threadSwitch
import ru.mikhailskiy.intensiv.extensions.toMoviesList
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.providers.ObservableCacheProvider
import ru.mikhailskiy.intensiv.providers.RepositoryAccess
import ru.mikhailskiy.intensiv.ui.movie_details.MovieDetailsFragment
import timber.log.Timber

class FeedFragment : Fragment(), ObservableCacheProvider<Map<FeedFragment.MovieType, List<Movie>>> {

    private var moviesMap = HashMap<MovieType, List<Movie>>()
    private val compositeDisposable = CompositeDisposable()
    private val movieApi by lazy {
        MovieApiClient.apiClient
    }
    private val movieDao by lazy {
        MovieDatabase.get(requireActivity()).getMovieDao()
    }
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
        movies_search_recycler_view.adapter = adapter.apply { this.clear() }

        search_toolbar.search_edit_text.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > 3) {
                openSearch(it.toString())
            }
        }
        getData()
    }

    override fun onStop() {
        super.onStop()
        search_toolbar.clear()
        saveMoviesToDb()
        compositeDisposable.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun createRemoteObservable(): Observable<Map<MovieType, List<Movie>>> {
        return Observable.zip(
            movieApi.getTopRatedMovies().map { it.toMoviesList(MovieType.TOP_RATED.name) },
            movieApi.getPopularMovies().map { it.toMoviesList(MovieType.POPULAR.name) },
            movieApi.getNowPlayingMovie().map { it.toMoviesList(MovieType.NOW_PLAYING.name) },
            movieApi.getUpcomingMovies().map { it.toMoviesList(MovieType.UPCOMING.name) },
            Function4<List<Movie>, List<Movie>, List<Movie>, List<Movie>, Map<MovieType, List<Movie>>> { topRated, popular, nowPlaying, upcoming ->

                clearDatabase()

                val map = HashMap<MovieType, List<Movie>>()
                    .plus(MovieType.TOP_RATED to topRated)
                    .plus(MovieType.POPULAR to popular)
                    .plus(MovieType.NOW_PLAYING to nowPlaying)
                    .plus(MovieType.UPCOMING to upcoming)
                moviesMap = map as HashMap<MovieType, List<Movie>>
                return@Function4 map
            })
    }

    override fun createOfflineObservable(): Observable<Map<MovieType, List<Movie>>> {
        return Observable.zip(
            movieDao.getMoviesByCategory(MovieType.TOP_RATED.name),
            movieDao.getMoviesByCategory(MovieType.POPULAR.name),
            movieDao.getMoviesByCategory(MovieType.NOW_PLAYING.name),
            movieDao.getMoviesByCategory(MovieType.UPCOMING.name),
            Function4<List<Movie>, List<Movie>, List<Movie>, List<Movie>, Map<MovieType, List<Movie>>> { topRated, popular, nowPlaying, upcoming ->

                if (topRated.isEmpty() || popular.isEmpty() || nowPlaying.isEmpty() || upcoming.isEmpty()) {
                    throw IllegalStateException("EMPTY RESPONSE")
                }
                return@Function4 HashMap<MovieType, List<Movie>>()
                    .plus(MovieType.TOP_RATED to topRated)
                    .plus(MovieType.POPULAR to popular)
                    .plus(MovieType.NOW_PLAYING to nowPlaying)
                    .plus(MovieType.UPCOMING to upcoming)
            })
    }

    private fun getData() {
        compositeDisposable.add(
            getObservable(RepositoryAccess.OFFLINE_FIRST)
                .threadSwitch()
                .addLoader(feed_progress_bar as ProgressBar)
                .doOnNext { feed_progress_bar.visibility = View.GONE }
                .subscribe({
                    it[MovieType.TOP_RATED]?.let { movieVoList ->
                        addMovieListToAdapter(movieVoList, R.string.top_rated, 2000)
                    }
                    it[MovieType.POPULAR]?.let { movieVoList ->
                        addMovieListToAdapter(movieVoList, R.string.popular, 500)
                    }
                    it[MovieType.NOW_PLAYING]?.let { movieVoList ->
                        addMovieListToAdapter(movieVoList, R.string.now_playing, 0)
                    }
                    it[MovieType.UPCOMING]?.let { movieVoList ->
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
        val moviesItemList = listOf(
            moviesVoList
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
        bundle.putString(ARG_DB_TYPE, MovieDetailsFragment.TableType.MOVIE.name)
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

    private fun saveMoviesToDb() {
        moviesMap[MovieType.TOP_RATED]?.let {
            compositeDisposable.add(
                movieDao
                    .saveMoviesList(it)
                    .subscribeOn(Schedulers.io())
                    .subscribe({}, { e -> throw IllegalStateException(e.message) })
            )
        }
        moviesMap[MovieType.POPULAR]?.let {
            compositeDisposable.add(
                movieDao
                    .saveMoviesList(it)
                    .subscribeOn(Schedulers.io())
                    .subscribe({}, { e -> throw IllegalStateException(e.message) })
            )
        }
        moviesMap[MovieType.NOW_PLAYING]?.let {
            compositeDisposable.add(
                movieDao
                    .saveMoviesList(it)
                    .subscribeOn(Schedulers.io())
                    .subscribe({}, { e -> throw IllegalStateException(e.message) })
            )
        }
        moviesMap[MovieType.UPCOMING]?.let {
            compositeDisposable.add(
                movieDao
                    .saveMoviesList(it)
                    .subscribeOn(Schedulers.io())
                    .subscribe({}, { e -> throw IllegalStateException(e.message) })
            )
        }
    }

    private fun clearDatabase() {
        compositeDisposable.add(
            movieDao
                .deleteAllMovies()
                .subscribeOn(Schedulers.computation())
                .subscribe({}, { e -> throw IllegalStateException(e.message) })
        )
    }

    enum class MovieType {
        POPULAR,
        TOP_RATED,
        NOW_PLAYING,
        UPCOMING
    }

    companion object {
        const val ARG_MOVIE_ID = "arg movie id"
        const val ARG_SEARCH = "arg search"
        const val ARG_DB_TYPE = "db_type"
    }
}