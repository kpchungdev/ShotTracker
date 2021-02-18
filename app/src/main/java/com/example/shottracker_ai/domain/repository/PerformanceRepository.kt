package com.example.shottracker_ai.domain.repository

import com.example.shottracker_ai.data.performance.Performance
import com.example.shottracker_ai.data.performance.PerformancesDao
import java.time.LocalDateTime
import javax.inject.Inject

class PerformanceRepository @Inject constructor(
    private val performancesDao: PerformancesDao
) {

    fun getPerformances() = performancesDao.getPerformances()

    suspend fun savePerformance(shotsMade: Int, shotAttempts: Int, duration: Int): Performance {
        return Performance(
            createdTime = LocalDateTime.now(),
            shotsMade = shotsMade,
            shotAttempts = shotAttempts,
            duration = duration
        ).also { performance ->
            performancesDao.insertPerformance(performance)
        }
    }

}