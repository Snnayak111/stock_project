package com.example.stockproject.ui.wishlistDetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockproject.data.WishlistStockEntity
import com.example.stockproject.domain.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistDetailViewModel @Inject constructor(
    private val repository: WishlistRepository
) : ViewModel() {
    fun getStocks(wishlistId: Long): StateFlow<List<WishlistStockEntity>> =
        repository.getStocks(wishlistId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addStockToWishlist(stock: WishlistStockEntity) {
        viewModelScope.launch { repository.addStockToWishlist(stock) }
    }
    fun removeStockFromWishlist(symbol: String, wishlistId: Long) {
        viewModelScope.launch { repository.removeStockFromWishlist(symbol, wishlistId) }
    }
} 