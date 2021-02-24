package ru.mikhailskiy.intensiv.data.movie_feed_model

import ru.mikhailskiy.intensiv.data.ViewObjectMapper
import ru.mikhailskiy.intensiv.data.voteAverageToRating

class MovieFeedDtoToVoConverter : ViewObjectMapper<MovieFeed, MovieFeedDto> {
    override fun toViewObject(feedDto: MovieFeedDto): MovieFeed {
        return MovieFeed(
            id = feedDto.id,
            title = feedDto.title,
            rating = feedDto.voteAverage.voteAverageToRating(),
            posterUrl = feedDto.posterPath,
            backdpopUrl = feedDto.backdropPath,
            voteCount = feedDto.voteCount
        )
    }
}