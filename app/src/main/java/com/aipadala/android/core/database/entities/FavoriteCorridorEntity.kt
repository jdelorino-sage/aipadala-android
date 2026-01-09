package com.aipadala.android.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_corridors")
data class FavoriteCorridorEntity(
    @PrimaryKey
    val id: String,
    val fromCurrency: String,
    val toCurrency: String,
    val displayName: String,
    val defaultAmount: Double,
    val position: Int,
    val createdAt: Long = System.currentTimeMillis()
)
