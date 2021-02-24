package ru.mikhailskiy.intensiv.data.movie_feed_model

import com.google.gson.annotations.SerializedName

data class MovieFeedResponse(
    var page: Int = 1,
    var results: List<MovieFeedDto>? = null,
    var dates: Dates? = null,
    @SerializedName("total_pages")
    var totalPages: Int = 1,
    @SerializedName("total_results")
    var totalResults: Int = 1
)