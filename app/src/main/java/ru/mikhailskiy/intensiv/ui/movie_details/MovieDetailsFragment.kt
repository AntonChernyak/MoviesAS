package ru.mikhailskiy.intensiv.ui.movie_details

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.movie_details_fragment.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.movie_model.MovieVO
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment.Companion.ARG_MOVIE

class MovieDetailsFragment : Fragment() {

    private var movieVO: MovieVO? = null
    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieVO = it.getParcelable(ARG_MOVIE)
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
/*
        details_movie_title_text_view.text = movieVO?.title
        details_movie_description_text_view.text = movieVO?.description
        year_text_view.text = movieVO?.year
        studio_text_view.text = movieVO?.studio
        genre_text_view.text = movieVO?.genre?.joinToString()

        movieVO?.posterUrl?.let { details_poster_image_view.loadImage(it) }

        val actorsItems = movieVO?.actors?.map { ActorItem(it) }?.toList()

        actors_recycler_view.adapter = adapter.apply { actorsItems?.let { addAll(it) } }*/
        actors_recycler_view.isNestedScrollingEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

/*    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_to_favorite -> {
                movieVO?.isFavorite = if (movieVO?.isFavorite!!) {
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
    }*/

    companion object {

        @JvmStatic
        fun newInstance(movieVO: MovieVO) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_MOVIE, movieVO)
                }
            }
    }

}