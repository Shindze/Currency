package com.example.currency.view

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
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
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class MainActivity : AppCompatActivity() {

    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
    private lateinit var viewModel: CurrencyViewModel
    private val updatePeriod: Long = 30000L
    private val animPlayPeriod: Long = 1000L
    private var updateScope = CoroutineScope(Dispatchers.Default)
    private var updateJob: Job? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var dateTextView: TextView

    @SuppressLint("MissingInflatedId", "SetTextI18n", "PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.let { actionBar ->
            actionBar.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.Primary)))
            val title = SpannableString("Курс валют")
            title.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, com.google.android.material.R.color.m3_ref_palette_white)), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            title.setSpan(AbsoluteSizeSpan(24, true), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            supportActionBar?.title = title
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.setStatusBarContrastEnforced(false)
            window.setNavigationBarContrastEnforced(false)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rcView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        progressBar = findViewById(R.id.progressBar)
        dateTextView = findViewById(R.id.Date)

        val viewModelFactory = CurrencyViewModelFactory(application)

        viewModel = ViewModelProvider(this, viewModelFactory)[CurrencyViewModel::class.java]

        startParseJob()

        viewModel.currencyRates.observe(this) { rates ->
            hideProgressBar()
            recyclerView.adapter = CurrencyAdapter(rates)
        }

        viewModel.currencyDates.observe(this) { dates ->
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = dates["PreviousDate"]?.let { inputFormat.parse(it) }
            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedDate = date?.let { outputFormat.format(it) }

            dateTextView.text = "Последнее обновление: $formattedDate"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.refresh_button -> {
                startParseJob()
                Toast.makeText(this, "Данные актуализированы", Toast.LENGTH_SHORT).show()
            }
        }
        return true
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