package com.example.stockproject.domain

sealed class uiResult<out T> {
    object Loading : uiResult<Nothing>()
    data class Success<out T>(val data: T) : uiResult<T>()
    data class Error(val message: String) : uiResult<Nothing>()
}
