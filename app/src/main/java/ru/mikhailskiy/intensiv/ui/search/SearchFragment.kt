package ru.mikhailskiy.intensiv.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.movie_search_model.MovieSearch
import ru.mikhailskiy.intensiv.data.movie_search_model.MovieSearchDtoToVoConverter
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.ui.afterTextChanged
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment.Companion.ARG_SEARCH
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment() {

    val compositeDisposable = CompositeDisposable()
    val adapter by lazy { GroupAdapter<GroupieViewHolder>() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchTerm = requireArguments().getString(ARG_SEARCH)

        search_toolbar.search_edit_text.afterTextChanged { query ->
            if (query.toString().length >= 3) {
                compositeDisposable.add(Observable.create<String> { emitter ->
                    emitter.onNext(query.toString().trim())
                }.debounce(500, TimeUnit.MILLISECONDS)
                    .filter { it.isNotBlank() }
                    .distinctUntilChanged()
                    .switchMap { MovieApiClient.apiClient.getSearchMovies(query = it) }
                    .map {
                        it.results?.let { response ->
                            MovieSearchDtoToVoConverter().toViewObject(response)
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ movieSearchList ->
                        adapter.clear()
                        val moviesSearchItems = movieSearchList.map { movieSearch ->
                            SearchMovieItem(movieSearch) { openMovieDetails(movieSearch) }
                        }
                        movies_search_recycler_view.adapter =
                            adapter.apply { addAll(moviesSearchItems) }
                    }, { error ->
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.error) + error.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                )

            }
        }

        search_toolbar.setText(searchTerm)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

    private fun openMovieDetails(movie: MovieSearch) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putInt(FeedFragment.ARG_MOVIE_ID, movie.id)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }
}