package ru.mikhailskiy.intensiv.extensions

fun Double.voteAverageToRating(): Float = this.div(2).toFloat()