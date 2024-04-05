package com.example.currency.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currency.model.Currency
import com.example.currency.repository.CurrencyRepository
import kotlinx.coroutines.launch

class CurrencyViewModel(private val repository: CurrencyRepository) : ViewModel() {
    private val _currencyRates = MutableLiveData<Map<String, Currency>>()
    val currencyRates: LiveData<Map<String, Currency>> = _currencyRates

    init {
        fetchCurrencyRates()
    }

    private fun fetchCurrencyRates() {
        viewModelScope.launch {
            try {
                val rates = repository.getCurrencyRates()
                _currencyRates.value = rates
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}


