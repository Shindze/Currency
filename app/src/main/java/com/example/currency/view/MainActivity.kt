package com.example.currency.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currency.R
import com.example.currency.adapter.CurrencyAdapter
import com.example.currency.network.NetworkClient
import com.example.currency.repository.CurrencyRepository
import com.example.currency.viewmodel.CurrencyViewModel
import com.example.currency.viewmodel.CurrencyViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: CurrencyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.rcView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val viewModelFactory = CurrencyViewModelFactory(application)


        viewModel = ViewModelProvider(this, viewModelFactory)[CurrencyViewModel::class.java]

        viewModel.currencyRates.observe(this, Observer { rates ->
            recyclerView.adapter = CurrencyAdapter(rates)
        })
    }
}