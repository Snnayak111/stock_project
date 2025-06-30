package com.example.stockproject.ui.wishlistListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockproject.data.WishlistEntity
import com.example.stockproject.domain.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistListViewModel @Inject constructor(
    private val repository: WishlistRepository
) : ViewModel() {
    val wishlists: StateFlow<List<WishlistEntity>> =
        repository.getWishlists().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createWishlist(name: String) {
        viewModelScope.launch { repository.createWishlist(name) }
    }
    fun deleteWishlist(wishlist: WishlistEntity) {
        viewModelScope.launch { repository.deleteWishlist(wishlist) }
    }
} 