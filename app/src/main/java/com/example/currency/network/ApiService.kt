package com.example.currency.network

import com.example.currency.model.CurrencyRatesResponse
import retrofit2.http.GET

interface ApiService {
    @GET("daily_json.js")
    suspend fun getCurrencyRates(): CurrencyRatesResponse
}