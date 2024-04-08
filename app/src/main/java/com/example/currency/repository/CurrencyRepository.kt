package com.example.currency.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.example.currency.network.ApiService
import com.example.currency.model.Currency

class CurrencyRepository(private val apiService: ApiService) {

    private val currencyRatesCache = mutableMapOf<String, Currency>()
    private val currencyUpdateDate = mutableMapOf<String, String>()

    suspend fun getCurrencyRates(): Map<String, Currency> {
        if (currencyRatesCache.isNotEmpty()) {
            return currencyRatesCache
        }

        val response = apiService.getCurrencyRates()
        Log.e("Данные запрошены:", response.toString())

        currencyUpdateDate["Date"] = response.Date
        currencyUpdateDate["PreviousDate"] = response.PreviousDate

        currencyRatesCache.putAll(response.Valute)

        return currencyRatesCache
    }

    fun getCurrencyUpdateDateMap(): Map<String, String> {
        return currencyUpdateDate
    }

}