package ru.mikhailskiy.intensiv.ui.movie_details

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_actor.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.Actor

class ActorItem(private val content: Actor): Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.actor_first_name.text = content.firstName
        viewHolder.actor_last_name.text = content.lastName

        Picasso.get().load(content.photoUrl).into(viewHolder.actor_photo)
    }

    override fun getLayout(): Int = R.layout.item_actor
}