package ru.mikhailskiy.intensiv.ui.watchlist

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.movie_feed_model.MovieFeed
import ru.mikhailskiy.intensiv.ui.loadImage

class MoviePreviewItem(
    private val content: MovieFeed,
    private val onClick: (movieFeed: MovieFeed) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_small

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.image_preview.setOnClickListener {
            onClick.invoke(content)
        }
        content.posterUrl?.let { viewHolder.image_preview.loadImage(it) }
    }
}