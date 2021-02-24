package ru.mikhailskiy.intensiv.ui

import android.widget.ImageView
import com.squareup.picasso.Picasso
import ru.mikhailskiy.intensiv.BuildConfig
import ru.mikhailskiy.intensiv.R

private const val DEFAULT_IMAGE_SIZE = "w500/"

fun ImageView.loadImage(url: String?, imageSize: String = DEFAULT_IMAGE_SIZE) {
    when {
        url == null -> {
            Picasso.get()
                .load(R.drawable.place_holder)
                .placeholder(R.drawable.place_holder)
                .into(this)
        }
        url.startsWith("http") -> {
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.place_holder)
                .into(this)
        }
        else -> {
            Picasso.get()
                .load(BuildConfig.BASE_IMAGE_URL + imageSize + url)
                .placeholder(R.drawable.place_holder)
                .into(this)
        }
    }
}