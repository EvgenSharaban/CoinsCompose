package com.example.coinscomp.domain.repositories

interface StatisticRepository {

    suspend fun getAmountOfDaysAppUsing(): Result<Int>
    suspend fun getTotalItemsCount(): Result<Int>

}