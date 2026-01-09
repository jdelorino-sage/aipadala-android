package com.aipadala.android.core.di

import com.aipadala.android.data.repository.AlertsRepositoryImpl
import com.aipadala.android.data.repository.FavoritesRepositoryImpl
import com.aipadala.android.data.repository.RatesRepositoryImpl
import com.aipadala.android.domain.repository.AlertsRepository
import com.aipadala.android.domain.repository.FavoritesRepository
import com.aipadala.android.domain.repository.RatesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRatesRepository(
        impl: RatesRepositoryImpl
    ): RatesRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        impl: FavoritesRepositoryImpl
    ): FavoritesRepository

    @Binds
    @Singleton
    abstract fun bindAlertsRepository(
        impl: AlertsRepositoryImpl
    ): AlertsRepository
}
