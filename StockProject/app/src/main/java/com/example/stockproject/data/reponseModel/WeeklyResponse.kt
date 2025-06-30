package com.example.stockproject.data.reponseModel

import com.google.gson.annotations.SerializedName

data class WeeklyResponse(
    @SerializedName("Weekly Time Series")
    val timeSeries: Map<String, CandleData>?
) 