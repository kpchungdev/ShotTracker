package com.example.shottracker_ai.ui.home.performance

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.example.shottracker_ai.R
import com.example.shottracker_ai.data.performance.Performance
import com.example.shottracker_ai.databinding.SectionFieldGoalChartBinding
import com.example.shottracker_ai.ui.common.chart.*

data class AverageFieldGoalChartSection(val performances: List<Performance>) {

    val chartData = performances.map { ChartData(it.totalFieldGoal) }

    private val hasPerformances = performances.isNotEmpty()
    val showFieldGoal = hasPerformances
    val labelTotalFieldGoalColor = if (hasPerformances) R.color.orange_500 else R.color.gray_500

    private val topPerformance = performances.maxByOrNull { performance -> performance.totalFieldGoal }
    val topFieldGoal = topPerformance?.totalFieldGoal?.let {
        "${"%.1f".format(it * 100)}%"
    }

}

fun SectionFieldGoalChartBinding.setUp(fragment: Fragment, section: LiveData<AverageFieldGoalChartSection>) {
    this.chart.setUp(fragment, section)

    section.observe(fragment) { section ->
        this.section = section
        this.chart.chartData = section.chartData
    }
}


private fun LinearChart.setUp(fragment: Fragment, section: LiveData<AverageFieldGoalChartSection>) {
    addPlugins(InitialLevelChartPlugin(this))
    addPlugins(TopLevelChartPlugin(this))
    addPlugins(FillChartPlugin(this))
}