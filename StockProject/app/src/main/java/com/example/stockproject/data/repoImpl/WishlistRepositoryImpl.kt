package com.example.stockproject.data.repoImpl

import com.example.stockproject.data.WishlistDao
import com.example.stockproject.data.WishlistEntity
import com.example.stockproject.data.WishlistStockDao
import com.example.stockproject.data.WishlistStockEntity
import com.example.stockproject.domain.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor(
    private val wishlistDao: WishlistDao,
    private val stockDao: WishlistStockDao
) : WishlistRepository {

    override fun getWishlists() = wishlistDao.getAllWishlists()

    override suspend fun createWishlist(name: String): Long =
        wishlistDao.createWishlist(WishlistEntity(name = name))

    override suspend fun deleteWishlist(wishlist: WishlistEntity) =
        wishlistDao.deleteWishlist(wishlist)

    override fun getStocks(wishlistId: Long) =
        stockDao.getStocksForWishlist(wishlistId)

    override suspend fun addStockToWishlist(stock: WishlistStockEntity) =
        stockDao.addStock(stock)

    override suspend fun removeStockFromWishlist(symbol: String, wishlistId: Long) =
        stockDao.removeStock(symbol, wishlistId)

    override fun isStockInWishlist(symbol: String, wishlistId: Long) =
        stockDao.isInWishlist(symbol, wishlistId)
} 