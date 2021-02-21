package ru.mikhailskiy.intensiv.data

fun Double.voteAverageToRating(): Float = this.div(2).toFloat()