package com.example.currency.model

data class Currency(
    val ID: String,
    val Name: String,
    val Value: Double,
)

data class CurrencyRatesResponse(
    val Date: String,
    val PreviousDate: String,
    val Valute: Map<String, Currency>
)