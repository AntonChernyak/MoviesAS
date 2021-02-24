package ru.mikhailskiy.intensiv.data.credits_model

import ru.mikhailskiy.intensiv.data.ViewObjectMapper

class ActorDtoToVoConverter : ViewObjectMapper<Actor, ActorDto> {
    override fun toViewObject(dto: ActorDto): Actor {
        return Actor(
            id = dto.id,
            firstName = dto.name.substringBefore(" "),
            lastName = dto.name.substringAfter(" "),
            posterPath = dto.profilePath
        )
    }
}