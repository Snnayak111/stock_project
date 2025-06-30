package com.example.stockproject.data.reponseModel

import com.google.gson.annotations.SerializedName

data class DailyResponse(
    @SerializedName("Time Series (Daily)")
    val timeSeries: Map<String, CandleData>?
) 