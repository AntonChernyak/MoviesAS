package ru.mikhailskiy.intensiv.data.dto.movies_details_dto

import com.google.gson.annotations.SerializedName

data class CompanyDto(
    val id: Int = 1,
    @SerializedName("logo_path")
    val logoPath: String? = "",
    val name: String = ""
)