package ru.mikhailskiy.intensiv.data.dto.actor

data class CreditsResponseDto(
    val id: Int,
    val cast: List<ActorDto>
    // val crew: List<Worker>
)