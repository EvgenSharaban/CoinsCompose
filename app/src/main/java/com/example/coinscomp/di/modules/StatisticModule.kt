package com.example.coinscomp.di.modules

import com.example.coinscomp.data.repositories.StatisticRepositoryImpl
import com.example.coinscomp.domain.repositories.StatisticRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StatisticModule {

    @Binds
    abstract fun provideStatisticRepository(statisticRepositoryImpl: StatisticRepositoryImpl): StatisticRepository

}