package com.example.suyxhear.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.suyxhear.data.NoiseRecord
import com.example.suyxhear.databinding.ItemAlertBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlertAdapter : ListAdapter<NoiseRecord, AlertAdapter.AlertViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val binding = ItemAlertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class AlertViewHolder(private val binding: ItemAlertBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: NoiseRecord) {
            binding.textAlertDb.text = "${record.decibels} dB"
            binding.textAlertTime.text = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date(record.timestamp))
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<NoiseRecord>() {
            override fun areItemsTheSame(oldItem: NoiseRecord, newItem: NoiseRecord): Boolean {
                return oldItem.timestamp == newItem.timestamp
            }

            override fun areContentsTheSame(oldItem: NoiseRecord, newItem: NoiseRecord): Boolean {
                return oldItem == newItem
            }
        }
    }
}