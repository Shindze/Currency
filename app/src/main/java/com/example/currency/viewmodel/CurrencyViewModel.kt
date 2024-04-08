package com.example.currency.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currency.model.Currency
import com.example.currency.repository.CurrencyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class CurrencyViewModel(private val repository: CurrencyRepository, application: Application) : ViewModel() {

    private val _currencyRates = MutableLiveData<Map<String, Currency>>()
    val currencyRates: LiveData<Map<String, Currency>> = _currencyRates

    private val _currencyDates = MutableLiveData<Map<String, String>>()
    val currencyDates: LiveData<Map<String, String>> = _currencyDates

     fun fetchCurrencyRates(application: Application) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val rates = repository.getCurrencyRates()
                _currencyRates.value = rates

                val dates = repository.getCurrencyUpdateDateMap()
                _currencyDates.value = dates

                Log.e("Данные получены:", currencyDates.toString() + currencyRates.toString())

            } catch (e: Exception) {
                val errorMessage = "Ошибка получения курсов валют. Пожалуйста, попробуйте позже."

                Log.e("Данные не получены:", e.toString())
                Toast.makeText(application, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}



