package ru.mikhailskiy.intensiv.presentation.movie_details

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.movie_details_fragment.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.database.MovieDatabase
import ru.mikhailskiy.intensiv.data.extensions.addLoader
import ru.mikhailskiy.intensiv.data.extensions.loadImage
import ru.mikhailskiy.intensiv.data.extensions.threadSwitch
import ru.mikhailskiy.intensiv.data.mappers.ActorDtoMapper
import ru.mikhailskiy.intensiv.data.mappers.MovieDetailsDtoMapper
import ru.mikhailskiy.intensiv.data.mappers.MovieToMovieDetailsMapper
import ru.mikhailskiy.intensiv.data.network.MovieApiClient
import ru.mikhailskiy.intensiv.data.providers.RepositoryAccess
import ru.mikhailskiy.intensiv.data.providers.SingleCacheProvider
import ru.mikhailskiy.intensiv.data.vo.Movie
import ru.mikhailskiy.intensiv.data.vo.MovieDetails
import ru.mikhailskiy.intensiv.presentation.feed.FeedFragment.Companion.ARG_DB_TYPE
import ru.mikhailskiy.intensiv.presentation.feed.FeedFragment.Companion.ARG_MOVIE_ID

class MovieDetailsFragment : Fragment(), SingleCacheProvider<MovieDetails> {

    private val compositeDisposable = CompositeDisposable()
    private var movieVoId: Int = 1
    private var movie: MovieDetails? = null
    private var dbType: String? = null
    private var menu: Menu? = null
    private val favoriteMovieDao by lazy {
        MovieDatabase.get(requireActivity()).getFavoriteMovieDao()
    }
    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dbType = it.getString(ARG_DB_TYPE)
            movieVoId = it.getInt(ARG_MOVIE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.movie_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity?)?.setSupportActionBar(details_toolbar)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.title = ""
        setHasOptionsMenu(true)

        getMovieById()
        addActorsToRecyclerView()
        actors_recycler_view.isNestedScrollingEnabled = false
    }

    override fun createRemoteSingle(): Single<MovieDetails> {
        return MovieApiClient
            .apiClient
            .getMovieDetails(movieVoId)
            .map {
                MovieDetailsDtoMapper().toViewObject(it)
            }
    }

    override fun createOfflineSingle(): Single<MovieDetails> {
        return favoriteMovieDao
            .exists(movieVoId)
            .flatMap {
                if (it) {
                    favoriteMovieDao.getFavoriteMovieById(movieVoId)
                } else {
                    MovieDatabase
                        .get(requireActivity())
                        .getMovieDao()
                        .getMovieById(movieVoId)
                        .map { movie ->
                            MovieToMovieDetailsMapper().toViewObject(movie)
                        }
                }
            }
    }

    private fun getMovieById() {
        val single = if (dbType == TableType.FAVORITE_MOVIE.name) {
            this@MovieDetailsFragment
                .getSingle(RepositoryAccess.OFFLINE_FIRST)
        } else {
            this@MovieDetailsFragment
                .getSingle(RepositoryAccess.REMOTE_FIRST)
        }

        compositeDisposable.add(
            single
                .threadSwitch()
                .addLoader(details_progress_bar as ProgressBar)
                .subscribe({ movieDetails ->
                    movie = movieDetails

                    details_movie_title_text_view.text = movie?.title
                    details_movie_description_text_view.text = movie?.overview
                    year_text_view.text = movie?.year
                    studio_text_view.text = movie?.productionCompanies
                    genre_text_view.text = movie?.genres

                    movie?.rating?.let { details_movie_rating_bar.rating = it }
                    movie?.posterPath?.let { details_poster_image_view.loadImage(it) }
                    movie?.let {
                        setStartFavoriteIconColor(
                            it,
                            menu?.findItem(R.id.action_add_to_favorite)
                        )
                    }
                }, { e ->
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.error) + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                })
        )
    }

    private fun addActorsToRecyclerView() {
        compositeDisposable.add(
            MovieApiClient.apiClient.getMovieCredits(movieVoId)
                .map { ActorDtoMapper().toViewObject(it.cast) }
                .threadSwitch()
                .subscribe({ creditsResponse ->
                    val actors = creditsResponse.map {
                        ActorItem(it)
                    }
                    actors_recycler_view.adapter = adapter.apply { addAll(actors) }
                }, { error ->
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.error) + error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                })
        )
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_menu, menu)
        this.menu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_to_favorite -> {
                if (movie?.isFavorite == true) {
                    item.setIcon(R.drawable.ic_not_favorite)
                    movie?.isFavorite = false
                    deleteMovieFromDatabase()
                } else if (movie?.isFavorite == false) {
                    item.setIcon(R.drawable.ic_favorite)
                    movie?.isFavorite = true
                    addMovieToDatabase()
                }
                return true
            }
            android.R.id.home -> {
                requireActivity().onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addMovieToDatabase() {
        movie?.let {
            favoriteMovieDao
                .saveFavoriteMovie(it)
                .threadSwitch()
                .subscribe {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.add_to_favorite),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun deleteMovieFromDatabase() {
        movie?.let {
            favoriteMovieDao
                .deleteFavoriteMovie(it)
                .threadSwitch()
                .subscribe {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.remove_from_favorite),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun setStartFavoriteIconColor(favoriteMovie: MovieDetails, menuItem: MenuItem?) {
        compositeDisposable.add(
            favoriteMovieDao
                .exists(favoriteMovie.id)
                .threadSwitch()
                .subscribe { exists ->
                    if (exists) {
                        movie?.isFavorite = true
                        menuItem?.setIcon(R.drawable.ic_favorite)
                    } else {
                        movie?.isFavorite = false
                        menuItem?.setIcon(R.drawable.ic_not_favorite)
                    }
                }
        )
    }

    enum class TableType {
        MOVIE,
        FAVORITE_MOVIE
    }

    companion object {
        @JvmStatic
        fun newInstance(movie: Movie) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_MOVIE_ID, movie.id)
                }
            }
    }

}