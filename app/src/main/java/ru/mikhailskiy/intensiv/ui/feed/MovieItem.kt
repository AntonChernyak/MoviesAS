package ru.mikhailskiy.intensiv.ui.feed

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.movie_feed_model.MovieFeed
import ru.mikhailskiy.intensiv.extensions.loadImage

class MovieItem(
    private val content: MovieFeed,
    private val onClick: (movieFeed: MovieFeed) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_with_text

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.details_movie_title_text_view.text = content.title
        viewHolder.movie_rating.rating = content.rating
        viewHolder.container_content.setOnClickListener {
            onClick.invoke(content)
        }
        content.posterUrl?.let { viewHolder.image_preview.loadImage(it) }
    }
}