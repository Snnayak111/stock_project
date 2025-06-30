package com.example.stockproject.data.apiCall

import com.example.stockproject.data.reponseModel.CompanyOverview
import com.example.stockproject.data.reponseModel.DailyResponse
import com.example.stockproject.data.reponseModel.IntradayResponse
import com.example.stockproject.data.reponseModel.SearchResponseModel
import com.example.stockproject.data.reponseModel.TopStocksResponseModel
import com.example.stockproject.data.reponseModel.WeeklyResponse
import org.openjdk.tools.javac.util.DefinedBy
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    companion object {
        const val APIKEY = "DVLKLKRK9693FX0B"
    }

    @GET("query")
    suspend fun getSearchTicker(
        @Query("function") function: String = "SYMBOL_SEARCH",
        @Query("keywords") keywords: String,
        @Query("apikey") apiKey: String = APIKEY
    ): SearchResponseModel

    @GET("query")
    suspend fun getTopGainersAndLosers(
        @Query("function") function: String = "TOP_GAINERS_LOSERS",
        @Query("apikey") apiKey: String = APIKEY
    ): TopStocksResponseModel

    @GET("query")
    suspend fun getCompanyOverview(
        @Query("function") function: String = "OVERVIEW",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = APIKEY
    ): CompanyOverview

    @GET("query")
    suspend fun getIntradayData(
        @Query("function") function: String = "TIME_SERIES_INTRADAY",
        @Query("symbol") symbol: String,
        @Query("interval") interval: String = "15min",
        @Query("apikey") apiKey: String = APIKEY
    ): IntradayResponse

    @GET("query")
    suspend fun getDailyData(
        @Query("function") function: String = "TIME_SERIES_DAILY",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = APIKEY
    ): DailyResponse

    @GET("query")
    suspend fun getWeeklyData(
        @Query("function") function: String = "TIME_SERIES_WEEKLY",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = APIKEY
    ): WeeklyResponse
}