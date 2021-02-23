package com.example.shottracker_ai.domain.service

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.shottracker_ai.data.performance.Performance
import com.example.shottracker_ai.ui.home.ui.StatType
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
@SmallTest
class StatAnalyzerTest {

    private val statAnalyzer = StatAnalyzer()

    private val performance1 = Performance(
            createdTime = LocalDateTime.now(),
            shotsMade = 1,
            shotAttempts = 10,
            duration = 10
    )

    private val performance2 = Performance(
            createdTime = LocalDateTime.now().plusDays(1),
            shotsMade = 2,
            shotAttempts = 10,
            duration = 10
    )

    private val performance3 = Performance(
            createdTime = LocalDateTime.now().plusDays(2),
            shotsMade = 3,
            shotAttempts = 10,
            duration = 10
    )

    val performances = listOf(performance1, performance2, performance3)

    @Test
    fun testAverageFieldGoal() = runBlocking {
        val averageFieldGoal = statAnalyzer.findAverage(
                statType = StatType.AVERAGE_FIELD_GOAL,
                performances = performances.changeRange(WorkOutAwayFromCurrent.ALL),
        )

        assertEquals(averageFieldGoal, 0.2, 0.0)
    }

    @Test
    fun testAverageShotsMade() = runBlocking {
        val averageMadeShots = statAnalyzer.findAverage(
                statType = StatType.AVERAGE_MADE_SHOTS,
                performances = performances.changeRange(WorkOutAwayFromCurrent.ALL),
        )

        assertEquals(averageMadeShots, 2.0, 0.0)
    }

    @Test
    fun testAverageMinutes() = runBlocking {
        val averageMinutes = statAnalyzer.findAverage(
                statType = StatType.AVERAGE_MINUTES,
                performances = performances.changeRange(WorkOutAwayFromCurrent.ALL),
        )

        assertEquals(averageMinutes, 10.0, 0.0)
    }

    @Test
    fun testAverageFieldGoalOneDayAwayFromCurrent() = runBlocking {
        val averageFieldGoal = statAnalyzer.findAverage(
                statType = StatType.AVERAGE_FIELD_GOAL,
                performances = performances.changeRange(WorkOutAwayFromCurrent.ONE_DAY)
        )

        assertEquals(averageFieldGoal, .15, 0.0)
    }

}