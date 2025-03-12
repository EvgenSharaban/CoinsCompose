package com.example.coinscomp.data.local.room.entities

import com.example.coinscomp.domain.models.CoinDomain
import kotlin.collections.map

object CoinDataBaseMapper {

    fun CoinDomain.mapToLocalEntity(): CoinRoomEntity {
        return CoinRoomEntity(
            id = this.id,
            name = this.name,
            symbol = this.symbol,
            rank = this.rank,
            isNew = this.isNew,
            isActive = this.isActive,
            type = this.type,
            startedAt = this.startedAt,
            logo = this.logo,
            description = this.description,
            price = this.price
        )
    }

    fun List<CoinDomain>.mapToLocalEntityList(): List<CoinRoomEntity> = this.map { it.mapToLocalEntity() }

}