package ru.mikhailskiy.intensiv.data.credits_model

import com.google.gson.annotations.SerializedName

data class ActorDto(
    val id: Int,
    val name: String,
    @SerializedName("profile_path")
    val profilePath: String?
)