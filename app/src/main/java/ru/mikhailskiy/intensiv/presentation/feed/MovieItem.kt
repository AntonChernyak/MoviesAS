package ru.mikhailskiy.intensiv.presentation.feed

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.extensions.loadImage
import ru.mikhailskiy.intensiv.data.vo.Movie

class MovieItem(
    private val content: Movie,
    private val onClick: (movie: Movie) -> Unit
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