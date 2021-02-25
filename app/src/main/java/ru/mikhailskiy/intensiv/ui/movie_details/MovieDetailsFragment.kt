package ru.mikhailskiy.intensiv.ui.movie_details

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.movie_details_fragment.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.credits_model.ActorDtoToVoConverter
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetails
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetailsDtoToVoConverter
import ru.mikhailskiy.intensiv.data.movie_feed_model.MovieFeed
import ru.mikhailskiy.intensiv.extensions.loadImage
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment.Companion.ARG_MOVIE_ID

class MovieDetailsFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()
    private var movieVoId: Int = 1
    private var movie: MovieDetails? = null
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireActivity() as AppCompatActivity?)?.setSupportActionBar(details_toolbar)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.title = ""
        setHasOptionsMenu(true)

        getMovieById()
        addActorsToRecyclerView()
        actors_recycler_view.isNestedScrollingEnabled = false
    }

    private fun getMovieById() {
        compositeDisposable.add(
            MovieApiClient.apiClient.getMovieDetails(movieVoId)
                .subscribeOn(Schedulers.io())
                .map { MovieDetailsDtoToVoConverter().toViewObject(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ movieDetails ->
                    movie = movieDetails

                    details_movie_title_text_view.text = movie?.title
                    details_movie_description_text_view.text = movie?.overview
                    year_text_view.text = movie?.year
                    studio_text_view.text = movie?.productionCompanies
                    genre_text_view.text = movie?.genres
                    movie?.rating?.let { details_movie_rating_bar.rating = it }
                    movie?.posterPath?.let { details_poster_image_view.loadImage(it) }
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
                .subscribeOn(Schedulers.io())
                .map { ActorDtoToVoConverter().toViewObject(it.cast) }
                .observeOn(AndroidSchedulers.mainThread())
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
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_to_favorite -> {
                movie?.isFavorite = if (movie?.isFavorite!!) {
                    item.setIcon(R.drawable.ic_not_favorite)
                    false
                } else {
                    item.setIcon(R.drawable.ic_favorite)
                    true
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

    companion object {

        @JvmStatic
        fun newInstance(movieFeed: MovieFeed) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_MOVIE_ID, movieFeed.id)
                }
            }
    }

}