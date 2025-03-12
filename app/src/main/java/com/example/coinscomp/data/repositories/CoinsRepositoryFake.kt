package com.example.coinscomp.data.repositories

import com.example.coinscomp.domain.models.CoinDomain
import com.example.coinscomp.domain.repositories.CoinsRepository
import javax.inject.Inject

class CoinsRepositoryFake @Inject constructor(

) : CoinsRepository {

    override suspend fun fetchCoinsFullEntity(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getCoinById(id: String): Result<CoinDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun getTickerById(id: String): Result<CoinDomain> {
        TODO("Not yet implemented")
    }

    override suspend fun getHiddenCoinsCount(): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun hideCoin(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getHidedCoinsIds(): Set<String> {
        TODO("Not yet implemented")
    }
}