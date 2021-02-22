package ru.mikhailskiy.intensiv

import android.widget.ImageView
import com.squareup.picasso.Picasso

private const val DEFAULT_IMAGE_SIZE = "w500/"

fun ImageView.loadImage(url: String, imageSize: String = DEFAULT_IMAGE_SIZE) {
    if (url.startsWith("http")) {
        Picasso.get()
            .load(url)
            .into(this)
    } else {
        Picasso.get()
            .load(BuildConfig.BASE_IMAGE_URL + imageSize + url)
            .into(this)
    }
}