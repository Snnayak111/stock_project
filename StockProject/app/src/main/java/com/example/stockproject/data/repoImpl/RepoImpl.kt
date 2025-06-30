package com.example.stockproject.data.repoImpl

import android.util.Log
import com.example.stockproject.data.apiCall.RetrofitService
import com.example.stockproject.data.reponseModel.CompanyOverview
import com.example.stockproject.data.reponseModel.SearchResponseModel
import com.example.stockproject.data.reponseModel.TopStocksResponseModel
import com.example.stockproject.domain.RepoInterface
import com.example.stockproject.domain.uiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RepoImpl @Inject constructor(
    private val api: RetrofitService
): RepoInterface {
    override fun getCompanyOverview(symbol: String): Flow<uiResult<CompanyOverview>> =flow {
        emit(uiResult.Loading)
        try {
            val response = api.getCompanyOverview(symbol = symbol)
            emit(uiResult.Success(response))
        }
        catch (e:Exception){
            emit(uiResult.Error(e.message.toString()))
        }

    }

    override fun getTopGainersLosers(): Flow<uiResult<TopStocksResponseModel>> =flow {
        emit(uiResult.Loading)
        try {
            val response = api.getTopGainersAndLosers()
            Log.d("fuck you", "getTopGainersLosers: ${response.top_gainers}")
            emit(uiResult.Success(response))
        }
        catch (e:Exception){
            emit(uiResult.Error(e.message.toString()))
        }
    }

    override fun getSearchResult(keyword: String): Flow<uiResult<SearchResponseModel>> =flow {
        emit(uiResult.Loading)
        try {
            val response = api.getSearchTicker(keywords = keyword)
            emit(uiResult.Success(response))
        }
        catch (e:Exception){
            emit(uiResult.Error(e.message.toString()))
        }
    }

    override fun getIntradaySeries(symbol: String, interval: String): Flow<uiResult<com.example.stockproject.data.reponseModel.IntradayResponse>> = flow {
        emit(uiResult.Loading)
        try {
            val response = api.getIntradayData(symbol = symbol, interval = interval)
            emit(uiResult.Success(response))
        } catch (e: Exception) {
            emit(uiResult.Error(e.message.toString()))
        }
    }

    override fun getDailySeries(symbol: String): Flow<uiResult<com.example.stockproject.data.reponseModel.DailyResponse>> = flow {
        emit(uiResult.Loading)
        try {
            val response = api.getDailyData(symbol = symbol)
            emit(uiResult.Success(response))
        } catch (e: Exception) {
            emit(uiResult.Error(e.message.toString()))
        }
    }

    override fun getWeeklySeries(symbol: String): Flow<uiResult<com.example.stockproject.data.reponseModel.WeeklyResponse>> = flow {
        emit(uiResult.Loading)
        try {
            val response = api.getWeeklyData(symbol = symbol)
            emit(uiResult.Success(response))
        } catch (e: Exception) {
            emit(uiResult.Error(e.message.toString()))
        }
    }

}