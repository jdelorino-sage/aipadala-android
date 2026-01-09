package com.aipadala.android.core.di

import android.content.Context
import com.aipadala.android.core.network.NetworkMonitor
import com.aipadala.android.core.util.AffiliateLinkHandler
import com.aipadala.android.core.util.AnalyticsTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitor = NetworkMonitor(context)

    @Provides
    @Singleton
    fun provideAnalyticsTracker(
        @ApplicationContext context: Context
    ): AnalyticsTracker = AnalyticsTracker(context)

    @Provides
    @Singleton
    fun provideAffiliateLinkHandler(
        @ApplicationContext context: Context,
        analyticsTracker: AnalyticsTracker
    ): AffiliateLinkHandler = AffiliateLinkHandler(context, analyticsTracker)
}
