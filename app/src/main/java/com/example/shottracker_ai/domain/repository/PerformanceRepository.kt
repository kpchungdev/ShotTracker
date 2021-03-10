package com.example.shottracker_ai.domain.repository

import com.example.shottracker_ai.data.performance.Performance
import com.example.shottracker_ai.data.performance.PerformancesDao
import com.example.shottracker_ai.data.prefs.SharedPreferenceStorage
import java.time.LocalDateTime
import javax.inject.Inject

class PerformanceRepository @Inject constructor(
        private val performancesDao: PerformancesDao,
        private val sharedPreferenceStorage: SharedPreferenceStorage,
) {

    fun getPerformances() = performancesDao.getPerformances()

    suspend fun savePerformance(shotsMade: Int, shotAttempts: Int, duration: Int, createTime: LocalDateTime?): Performance {
        sharedPreferenceStorage.totalMadeShots = sharedPreferenceStorage.totalMadeShots + shotsMade
        sharedPreferenceStorage.totalShotAttempts = sharedPreferenceStorage.totalShotAttempts + shotAttempts

        val totalFieldGoal = (sharedPreferenceStorage.totalMadeShots / sharedPreferenceStorage.totalShotAttempts.toFloat())
        return Performance(
                createdTime = createTime ?: LocalDateTime.now(),
                shotsMade = shotsMade,
                shotAttempts = shotAttempts,
                duration = duration,
                totalFieldGoal = totalFieldGoal
        ).also { performance ->
            performancesDao.insertPerformance(performance)
        }
    }

    suspend fun clearPerformances() {
        sharedPreferenceStorage.totalMadeShots = 0
        sharedPreferenceStorage.totalShotAttempts = 0

        performancesDao.deletePerformances()
    }

}