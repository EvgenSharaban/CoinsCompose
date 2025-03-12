package com.example.coinscomp.data.repositories

import android.util.Log
import androidx.room.withTransaction
import com.example.coinscomp.core.networking.safeApiCall
import com.example.coinscomp.core.networking.safeApiCallList
import com.example.coinscomp.core.networking.toDomain
import com.example.coinscomp.core.networking.toDomainList
import com.example.coinscomp.core.other.FAILURE_VALUE
import com.example.coinscomp.core.other.TAG
import com.example.coinscomp.core.other.roundTo
import com.example.coinscomp.data.local.datastore.CoinsDataStore
import com.example.coinscomp.data.local.room.CoinsDao
import com.example.coinscomp.data.local.room.CoinsDataBase
import com.example.coinscomp.data.local.room.entities.CoinDataBaseMapper.mapToLocalEntityList
import com.example.coinscomp.data.local.room.entities.CoinRoomEntity
import com.example.coinscomp.data.network.ApiService
import com.example.coinscomp.data.network.entities.mappers.CoinDomainMapper
import com.example.coinscomp.data.repositories.CoinsRepositoryConst.FILTERING_TYPE
import com.example.coinscomp.domain.models.CoinDomain
import com.example.coinscomp.domain.repositories.CoinsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CoinsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dataBase: CoinsDataBase,
    private val coinsDao: CoinsDao,
    private val dataStore: CoinsDataStore
) : CoinsRepository {

    private val allCoins: Flow<List<CoinRoomEntity>> = coinsDao.getAllCoins()
    private val hiddenCoins: Flow<Set<String>> = dataStore.getHiddenCoinsFlow()

    override val coins: Flow<List<CoinRoomEntity>> = combine(allCoins, hiddenCoins) { allCoins, hiddenCoinsIds ->
        allCoins.filter { !hiddenCoinsIds.contains(it.id) }
    }

    override suspend fun fetchCoinsFullEntity(): Result<Unit> {
        return safeApiCallList {
            apiService.getCoins()
        }
            .toDomainList(CoinDomainMapper)
            .mapCatching { coinsList ->
                getDetailInfoByList(coinsList)
            }
            .onSuccess { coinsList ->
                insertCoinsToDB(coinsList)
                Log.d(TAG, "getCoins: success, size = ${coinsList.size}")
            }
            .onFailure { error ->
                Log.d(TAG, "getCoins(): failure, \nerror = $error")
            }
            .map { } // need for Result<Unit>
    }

    override suspend fun getCoinById(id: String): Result<CoinDomain> {
        return safeApiCall {
            apiService.getCoinById(id)
        }.toDomain(CoinDomainMapper)
    }

    override suspend fun getTickerById(id: String): Result<CoinDomain> {
        return safeApiCall {
            apiService.getTickerById(id)
        }.toDomain(CoinDomainMapper)
    }

    override suspend fun getHiddenCoinsCount(): Result<Int> {
        return try {
            val list = dataStore.getHidedCoinsIds()
            Result.success(list.size)
        } catch (e: Throwable) {
            Result.failure(Exception(FAILURE_VALUE, e))
        }
    }

    override suspend fun hideCoin(id: String) {
        dataStore.addHidedCoinId(id)
        Log.d(TAG, "onCleared: hidden item = $id")
    }

    override suspend fun getHidedCoinsIds(): Set<String> {
        return dataStore.getHidedCoinsIds()
    }

    private suspend fun getDetailInfoByList(coins: List<CoinDomain>): List<CoinDomain> = coroutineScope {
        Log.d(TAG, "getCoins: time start")
        val list = coins
            .filter { it.rank > 0 && it.isActive && it.type == FILTERING_TYPE }
            .sortedBy { it.rank }
            .take(MAX_COUNT_ITEMS)
            .map { coin ->
                async {
                    getDetailInfo(coin)
                }
            }
            .awaitAll()
            .mapNotNull { result ->
                result.getOrElse { error ->
                    Log.e(TAG, "Error fetching coin by id: $error")
                    null
                }
            }
        Log.d(TAG, "getCoins: time end")
        list
    }

    private suspend fun getDetailInfo(coin: CoinDomain): Result<CoinDomain> {
        return getCoinById(coin.id)
            .mapCatching { fetchedCoin ->
                val price = getTickerById(fetchedCoin.id).getOrThrow().price
                val roundedPrice = price.roundTo(NUMBERS_OF_DIGITS_PRICE_AFTER_POINT)
                fetchedCoin.copy(price = roundedPrice)
            }
    }

    private suspend fun insertCoinsToDB(list: List<CoinDomain>) {
        try {
            dataBase.withTransaction {
                coinsDao.deleteAllCoins()
                coinsDao.insertAllCoins(list.mapToLocalEntityList())
            }
            Log.d(TAG, "insertCoinsToDB: success")
        } catch (e: Throwable) {
            Log.d(TAG, "insertCoinsToDB: failed, \nerror = $e")
        }
    }

    companion object {
        private const val NUMBERS_OF_DIGITS_PRICE_AFTER_POINT = 2
        private const val MAX_COUNT_ITEMS = 10
    }

}