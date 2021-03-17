package ru.mikhailskiy.intensiv.data.extension

import ru.mikhailskiy.intensiv.data.dto.movie.MovieResponseDto
import ru.mikhailskiy.intensiv.data.mapper.MovieDtoMapper
import ru.mikhailskiy.intensiv.data.vo.Movie

fun Double.voteAverageToRating(): Float = this.div(2).toFloat()

fun MovieResponseDto.toMoviesList(type: String = ""): List<Movie>? = this.results?.let { dtoList ->
    dtoList.map {
        return@map MovieDtoMapper().toViewObject(it).apply { this.type = type }
    }
}