package com.example.stockproject.data.reponseModel

data class TopStocksResponseModel(
    val last_updated: String,
    val metadata: String,
    val most_actively_traded: List<MostActivelyTraded>,
    val top_gainers: List<TopStocks>,
    val top_losers: List<TopStocks>
)