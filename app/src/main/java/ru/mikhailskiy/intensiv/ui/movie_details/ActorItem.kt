package ru.mikhailskiy.intensiv.ui.movie_details

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_actor.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.credits_model.Actor
import ru.mikhailskiy.intensiv.extensions.loadImage

class ActorItem(private val content: Actor) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.actor_first_name_text_view.text = content.firstName
        viewHolder.actor_last_name_text_view.text = content.lastName
        content.posterPath?.let { viewHolder.actor_photo_image_view.loadImage(it) }
    }

    override fun getLayout(): Int = R.layout.item_actor
}