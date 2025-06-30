package com.example.stockproject.domain

import com.example.stockproject.data.WishlistEntity
import com.example.stockproject.data.WishlistStockEntity
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getWishlists(): Flow<List<WishlistEntity>>
    suspend fun createWishlist(name: String): Long
    suspend fun deleteWishlist(wishlist: WishlistEntity)

    fun getStocks(wishlistId: Long): Flow<List<WishlistStockEntity>>
    suspend fun addStockToWishlist(stock: WishlistStockEntity)
    suspend fun removeStockFromWishlist(symbol: String, wishlistId: Long)
    fun isStockInWishlist(symbol: String, wishlistId: Long): Flow<Boolean>
} 