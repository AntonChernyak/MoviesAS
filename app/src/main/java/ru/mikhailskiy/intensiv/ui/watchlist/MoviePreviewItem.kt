package ru.mikhailskiy.intensiv.ui.watchlist

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.movie_details_model.MovieDetails
import ru.mikhailskiy.intensiv.extensions.loadImage

class MoviePreviewItem(
    private val content: MovieDetails,
    private val onClick: (movie: MovieDetails) -> Unit,
    private val favoriteAdapterListener: FavoriteAdapterListener
) : Item() {

    override fun getLayout() = R.layout.item_small

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.image_preview.setOnClickListener {
            onClick.invoke(content)
        }
        viewHolder.image_preview.setOnLongClickListener {
            favoriteAdapterListener.onLongClickItem(position)
            return@setOnLongClickListener true
        }
        viewHolder.image_preview.loadImage(content.posterPath)
    }

    fun interface FavoriteAdapterListener {
        fun onLongClickItem(position: Int)
    }
}