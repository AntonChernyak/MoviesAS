package ru.mikhailskiy.intensiv.ui.movie_details

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
import ru.mikhailskiy.intensiv.data.credits_model.ActorDtoToVoConverter
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetails
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetailsDtoToVoConverter
import ru.mikhailskiy.intensiv.data.movie_feed_model.Movie
import ru.mikhailskiy.intensiv.database.MovieDatabase
import ru.mikhailskiy.intensiv.extensions.addLoader
import ru.mikhailskiy.intensiv.extensions.loadImage
import ru.mikhailskiy.intensiv.extensions.threadSwitch
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.providers.RepositoryAccess
import ru.mikhailskiy.intensiv.providers.SingleCacheProvider
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment.Companion.ARG_MOVIE_ID

class MovieDetailsFragment : Fragment(), SingleCacheProvider<MovieDetails> {

    private val compositeDisposable = CompositeDisposable()
    private var movieVoId: Int = 1
    private var movie: MovieDetails? = null
    private var menu: Menu? = null
    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
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
                MovieDetailsDtoToVoConverter().toViewObject(it)
            }
    }

    override fun createOfflineSingle(): Single<MovieDetails> {
        return MovieDatabase
            .get(requireActivity())
            .getFavoriteMovieDao()
            .getFavoriteMovieById(movieVoId)
    }

    private fun getMovieById() {
        compositeDisposable.add(
            this@MovieDetailsFragment.getSingle(RepositoryAccess.OFFLINE_FIRST)
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
                .map { ActorDtoToVoConverter().toViewObject(it.cast) }
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
            MovieDatabase
                .get(requireActivity())
                .getFavoriteMovieDao()
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
            MovieDatabase
                .get(requireActivity())
                .getFavoriteMovieDao()
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
        compositeDisposable.add(MovieDatabase
            .get(requireActivity())
            .getFavoriteMovieDao()
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