package com.example.stockproject.ui.detailScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.stockproject.data.reponseModel.CompanyOverview
import com.example.stockproject.domain.RepoInterface
import com.example.stockproject.domain.uiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.stockproject.data.reponseModel.CandleData
import com.example.stockproject.data.reponseModel.DailyResponse
import com.example.stockproject.data.reponseModel.IntradayResponse
import com.example.stockproject.data.reponseModel.WeeklyResponse
import kotlinx.coroutines.flow.update

enum class ChartRange { OneDay, OneWeek, OneMonth, OneYear }

class ChartUiState(
    val isLoading: Boolean = false,
    val data: List<Float> = emptyList(),
    val error: String? = null,
    val selectedRange: ChartRange = ChartRange.OneDay
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: RepoInterface
) : ViewModel() {
    private val _details = MutableStateFlow<uiResult<CompanyOverview>>(uiResult.Loading)
    val details: StateFlow<uiResult<CompanyOverview>> = _details.asStateFlow()

    private val _selectedRange = MutableStateFlow(ChartRange.OneDay)
    val selectedRange: StateFlow<ChartRange> = _selectedRange

    private val _chartState = MutableStateFlow(ChartUiState())
    val chartState: StateFlow<ChartUiState> = _chartState.asStateFlow()

    fun setSelectedRange(range: ChartRange) {
        _selectedRange.value = range
    }

    fun loadDetails(symbol: String) {
        viewModelScope.launch {
            repository.getCompanyOverview(symbol).collect { result ->
                _details.value = result
            }
        }
    }

    fun loadChartData(symbol: String, range: ChartRange) {
        _chartState.value = ChartUiState(isLoading = true, selectedRange = range)
        viewModelScope.launch {
            when (range) {
                ChartRange.OneDay -> {
                    repository.getIntradaySeries(symbol, interval = "15min").collect { result ->
                        when (result) {
                            is uiResult.Success -> {
                                val candles = result.data.timeSeries?.toSortedMap(compareByDescending { it })?.values?.take(30)
                                val closes = candles?.mapNotNull { it.close?.toFloatOrNull() }?.reversed() ?: emptyList()
                                _chartState.value = ChartUiState(data = closes, selectedRange = range)
                            }
                            is uiResult.Error -> _chartState.value = ChartUiState(error = result.message, selectedRange = range)
                            is uiResult.Loading -> _chartState.value = ChartUiState(isLoading = true, selectedRange = range)
                        }
                    }
                }
                ChartRange.OneWeek -> {
                    repository.getDailySeries(symbol).collect { result ->
                        when (result) {
                            is uiResult.Success -> {
                                val candles = result.data.timeSeries?.toSortedMap(compareByDescending { it })?.values?.take(7)
                                val closes = candles?.mapNotNull { it.close?.toFloatOrNull() }?.reversed() ?: emptyList()
                                _chartState.value = ChartUiState(data = closes, selectedRange = range)
                            }
                            is uiResult.Error -> _chartState.value = ChartUiState(error = result.message, selectedRange = range)
                            is uiResult.Loading -> _chartState.value = ChartUiState(isLoading = true, selectedRange = range)
                        }
                    }
                }
                ChartRange.OneMonth -> {
                    repository.getDailySeries(symbol).collect { result ->
                        when (result) {
                            is uiResult.Success -> {
                                val candles = result.data.timeSeries?.toSortedMap(compareByDescending { it })?.values?.take(30)
                                val closes = candles?.mapNotNull { it.close?.toFloatOrNull() }?.reversed() ?: emptyList()
                                _chartState.value = ChartUiState(data = closes, selectedRange = range)
                            }
                            is uiResult.Error -> _chartState.value = ChartUiState(error = result.message, selectedRange = range)
                            is uiResult.Loading -> _chartState.value = ChartUiState(isLoading = true, selectedRange = range)
                        }
                    }
                }
                ChartRange.OneYear -> {
                    repository.getWeeklySeries(symbol).collect { result ->
                        when (result) {
                            is uiResult.Success -> {
                                val candles = result.data.timeSeries?.toSortedMap(compareByDescending { it })?.values?.take(52)
                                val closes = candles?.mapNotNull { it.close?.toFloatOrNull() }?.reversed() ?: emptyList()
                                _chartState.value = ChartUiState(data = closes, selectedRange = range)
                            }
                            is uiResult.Error -> _chartState.value = ChartUiState(error = result.message, selectedRange = range)
                            is uiResult.Loading -> _chartState.value = ChartUiState(isLoading = true, selectedRange = range)
                        }
                    }
                }
            }
        }
    }
} 