package ru.mikhailskiy.intensiv.data.dto.movie

import com.google.gson.annotations.SerializedName

data class MovieResponseDto(
    var page: Int = 1,
    var results: List<MovieDto>? = null,
    var datesDto: DatesDto? = null,
    @SerializedName("total_pages")
    var totalPages: Int = 1,
    @SerializedName("total_results")
    var totalResults: Int = 1
)