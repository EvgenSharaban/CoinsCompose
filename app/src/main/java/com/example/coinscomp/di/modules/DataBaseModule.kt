package com.example.coinscomp.di.modules

import android.content.Context
import com.example.coinscomp.data.local.room.CoinsDao
import com.example.coinscomp.data.local.room.CoinsDataBase
import com.example.coinscomp.data.local.room.NotesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext context: Context
    ): CoinsDataBase {
        return CoinsDataBase.getDataBase(context)
    }

    @Provides
    fun provideCoinsDao(dataBase: CoinsDataBase): CoinsDao {
        return dataBase.coinsDao()
    }

    @Provides
    fun provideNotesDao(dataBase: CoinsDataBase): NotesDao {
        return dataBase.notesDao()
    }

}