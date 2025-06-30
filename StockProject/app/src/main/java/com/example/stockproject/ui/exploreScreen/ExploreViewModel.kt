package com.example.stockproject.ui.exploreScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockproject.data.repoImpl.RepoImpl
import com.example.stockproject.data.reponseModel.TopStocks
import com.example.stockproject.data.reponseModel.TopStocksResponseModel
import com.example.stockproject.domain.WishlistRepository
import com.example.stockproject.domain.uiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: RepoImpl
) : ViewModel() {

    private val _gainers = MutableStateFlow<uiResult<List<TopStocks>>>(uiResult.Loading)
    val gainers: StateFlow<uiResult<List<TopStocks>>> = _gainers.asStateFlow()

    private val _losers = MutableStateFlow<uiResult<List<TopStocks>>>(uiResult.Loading)
    val losers: StateFlow<uiResult<List<TopStocks>>> = _losers.asStateFlow()

    init {
        loadTopGainersLosers()
    }

    fun loadTopGainersLosers() {
        viewModelScope.launch {
            repository.getTopGainersLosers()
                .collect {result ->
                    when (result) {
                        is uiResult.Loading -> {
                            _gainers.value = uiResult.Loading
                            _losers.value = uiResult.Loading
                        }
                        is uiResult.Success -> {
                            // Extract separate lists from the response model
                            _gainers.value = uiResult.Success(result.data.top_gainers)
                            _losers.value = uiResult.Success(result.data.top_losers)
                        }
                        is uiResult.Error -> {
                            _gainers.value = uiResult.Error(result.message)
                            _losers.value = uiResult.Error(result.message)
                        }

                    }
                    }
        }
    }
}
