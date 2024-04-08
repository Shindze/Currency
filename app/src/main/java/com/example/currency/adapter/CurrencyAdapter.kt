package com.example.currency.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currency.R
import com.example.currency.model.Currency

class CurrencyAdapter(private val valute: Map<String, Currency>) : RecyclerView.Adapter<CurrencyAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsCurrencies = valute.values.toList()
        val currencyItem = itemsCurrencies[position]

        holder.currencyTextView.text = currencyItem.Name

        holder.valueTextView.text = currencyItem.Value.toString() + " ₽"
    }

    override fun getItemCount(): Int {
        return valute.values.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val currencyTextView: TextView = itemView.findViewById(R.id.currencyText)
        val valueTextView: TextView = itemView.findViewById(R.id.valueText)
    }

}