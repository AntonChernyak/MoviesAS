package ru.mikhailskiy.intensiv.ui.tvshows

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.DtoToVoConverter
import ru.mikhailskiy.intensiv.data.movie_model.MovieResponse
import ru.mikhailskiy.intensiv.data.movie_model.MovieVO
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment.Companion.API_KEY
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment.Companion.ARG_MOVIE_ID

class TvShowsFragment : Fragment() {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.tv_shows_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MovieApiClient.apiClient.getPopularTvShowsList(API_KEY)
            .enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.check_net_connection),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful) {
                        val tvShowsVOList =
                            response.body()?.results?.map { DtoToVoConverter.movieDtoConverter(it) }
                        val tvShowsItems = tvShowsVOList?.map { tvShow ->
                            TvShowItem(tvShow) { openTvShowDetails(tvShow) }
                        }

                        tv_shows_recycler_view.adapter =
                            adapter.apply { tvShowsItems?.let { addAll(it) } }
                    } else Toast.makeText(
                        requireActivity(),
                        getString(R.string.error) + response.code(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun openTvShowDetails(movieVO: MovieVO) {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    companion object {
        @JvmStatic
        fun newInstance(movieVO: MovieVO) =
            TvShowsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_MOVIE_ID, movieVO.id)
                }
            }
    }
}