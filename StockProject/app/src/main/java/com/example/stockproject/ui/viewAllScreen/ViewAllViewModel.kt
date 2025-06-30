package com.example.stockproject.ui.viewAllScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.stockproject.data.reponseModel.TopStocks
import com.example.stockproject.domain.RepoInterface
import com.example.stockproject.domain.uiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ViewAllViewModel @Inject constructor(
    private val repository: RepoInterface
) : ViewModel() {
    private val _stocks = MutableStateFlow<uiResult<List<TopStocks>>>(uiResult.Loading)
    val stocks: StateFlow<uiResult<List<TopStocks>>> = _stocks.asStateFlow()

    private val _allStocks = MutableStateFlow<List<TopStocks>>(emptyList())
    private val _currentPage = MutableStateFlow(0)
    private val _isLoadingMore = MutableStateFlow(false)
    private val _hasMoreData = MutableStateFlow(true)

    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()
    val hasMoreData: StateFlow<Boolean> = _hasMoreData.asStateFlow()

    companion object {
        const val PAGE_SIZE = 20
    }

    fun loadStocks(isGainer: Boolean) {
        viewModelScope.launch {
            _currentPage.value = 0
            _allStocks.value = emptyList()
            _hasMoreData.value = true
            
            repository.getTopGainersLosers().collect { result ->
                when (result) {
                    is uiResult.Loading -> _stocks.value = uiResult.Loading
                    is uiResult.Success -> {
                        val allData = if (isGainer) {
                            result.data.top_gainers
                        } else {
                            result.data.top_losers
                        }
                        
                        _allStocks.value = allData
                        loadNextPage()
                    }
                    is uiResult.Error -> _stocks.value = uiResult.Error(result.message)
                }
            }
        }
    }
    
    fun loadNextPage() {
        if (_isLoadingMore.value || !_hasMoreData.value) return
        
        viewModelScope.launch {
            _isLoadingMore.value = true
            
            val startIndex = _currentPage.value * PAGE_SIZE
            val endIndex = startIndex + PAGE_SIZE
            val allData = _allStocks.value
            
            if (startIndex >= allData.size) {
                _hasMoreData.value = false
                _isLoadingMore.value = false
                return@launch
            }
            
            val pageData = allData.subList(startIndex, minOf(endIndex, allData.size))
            
            if (_currentPage.value == 0) {
                // First page
                _stocks.value = uiResult.Success(pageData)
            } else {
                // Append to existing data
                val currentData = (_stocks.value as? uiResult.Success)?.data ?: emptyList()
                _stocks.value = uiResult.Success(currentData + pageData)
            }
            
            _currentPage.value = _currentPage.value + 1
            _hasMoreData.value = endIndex < allData.size
            _isLoadingMore.value = false
        }
    }
    
    fun refresh() {
        _currentPage.value = 0
        _hasMoreData.value = true
        loadNextPage()
    }
} 