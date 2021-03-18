package ru.mikhailskiy.intensiv.data.mapper

import ru.mikhailskiy.intensiv.data.dto.actor.ActorDto
import ru.mikhailskiy.intensiv.data.vo.Actor

class ActorDtoMapper : ViewObjectMapper<Actor, ActorDto> {
    override fun toViewObject(dto: ActorDto): Actor {
        return Actor(
            id = dto.id,
            firstName = dto.name.substringBefore(" "),
            lastName = dto.name.substringAfter(" "),
            posterPath = dto.profilePath
        )
    }
}