package com.example.shottracker_ai.domain.service

import com.example.shottracker_ai.data.performance.Performance
import com.example.shottracker_ai.ui.home.stats.StatType

class StatAnalyzer {

//    Can assume the performances are sorted
    fun findAverage(statType: StatType, performances: List<Performance>): Double {
        return when (statType) {
            StatType.AVERAGE_FIELD_GOAL -> findAverageFieldGoal(performances)
            StatType.AVERAGE_MADE_SHOTS -> findAverageShotsMade(performances)
            StatType.AVERAGE_MINUTES -> findAverageMinutes(performances)
        }
    }

    private fun findAverageFieldGoal(sortedPerformances: List<Performance>): Double {
        val totalShotsMade = findShotsMade(sortedPerformances)
        val totalShotAttempts = sortedPerformances.sumBy { it.shotAttempts }
        return totalShotsMade.toDouble() / totalShotAttempts.toDouble()
    }

    private fun findAverageShotsMade(sortedPerformances: List<Performance>): Double {
        val totalMadeShots = findShotsMade(sortedPerformances)
        return totalMadeShots.toDouble() / sortedPerformances.size.toDouble()
    }

    private fun findAverageMinutes(sortedPerformances: List<Performance>): Double {
        val totalDuration = sortedPerformances.sumBy { it.duration }.toDouble()
        return totalDuration / sortedPerformances.size.toDouble()
    }

    private fun findShotsMade(sortedPerformances: List<Performance>) = sortedPerformances.sumBy { it.shotsMade }

}

enum class WorkOutAwayFromCurrent {
    ONE_DAY, WEEK, MONTH, YEAR, ALL;
}

fun List<Performance>.changeRange(workOutAwayFromCurrent: WorkOutAwayFromCurrent) = when (workOutAwayFromCurrent) {
    WorkOutAwayFromCurrent.ALL -> this.subList(0, this.size)
    WorkOutAwayFromCurrent.ONE_DAY -> this.subList(0, this.size - 1)
    WorkOutAwayFromCurrent.WEEK -> this.subList(0, this.size - 7)
    WorkOutAwayFromCurrent.MONTH -> this.subList(0, this.size - 30)
    WorkOutAwayFromCurrent.YEAR -> this.subList(0, this.size - 365)
}