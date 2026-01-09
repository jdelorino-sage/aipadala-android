package com.aipadala.android.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aipadala.android.core.database.dao.AlertsDao
import com.aipadala.android.core.database.dao.FavoritesDao
import com.aipadala.android.core.database.dao.RatesDao
import com.aipadala.android.core.database.entities.CachedRateEntity
import com.aipadala.android.core.database.entities.FavoriteCorridorEntity
import com.aipadala.android.core.database.entities.RateAlertEntity

@Database(
    entities = [
        CachedRateEntity::class,
        FavoriteCorridorEntity::class,
        RateAlertEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AIPadalaDatabase : RoomDatabase() {
    abstract fun ratesDao(): RatesDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun alertsDao(): AlertsDao
}
