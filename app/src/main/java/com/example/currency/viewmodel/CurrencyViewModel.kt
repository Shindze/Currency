package com.example.currency.viewmodel

import android.app.Application
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

class CurrencyViewModel(private val repository: CurrencyRepository, application: Application) : ViewModel() {

    private val _currencyRates = MutableLiveData<Map<String, Currency>>()
    val currencyRates: LiveData<Map<String, Currency>> = _currencyRates

//    private val updatePeriod: Long = 30000L
//    private val updateScope = CoroutineScope(Dispatchers.Default)

//    init {
//        updateScope.launch {
////            while (true) {
//                fetchCurrencyRates(application)
////                delay(updatePeriod)
////            }
//        }
//    }

     fun fetchCurrencyRates(application: Application) {
        viewModelScope.launch {
            try {
                val rates = repository.getCurrencyRates()
                _currencyRates.value = rates
                val errorMessage = "данные получены."
                Toast.makeText(application, errorMessage, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                val errorMessage = "Ошибка получения курсов валют. Пожалуйста, попробуйте позже."
                Toast.makeText(application, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

//    override fun onCleared() {
//        super.onCleared()
//        updateScope.cancel()
//    }
}



