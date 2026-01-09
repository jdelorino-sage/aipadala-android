package com.aipadala.android.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aipadala.android.core.database.entities.FavoriteCorridorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorite_corridors ORDER BY position ASC")
    fun getAllFavorites(): Flow<List<FavoriteCorridorEntity>>

    @Query("SELECT * FROM favorite_corridors ORDER BY position ASC")
    suspend fun getAllFavoritesSync(): List<FavoriteCorridorEntity>

    @Query("SELECT * FROM favorite_corridors WHERE id = :id")
    suspend fun getFavoriteById(id: String): FavoriteCorridorEntity?

    @Query("SELECT COUNT(*) FROM favorite_corridors")
    suspend fun getFavoritesCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteCorridorEntity)

    @Update
    suspend fun updateFavorite(favorite: FavoriteCorridorEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteCorridorEntity)

    @Query("DELETE FROM favorite_corridors WHERE id = :id")
    suspend fun deleteFavoriteById(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_corridors WHERE fromCurrency = :from AND toCurrency = :to)")
    suspend fun isFavorite(from: String, to: String): Boolean

    @Query("UPDATE favorite_corridors SET position = :newPosition WHERE id = :id")
    suspend fun updatePosition(id: String, newPosition: Int)
}
