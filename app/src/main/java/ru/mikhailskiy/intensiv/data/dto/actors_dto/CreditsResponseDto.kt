package ru.mikhailskiy.intensiv.data.dto.actors_dto

data class CreditsResponseDto(
    val id: Int,
    val cast: List<ActorDto>
    // val crew: List<Worker>
)