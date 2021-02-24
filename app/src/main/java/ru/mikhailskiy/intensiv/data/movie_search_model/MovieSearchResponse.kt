package ru.mikhailskiy.intensiv.data.movie_search_model

import com.google.gson.annotations.SerializedName

class MovieSearchResponse(
    val page: Int = 1,
    val results: List<MovieSearchDto>? = null,
    @SerializedName("total_pages")
    var totalPages: Int = 1,
    @SerializedName("total_results")
    var totalResults: Int = 1
)