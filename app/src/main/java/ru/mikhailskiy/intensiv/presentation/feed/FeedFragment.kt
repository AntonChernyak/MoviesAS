package ru.mikhailskiy.intensiv.presentation.feed

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.extension.afterTextChanged
import ru.mikhailskiy.intensiv.data.repository.local.*
import ru.mikhailskiy.intensiv.data.repository.remote.NowPlayingMoviesRemoteRepository
import ru.mikhailskiy.intensiv.data.repository.remote.PopularMoviesRemoteRepository
import ru.mikhailskiy.intensiv.data.repository.remote.TopRatedMoviesRemoteRpository
import ru.mikhailskiy.intensiv.data.repository.remote.UpcomingMoviesRemoteRepository
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.domain.repository.MoviesRepository
import ru.mikhailskiy.intensiv.domain.usecase.FeedFragmentUseCase
import ru.mikhailskiy.intensiv.presentation.movie_details.MovieDetailsFragment
import timber.log.Timber

class FeedFragment : Fragment(), FeedPresenter.FeedView {

    // Инициализируем
    private val presenter: FeedPresenter by lazy {
        FeedPresenter(
            FeedFragmentUseCase(
                createRemoteRepository(),
                createLocalRepository(),
                MoviesStoreRepository(requireActivity())
            )
        )
    }

    private var moviesMap = HashMap<MovieType, List<Movie>>()
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
        movies_search_recycler_view.adapter = adapter.apply { this.clear() }

        presenter.attachView(this)

        search_toolbar.search_edit_text.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > 3) {
                openSearch(it.toString())
            }
        }

        presenter.getMovies()
    }

    override fun onStop() {
        super.onStop()
        search_toolbar.clear()
        saveMoviesToDb()
        presenter.detachView()
        compositeDisposable.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }


    override fun showMovies(map: Map<MovieType, List<Movie>>) {
        moviesMap = map as HashMap<MovieType, List<Movie>>
        map[MovieType.TOP_RATED]?.let { movieVoList ->
            addMovieListToAdapter(movieVoList, R.string.top_rated, TOP_RATED_VOTE_COUNT)
        }
        map[MovieType.POPULAR]?.let { movieVoList ->
            addMovieListToAdapter(movieVoList, R.string.popular, POPULAR_VOTE_COUNT)
        }
        map[MovieType.NOW_PLAYING]?.let { movieVoList ->
            addMovieListToAdapter(movieVoList, R.string.now_playing, NOW_PLAYING_VOTE_COUNT)
        }
        map[MovieType.UPCOMING]?.let { movieVoList ->
            addMovieListToAdapter(movieVoList, R.string.upcoming, UPCOMING_VOTE_COUNT)
        }
    }

    override fun showLoading() {
        feed_progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        feed_progress_bar.visibility = View.GONE
    }

    override fun showEmptyMovies() {
        addMovieListToAdapter(emptyList(), R.string.top_rated)
        addMovieListToAdapter(emptyList(), R.string.popular)
        addMovieListToAdapter(emptyList(), R.string.now_playing)
        addMovieListToAdapter(emptyList(), R.string.upcoming)
    }

    private fun addMovieListToAdapter(
        moviesVoList: List<Movie>,
        @StringRes label: Int,
        voteCount: Int = 0
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
        val sumList = listOf(
            moviesMap[MovieType.TOP_RATED],
            moviesMap[MovieType.POPULAR],
            moviesMap[MovieType.NOW_PLAYING],
            moviesMap[MovieType.UPCOMING]
        ).flatMap { it ?: ArrayList() }

        presenter.saveMovies(sumList)
    }

    private fun createRemoteRepository(): HashMap<MovieType, MoviesRepository> {
        return hashMapOf(
            MovieType.TOP_RATED to TopRatedMoviesRemoteRpository(),
            MovieType.UPCOMING to UpcomingMoviesRemoteRepository(),
            MovieType.POPULAR to PopularMoviesRemoteRepository(),
            MovieType.NOW_PLAYING to NowPlayingMoviesRemoteRepository()
        )
    }

    private fun createLocalRepository(): HashMap<MovieType, MoviesRepository> {
        return hashMapOf(
            MovieType.TOP_RATED to TopRatedMoviesLocalRepository(requireActivity()),
            MovieType.UPCOMING to UpcomingMoviesLocalRepository(requireActivity()),
            MovieType.POPULAR to PopularMoviesLocalRepository(requireActivity()),
            MovieType.NOW_PLAYING to NowPlayingMoviesLocalRepository(requireActivity())
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
        const val TOP_RATED_VOTE_COUNT = 2000
        const val POPULAR_VOTE_COUNT = 500
        const val NOW_PLAYING_VOTE_COUNT = 0
        const val UPCOMING_VOTE_COUNT = 0
    }
}