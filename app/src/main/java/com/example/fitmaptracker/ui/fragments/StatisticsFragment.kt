package com.example.fitmaptracker.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fitmaptracker.R
import com.example.fitmaptracker.other.Constants.formatMillisToMinutesSeconds
import com.example.fitmaptracker.other.CustomMarkerView
import com.example.fitmaptracker.ui.viewmodels.StatisticsViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel by viewModels<StatisticsViewModel>()

    lateinit var tvTotalTime :TextView
    lateinit var tvTotalDistance: TextView
    lateinit var  tvTotalCalories :TextView
    lateinit var  tvAverageSpeed :TextView

    lateinit var  bar_chart :BarChart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTotalTime = view.findViewById(R.id.tv_total_time)
        tvTotalDistance = view.findViewById(R.id.tv_total_distance)
        tvTotalCalories = view.findViewById(R.id.tv_total_calories)
        tvAverageSpeed = view.findViewById(R.id.tv_average_speed)
        bar_chart = view.findViewById(R.id.bar_chart)



        lifecycleScope.launch {
            viewModel.getSummary().collect{run->
                withContext(Dispatchers.Main){

                        val avgSpeed = String.format("%.2f",run.avgSpeedInKMH)
                        val distanceKm = String.format("%.2f",run.distanceInMeters/1000f)
                        tvTotalTime.text=formatMillisToMinutesSeconds(run.timeInMillis)
                        tvTotalDistance.text=distanceKm
                        tvTotalCalories.text=run.caloriesBurned.toString()
                        tvAverageSpeed.text=avgSpeed
                    }
                }
            }
        setupBarChart()
    }

    private fun setupBarChart(){
        bar_chart.run {
            xAxis.apply {
                position= XAxis.XAxisPosition.BOTTOM
                setDrawLabels(false)
                axisLineColor= Color.WHITE
                textColor= Color.WHITE
                setDrawGridLines(false)
            }
            axisLeft.apply {
                axisLineColor= Color.WHITE
                textColor= Color.WHITE
                setDrawGridLines(false)
            }
            axisLeft.apply {
                axisLineColor= Color.WHITE
                textColor= Color.WHITE
                setDrawGridLines(false)
            }
            description.text="Abg Speed Over Time"
            legend.isEnabled=false

            viewModel.getRunsSortedByDate().observe(viewLifecycleOwner){
                it?.let{
                    val avgSpeedBarEntries = it.indices.map { i -> BarEntry(i.toFloat(), it[i].avgSpeedInKMH) }
                    val barDataSet = BarDataSet(avgSpeedBarEntries,"Avg Speeds Over Time ")
                    val barData = BarData(barDataSet)
                    data=barData
                }
                // used to create a pop-up menu and shown when clicked on the graph
                marker= CustomMarkerView(it,requireContext(),R.layout.marker_view)
                invalidate()
            }
        }
    }
}