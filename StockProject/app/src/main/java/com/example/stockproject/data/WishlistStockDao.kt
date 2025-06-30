package com.example.stockproject.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistStockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStock(stock: WishlistStockEntity)

    @Query("DELETE FROM wishlist_stocks WHERE symbol = :symbol AND wishlistId = :wishlistId")
    suspend fun removeStock(symbol: String, wishlistId: Long)

    @Query("SELECT * FROM wishlist_stocks WHERE wishlistId = :wishlistId")
    fun getStocksForWishlist(wishlistId: Long): Flow<List<WishlistStockEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_stocks WHERE symbol = :symbol AND wishlistId = :wishlistId)")
    fun isInWishlist(symbol: String, wishlistId: Long): Flow<Boolean>
} 