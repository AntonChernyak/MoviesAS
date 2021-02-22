package ru.mikhailskiy.intensiv.ui.movie_details

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.movie_details_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.credits_model.ActorDtoToVoConverter
import ru.mikhailskiy.intensiv.data.credits_model.CreditsResponse
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetails
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetailsDto
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetailsDtoToVoConverter
import ru.mikhailskiy.intensiv.data.movie_model.Movie
import ru.mikhailskiy.intensiv.loadImage
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment.Companion.ARG_MOVIE_ID

class MovieDetailsFragment : Fragment() {

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
        MovieApiClient.apiClient.getMovieDetails(movieVoId)
            .enqueue(object : Callback<MovieDetailsDto> {
                override fun onFailure(call: Call<MovieDetailsDto>, t: Throwable) {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.check_net_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<MovieDetailsDto>,
                    response: Response<MovieDetailsDto>
                ) {
                    if (response.isSuccessful) {
                        movie =
                            response.body()?.let { MovieDetailsDtoToVoConverter().toViewObject(it) }

                        details_movie_title_text_view.text = movie?.title
                        details_movie_description_text_view.text = movie?.overview
                        year_text_view.text = movie?.year
                        studio_text_view.text = movie?.productionCompanies
                        genre_text_view.text = movie?.genres
                        movie?.rating?.let { details_movie_rating_bar.rating = it }
                        movie?.posterPath?.let { details_poster_image_view.loadImage(it) }
                    } else Toast.makeText(
                        requireActivity(),
                        getString(R.string.error) + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun addActorsToRecyclerView() {
        MovieApiClient.apiClient.getMovieCredits(movieVoId)
            .enqueue(object : Callback<CreditsResponse> {
                override fun onFailure(call: Call<CreditsResponse>, t: Throwable) {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.check_net_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<CreditsResponse>,
                    response: Response<CreditsResponse>
                ) {
                    if (response.isSuccessful) {
                        val actors = response.body()?.cast?.map {
                            ActorItem(
                                ActorDtoToVoConverter().toViewObject(it)
                            )
                        }
                        actors_recycler_view.adapter = adapter.apply { actors?.let { addAll(it) } }
                    } else Toast.makeText(
                        requireActivity(),
                        getString(R.string.error) + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
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
        fun newInstance(movie: Movie) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_MOVIE_ID, movie.id)
                }
            }
    }

}