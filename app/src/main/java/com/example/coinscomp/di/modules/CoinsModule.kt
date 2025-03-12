package com.example.coinscomp.di.modules

import com.example.coinscomp.data.repositories.CoinsRepositoryFake
import com.example.coinscomp.data.repositories.CoinsRepositoryImpl
import com.example.coinscomp.domain.repositories.CoinsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoinsModule {

    @Binds
    abstract fun provideCoinsRepository(coinsRepositoryImpl: CoinsRepositoryFake): CoinsRepository
}