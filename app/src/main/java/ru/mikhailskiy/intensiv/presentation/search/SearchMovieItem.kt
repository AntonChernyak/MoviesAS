package ru.mikhailskiy.intensiv.presentation.search

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_search_movie.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.extension.loadImage
import ru.mikhailskiy.intensiv.data.vo.Movie

class SearchMovieItem(
    private val movieSearch: Movie,
    private val onClick: (movieSearch: Movie) -> Unit
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.movieSearchRatingBar.rating = movieSearch.rating
        viewHolder.movieTitleSearchTextView.text = movieSearch.title
        viewHolder.movieDescriptionSearchTextView.text = movieSearch.description
        viewHolder.movieReleaseDateSearchTextView.text = movieSearch.releaseYear
        viewHolder.searchPosterImageView.loadImage(movieSearch.posterUrl)

        viewHolder.searchFragmentContent.setOnClickListener {
            onClick.invoke(movieSearch)
        }
    }

    override fun getLayout(): Int = R.layout.item_search_movie
}