package com.example.coinscomp.domain.repositories

import com.example.coinscomp.data.local.room.entities.CoinRoomEntity
import com.example.coinscomp.domain.models.CoinDomain
import kotlinx.coroutines.flow.Flow

interface CoinsRepository {

    val coins: Flow<List<CoinRoomEntity>>

    suspend fun fetchCoinsFullEntity(): Result<Unit>

    suspend fun getCoinById(id: String): Result<CoinDomain>

    suspend fun getTickerById(id: String): Result<CoinDomain>

    suspend fun getHiddenCoinsCount(): Result<Int>

    suspend fun hideCoin(id: String)

    suspend fun getHidedCoinsIds(): Set<String>

}