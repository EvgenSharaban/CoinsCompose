package com.example.coinscomp.presentation.coins.models

import com.example.coinscomp.data.local.room.entities.CoinRoomEntity

object CoinUiModelMapper {

    fun CoinRoomEntity.mapToUiModel(): ModelCoinsCustomView {
        return ModelCoinsCustomView(
            id = this.id,
            rank = this.rank,
            name = this.name,
            description = this.description,
            creationDate = this.startedAt,
            logo = this.logo,
            shortName = this.symbol,
            type = this.type,
            isActive = this.isActive,
            price = this.price
        )
    }
}