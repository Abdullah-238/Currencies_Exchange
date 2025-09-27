package com.techabdullah.currencies_exchange.ui.currencies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techabdullah.currencies_exchange.R

class CurrencyAdapter(private var items: Map<String, Double>) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private var entries: List<Map.Entry<String, Double>> = items.entries.toList()

    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val tvType: TextView = itemView.findViewById(R.id.tvTitle)
        val tvNote: TextView = itemView.findViewById(R.id.tvText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_currency, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val entry = entries[position]
        holder.tvType.text = entry.key
        holder.tvNote.text = entry.value.toString()
    }

    override fun getItemCount(): Int = entries.size

    fun updateData(newItems: Map<String, Double>) {
        items = newItems
        entries = newItems.entries.toList()
        notifyDataSetChanged()
    }
}