package ru.mikhailskiy.intensiv.presentation.watchlist

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.extensions.loadImage
import ru.mikhailskiy.intensiv.data.vo.MovieDetails

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