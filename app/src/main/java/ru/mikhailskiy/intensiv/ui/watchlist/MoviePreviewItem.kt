package ru.mikhailskiy.intensiv.ui.watchlist

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.movie_model.MovieVO
import ru.mikhailskiy.intensiv.loadImage

class MoviePreviewItem(
    private val content: MovieVO,
    private val onClick: (movieVO: MovieVO) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_small

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.image_preview.setOnClickListener {
            onClick.invoke(content)
        }
        content.posterUrl?.let { viewHolder.image_preview.loadImage(it) }
    }
}