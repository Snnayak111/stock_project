package com.example.stockproject.data.reponseModel

data class TopStocks(
    val change_amount: String,
    val change_percentage: String,
    val price: String,
    val ticker: String,
    val volume: String
)