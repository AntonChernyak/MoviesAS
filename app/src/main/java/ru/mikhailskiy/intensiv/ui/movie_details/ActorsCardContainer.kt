package ru.mikhailskiy.intensiv.ui.movie_details

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.movie_details_fragment.*
import ru.mikhailskiy.intensiv.R


class ActorsCardContainer(
    private val actorsList: List<ActorItem>
): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.actors_recycler_view.adapter = GroupAdapter<GroupieViewHolder>().apply { addAll(actorsList) }
    }

    override fun getLayout(): Int = R.layout.movie_details_fragment
}