package com.aipadala.android.core.di

import android.content.Context
import androidx.room.Room
import com.aipadala.android.core.database.AIPadalaDatabase
import com.aipadala.android.core.database.dao.AlertsDao
import com.aipadala.android.core.database.dao.FavoritesDao
import com.aipadala.android.core.database.dao.RatesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AIPadalaDatabase = Room.databaseBuilder(
        context,
        AIPadalaDatabase::class.java,
        "aipadala_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideRatesDao(database: AIPadalaDatabase): RatesDao = database.ratesDao()

    @Provides
    fun provideFavoritesDao(database: AIPadalaDatabase): FavoritesDao = database.favoritesDao()

    @Provides
    fun provideAlertsDao(database: AIPadalaDatabase): AlertsDao = database.alertsDao()
}
