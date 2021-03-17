package ru.mikhailskiy.intensiv.data.mapper

interface ViewObjectMapper<VO, DTO> {

    fun toViewObject(dto: DTO): VO

    fun toViewObject(list: Collection<DTO>): List<VO> {
        val result = ArrayList<VO>()
        list.mapTo(result) { toViewObject(it) }
        return result
    }
}