package ru.mikhailskiy.intensiv.data.movie_details_model

import com.google.gson.annotations.SerializedName

data class Company(
    val id: Int = 1,
    @SerializedName("logo_path")
    val logoPath: String? = "",
    val name: String = ""
)