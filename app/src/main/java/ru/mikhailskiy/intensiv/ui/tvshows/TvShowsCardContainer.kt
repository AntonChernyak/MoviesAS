package ru.mikhailskiy.intensiv.ui.tvshows

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.mikhailskiy.intensiv.R

class TvShowsCardContainer(
    private val items: List<TvShowItem>
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.tv_shows_recycler_view.adapter =
            GroupAdapter<GroupieViewHolder>().apply { addAll(items) }
    }

    override fun getLayout(): Int = R.layout.tv_shows_fragment
}