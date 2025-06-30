package com.example.stockproject.domain

import com.example.stockproject.data.reponseModel.CompanyOverview
import com.example.stockproject.data.reponseModel.SearchResponseModel
import com.example.stockproject.data.reponseModel.TopStocksResponseModel
import kotlinx.coroutines.flow.Flow

interface RepoInterface {

    fun getCompanyOverview(symbol: String): Flow<uiResult<CompanyOverview>>

    fun getTopGainersLosers(): Flow<uiResult<TopStocksResponseModel>>

    fun getSearchResult(keyword: String): Flow<uiResult<SearchResponseModel>>

    fun getIntradaySeries(symbol: String, interval: String = "15min"): Flow<uiResult<com.example.stockproject.data.reponseModel.IntradayResponse>>

    fun getDailySeries(symbol: String): Flow<uiResult<com.example.stockproject.data.reponseModel.DailyResponse>>

    fun getWeeklySeries(symbol: String): Flow<uiResult<com.example.stockproject.data.reponseModel.WeeklyResponse>>

}