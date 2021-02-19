package ru.mikhailskiy.intensiv.data.credits_model

data class CreditsResponse(
    val id: Int,
    val cast: List<ActorDto>
    // val crew: List<Worker>
)