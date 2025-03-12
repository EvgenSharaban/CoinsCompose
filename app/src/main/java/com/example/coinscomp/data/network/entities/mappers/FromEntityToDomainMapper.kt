package com.example.coinscomp.data.network.entities.mappers

interface FromEntityToDomainMapper<Entity, Domain> {

    fun mapToDomain(entity: Entity?): Domain
    fun mapToDomainList(list: List<Entity?>?): List<Domain>


    fun List<Entity?>?.toDomainList(): List<Domain> {
        return safeListResult(this).map {
            mapToDomain(it)
        }
    }

    private fun <T> safeListResult(list: List<T?>?): List<T> {
        if (list.isNullOrEmpty())
            return emptyList()
        return list.filterNotNull()
    }

}