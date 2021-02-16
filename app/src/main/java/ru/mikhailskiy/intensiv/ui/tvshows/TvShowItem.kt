package ru.mikhailskiy.intensiv.ui.tvshows

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_tv_show.*
import kotlinx.android.synthetic.main.item_tv_show.tv_show_rating
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.Movie

class TvShowItem (
    private val tvShow: Movie,
    private val onClick: (movie: Movie) -> Unit
    ): Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tv_show_title.text = tvShow.title
        viewHolder.tv_show_rating.rating = tvShow.rating
        viewHolder.tv_card_content.setOnClickListener {
            onClick.invoke(tvShow)
        }

        Picasso.get()
            .load("https://i.ytimg.com/vi/Qgcj0QJDYEw/maxresdefault.jpg")
            .into(viewHolder.image_tv_show_preview)
    }

    override fun getLayout(): Int = R.layout.item_tv_show
}