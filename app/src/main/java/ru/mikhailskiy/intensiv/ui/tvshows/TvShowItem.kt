package ru.mikhailskiy.intensiv.ui.tvshows

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_tv_show.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.movie_model.Movie
import ru.mikhailskiy.intensiv.loadImage

class TvShowItem(
    private val tvShow: Movie,
    private val onClick: (movie: Movie) -> Unit
): Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tv_show_title_text_view.text = tvShow.title
        viewHolder.tv_show_rating_rating_bar.rating = tvShow.rating
        viewHolder.tv_card_content.setOnClickListener {
            onClick.invoke(tvShow)
        }
        tvShow.backdpopUrl?.let { viewHolder.tv_show_preview_image_view.loadImage(it) }
    }

    override fun getLayout(): Int = R.layout.item_tv_show
}