package com.example.currency.model

data class Currency(
    val ID: String,
    val NumCode: String,
    val CharCode: String,
    val Nominal: Int,
    val Name: String,
    val Value: Double,
    val Previous: Double
)

data class CurrencyRatesResponse(
    val Valute: Map<String, Currency>
)