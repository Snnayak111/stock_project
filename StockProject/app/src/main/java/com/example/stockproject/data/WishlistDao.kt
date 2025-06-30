package com.example.stockproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Insert
    suspend fun createWishlist(wishlist: WishlistEntity): Long

    @Delete
    suspend fun deleteWishlist(wishlist: WishlistEntity)

    @Query("SELECT * FROM wishlists")
    fun getAllWishlists(): Flow<List<WishlistEntity>>
} 