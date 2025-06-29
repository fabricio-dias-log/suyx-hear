package com.example.suyxhear

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.suyxhear.adapter.AlertAdapter
import com.example.suyxhear.databinding.FragmentHistoryBinding
import com.example.suyxhear.viewmodel.SharedViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.R as MaterialR

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var alertAdapter: AlertAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupChart()

        sharedViewModel.noiseHistory.observe(viewLifecycleOwner) { history ->
            val hasHistory = history.isNotEmpty()
            binding.lineChart.isVisible = hasHistory
            binding.textAlertsTitle.isVisible = hasHistory
            binding.recyclerViewAlerts.isVisible = hasHistory
            binding.textEmptyHistory.isVisible = !hasHistory

            if (hasHistory) {
                updateChart(history)
                val dbLimit = sharedViewModel.dbLimit.value ?: 55
                val alerts = history.filter { it.decibels > dbLimit }.reversed()
                alertAdapter.submitList(alerts)
            }
        }
    }

    private fun setupRecyclerView() {
        alertAdapter = AlertAdapter()
        binding.recyclerViewAlerts.apply {
            adapter = alertAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupChart() {
        binding.lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setDrawGridBackground(false)
            legend.isEnabled = false
            xAxis.textColor = Color.GRAY
            axisLeft.textColor = Color.GRAY
            axisRight.isEnabled = false
        }
    }

    private fun updateChart(history: List<com.example.suyxhear.data.NoiseRecord>) {
        val entries = ArrayList<Entry>()
        history.forEachIndexed { index, record ->
            entries.add(Entry(index.toFloat(), record.decibels.toFloat()))
        }

        val colorPrimary = com.google.android.material.color.MaterialColors.getColor(requireContext(), MaterialR.attr.colorPrimary, Color.BLUE)

        val dataSet = LineDataSet(entries, "Nível de Ruído").apply {
            color = colorPrimary
            valueTextColor = com.google.android.material.color.MaterialColors.getColor(requireContext(), MaterialR.attr.colorOnSurface, Color.BLACK)
            setDrawCircles(false)
            lineWidth = 2f
        }

        val lineData = LineData(dataSet)
        binding.lineChart.data = lineData
        binding.lineChart.invalidate() // refresh
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
