package ru.mikhailskiy.intensiv

import android.widget.ImageView
import com.squareup.picasso.Picasso

private const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"
private const val IMAGE_SIZE = "w500/"

fun ImageView.loadImage(url: String) {
    if (url.startsWith("http")) Picasso.get().load(url).into(this)
    else Picasso.get().load(BASE_IMAGE_URL + IMAGE_SIZE + url).into(this)
}