package ru.mikhailskiy.intensiv.ui.movie_details

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.movie_details_fragment.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.Actor

private const val ARG_TITLE = "title"
private const val ARG_DESCRIPTION = "description"
private const val ARG_STUDIO = "studio"
private const val ARG_GENRE = "genre"
private const val ARG_YEAR = "year"
private const val ARG_POSTER_URL = "posterUrl"
private const val ARG_ACTORS = "actors"

class MovieDetailsFragment : Fragment() {

    private var title: String? = null
    private var description: String? = null
    private var year: String? = null
    private var studio: String? = null
    private var genres: String? = null
    private var posterUrl: String? = null
    private var actorsList: List<Actor> = ArrayList()

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            description = it.getString(ARG_DESCRIPTION)
            year = it.getString(ARG_YEAR)
            studio = it.getString(ARG_STUDIO)
            genres = it.getString(ARG_GENRE)
            posterUrl = it.getString(ARG_POSTER_URL)
            actorsList = it.getParcelableArrayList(ARG_ACTORS)!!
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
        (requireActivity() as AppCompatActivity?)?.setSupportActionBar(toolbar_details)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity?)?.supportActionBar?.title = ""
        setHasOptionsMenu(true)

        details_movie_title.text = title
        details_movie_description.text = description
        textView_year.text = year
        textView_studio.text = studio
        textView_genre.text = genres

        Picasso.get().load(posterUrl).into(image_poster)

        actors_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        actors_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        val actorsItems = listOf(
            ActorsCardContainer(
                actorsList.map {
                    Log.d("TAGGGG", it.toString())
                    ActorItem(it)
                }.toList()
            )
        )
        Log.d("TAGGGG", "actorsItems = ${actorsItems}")
        actors_recycler_view.adapter = adapter.apply { addAll(actorsItems) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_to_favorite -> {
                item.setIcon(R.drawable.ic_favorite)
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
        fun newInstance(param1: String, param2: String) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, param1)
                    putString(ARG_DESCRIPTION, param2)
                }
            }
    }

}