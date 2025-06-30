package com.example.stockproject.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "wishlist_stocks",
    foreignKeys = [ForeignKey(
        entity = WishlistEntity::class,
        parentColumns = ["id"],
        childColumns = ["wishlistId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("wishlistId")]
)
data class WishlistStockEntity(
    @PrimaryKey val symbol: String,
    val name: String,
    val exchange: String,
    val currency: String,
    val wishlistId: Long
) 