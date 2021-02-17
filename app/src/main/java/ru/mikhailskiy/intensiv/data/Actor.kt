package ru.mikhailskiy.intensiv.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Actor(
    val firstName: String,
    val lastName: String,
    val photoUrl: String
) : Parcelable