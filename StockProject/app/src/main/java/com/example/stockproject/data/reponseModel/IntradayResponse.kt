package com.example.stockproject.data.reponseModel

import com.google.gson.annotations.SerializedName

data class IntradayResponse(
    @SerializedName("Time Series (15min)")
    val timeSeries: Map<String, CandleData>?
) 