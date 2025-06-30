package com.example.stockproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlists")
data class WishlistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
) 