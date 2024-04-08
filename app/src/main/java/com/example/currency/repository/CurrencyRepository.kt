package com.example.currency.repository

import android.util.Log
import android.widget.Toast
import com.example.currency.network.ApiService
import com.example.currency.model.Currency

class CurrencyRepository(private val apiService: ApiService) {

    private val currencyRatesCache = mutableMapOf<String, Currency>()

    suspend fun getCurrencyRates(): Map<String, Currency> {
        if (currencyRatesCache.isNotEmpty()) {
            return currencyRatesCache
        }

        val response = apiService.getCurrencyRates()
        Log.e("Запрос:", response.toString())

        currencyRatesCache.putAll(response.Valute)

        return currencyRatesCache
    }

    fun getCurrency(charCode: String): Currency? {
        return currencyRatesCache[charCode]
    }

    fun getCurrencyValue(charCode: String): Double {
        return getCurrency(charCode)?.Value ?: 0.0
    }

    fun getCurrencyName(charCode: String): String {
        return getCurrency(charCode)?.Name ?: ""
    }
}