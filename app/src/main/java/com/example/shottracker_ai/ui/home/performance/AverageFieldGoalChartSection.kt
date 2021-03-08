package com.example.shottracker_ai.ui.home.performance

import com.example.shottracker_ai.data.performance.Performance

data class AverageFieldGoalChartSection(
        val performances: List<Performance>,
) {

    val hasPerformances = performances.isNotEmpty()

    private val topPerformance = performances.maxByOrNull { performance -> performance.totalFieldGoal }
    val topFieldGoalLabel = topPerformance?.totalFieldGoal?.let {
        "${"%.2f".format(it)}%"
    }

}