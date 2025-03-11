package com.example.coinscomp.ui.data.network.entities.mappers

import com.example.coinscomp.ui.data.network.entities.CoinEntity
import com.example.coinscomp.ui.domain.models.CoinDomain

object CoinsMapper : FromEntityToDomainMapper<CoinEntity, CoinDomain> {

    override fun mapToDomain(entity: CoinEntity?): CoinDomain {
        return CoinDomain(
            id = entity?.id ?: "",
            name = entity?.name ?: "",
            symbol = entity?.symbol ?: "",
            rank = entity?.rank ?: 0,
            isNew = entity?.isNew == true,
            isActive = entity?.isActive == true,
            type = entity?.type ?: "",
            logo = entity?.logo ?: "",
            description = entity?.description ?: "",
            startedAt = entity?.startedAt ?: "",
            price = entity?.quotes?.usd?.price ?: 0.0
        )
    }

    override fun mapToDomainList(list: List<CoinEntity?>?): List<CoinDomain> {
        return list.toDomainList()
    }

}