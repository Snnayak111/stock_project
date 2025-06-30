package com.example.stockproject.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WishlistEntity::class, WishlistStockEntity::class],
    exportSchema = false,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wishlistDao(): WishlistDao
    abstract fun wishlistStockDao(): WishlistStockDao
} 