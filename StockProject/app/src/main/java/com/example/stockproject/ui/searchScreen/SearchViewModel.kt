package com.example.stockproject.ui.searchScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.stockproject.data.reponseModel.BestMatch
import com.example.stockproject.domain.RepoInterface
import com.example.stockproject.domain.uiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: RepoInterface
) : ViewModel() {
    private val _results = MutableStateFlow<uiResult<List<BestMatch>>>(uiResult.Loading)
    val results: StateFlow<uiResult<List<BestMatch>>> = _results.asStateFlow()

    fun search(keyword: String) {
        viewModelScope.launch {
            repository.getSearchResult(keyword).collect { result ->
                when (result) {
                    is uiResult.Loading -> _results.value = uiResult.Loading
                    is uiResult.Success -> _results.value = uiResult.Success(result.data.bestMatches ?: emptyList())
                    is uiResult.Error -> _results.value = uiResult.Error(result.message)
                }
            }
        }
    }
} 