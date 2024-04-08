package com.example.currency.view

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currency.R
import com.example.currency.adapter.CurrencyAdapter
import com.example.currency.viewmodel.CurrencyViewModel
import com.example.currency.viewmodel.CurrencyViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CurrencyViewModel
    private val updatePeriod: Long = 30000L
    private val animPlayPeriod: Long = 1000L
    private var updateScope = CoroutineScope(Dispatchers.Default)
    private var updateJob: Job? = null
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.setStatusBarContrastEnforced(false)
            window.setNavigationBarContrastEnforced(false)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rcView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        progressBar = findViewById(R.id.progressBar)

        val viewModelFactory = CurrencyViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory)[CurrencyViewModel::class.java]

        startParseJob()

        viewModel.currencyRates.observe(this) { rates ->
            hideProgressBar()
            recyclerView.adapter = CurrencyAdapter(rates)
        }
    }

    private fun startParseJob() {
        updateJob = updateScope.launch {
            while (true) {
                viewModel.currencyRates.value?.isEmpty() ?: showProgressBar()
                delay(animPlayPeriod)
                viewModel.fetchCurrencyRates(application)
                delay(updatePeriod)
            }
        }
    }

    private fun showProgressBar() {
        runOnUiThread {
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        runOnUiThread {
            progressBar.visibility = View.GONE
        }
    }

    private fun stopUpdateJob() {
        updateJob?.cancel()
        updateJob = null
    }

    override fun onResume() {
        super.onResume()

        if (updateJob == null) startParseJob()
    }

    override fun onStop() {
        super.onStop()
        stopUpdateJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        updateScope.cancel()
    }
}