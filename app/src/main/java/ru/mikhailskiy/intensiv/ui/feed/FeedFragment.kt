package ru.mikhailskiy.intensiv.ui.feed

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mikhailskiy.intensiv.BuildConfig
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.DtoToVoConverter
import ru.mikhailskiy.intensiv.data.movie_model.MovieResponse
import ru.mikhailskiy.intensiv.data.movie_model.MovieVO
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.ui.afterTextChanged
import timber.log.Timber

class FeedFragment : Fragment() {

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
        movies_recycler_view.layoutManager = LinearLayoutManager(context)
        movies_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        search_toolbar.search_edit_text.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > 3) {
                openSearch(it.toString())
            }
        }

        getDataFromNet(
            MovieApiClient.apiClient.getTopRatedMovies(API_KEY),
            R.string.top_rated,
            2000
        )
        getDataFromNet(MovieApiClient.apiClient.getPopularMovies(API_KEY), R.string.popular, 500)
        getDataFromNet(
            MovieApiClient.apiClient.getNowPlayingMovie(API_KEY),
            R.string.now_playing,
            0
        )
        getDataFromNet(MovieApiClient.apiClient.getUpcomingMovies(API_KEY), R.string.upcoming, 0)
    }

    private fun getDataFromNet(apiFunction: Call<MovieResponse>, label: Int, voteCount: Int) {
        apiFunction.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.check_net_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val moviesVoList = response.body()?.results?.map {
                        DtoToVoConverter.movieDtoConverter(it)
                    }

                    val moviesList = listOf(moviesVoList?.filter {
                        it.voteCount >= voteCount
                    }?.map { movieVo ->
                        MovieItem(movieVo) {
                            openMovieDetails(movieVo)
                        }
                    }?.let { MainCardContainer(label, it) })

                    adapter.apply { addAll(moviesList) }
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error) + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        })
    }

    private fun openMovieDetails(movieVO: MovieVO) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putInt(ARG_MOVIE_ID, movieVO.id)
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
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    companion object {
        const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
        const val ARG_MOVIE_ID = "arg movie id"
        const val ARG_SEARCH = "arg search"
    }
}