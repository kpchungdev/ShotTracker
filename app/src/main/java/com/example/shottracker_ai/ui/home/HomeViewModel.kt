package com.example.shottracker_ai.ui.home

import androidx.lifecycle.*
import com.example.shottracker_ai.data.performance.Performance
import com.example.shottracker_ai.data.prefs.SharedPreferenceStorage
import com.example.shottracker_ai.domain.repository.PerformanceRepository
import com.example.shottracker_ai.domain.repository.UserRepository
import com.example.shottracker_ai.domain.service.StatAnalyzer
import com.example.shottracker_ai.ui.BaseViewModel
import com.example.shottracker_ai.ui.home.performance.AverageFieldGoalChartSection
import com.example.shottracker_ai.ui.home.ranges.RangeSection
import com.example.shottracker_ai.ui.home.stats.AverageStatSection
import com.example.shottracker_ai.ui.home.stats.StatRange
import com.example.shottracker_ai.ui.home.stats.StatType
import com.example.shottracker_ai.utilities.combineWithLatest
import com.example.shottracker_ai.utilities.format
import com.example.shottracker_ai.utilities.toRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject internal constructor(
        userRepository: UserRepository,
        sharedPreferenceStorage: SharedPreferenceStorage,
        private val performanceRepository: PerformanceRepository,
        private val statAnalyzer: StatAnalyzer,
) : BaseViewModel() {
    val launchDestination: LiveData<LaunchDestination> = MutableLiveData<LaunchDestination>().also { result ->
        launchBackground {
            val destination = if (userRepository.getProfile().name == null) LaunchDestination.PROFILE_SCREEN
            else LaunchDestination.HOME_SCREEN

            result.postValue(destination)
        }
    }

    //     Top
    val userName = sharedPreferenceStorage.userNameLiveData.map { "Hi, ${it}" }
    val profileImage = sharedPreferenceStorage.userImagePathLiveData.map { it }

    private val currentDate = MutableLiveData(LocalDate.now())
    val currentDateText = currentDate.map { it.format() }

    fun updateDate() {
        currentDate.value = LocalDate.now()
    }

    // Average Stat Cards

    private val performances = performanceRepository.getPerformances().asLiveData()
    val range = MutableLiveData(StatRange.ALL)
    fun changeRange(range: StatRange) {
        this.range.value = range
    }

    val targetPerformances = performances.combineWithLatest(range) { performances, range ->
        performances.toRange(range)
    }

    private val comparisonRange = performances.combineWithLatest(range) { performances, range ->
        if (performances.isEmpty()) {
            StatRange.ALL
        } else {
            when (range) {
                StatRange.ALL -> StatRange.DAY
                else -> StatRange.ALL
            }
        }
    }
    private val comparisonPerformances = performances.combineWithLatest(comparisonRange) { performances, range ->
        performances.toRange(range)
    }

    val averageFieldGoal = targetPerformances.combineWithLatest(comparisonPerformances, comparisonRange) { target, compare, comparisonRange ->
        createAverageStatSection(StatType.AVERAGE_FIELD_GOAL, comparisonRange, target, compare)
    }
    val averageShotsMade = targetPerformances.combineWithLatest(comparisonPerformances, comparisonRange) { target, compare, comparisonRange ->
        createAverageStatSection(StatType.AVERAGE_MADE_SHOTS, comparisonRange, target, compare)
    }
    val averageMinutes = targetPerformances.combineWithLatest(comparisonPerformances, comparisonRange) { target, compare, comparisonRange ->
        createAverageStatSection(StatType.AVERAGE_MINUTES, comparisonRange, target, compare)
    }

    private fun createAverageStatSection(statType: StatType,
                                         comparisonRange: StatRange,
                                         targetPerformances: List<Performance>,
                                         comparePerformances: List<Performance>): AverageStatSection {
        val targetStats = if (targetPerformances.isEmpty()) 0.toDouble()
        else statAnalyzer.findAverage(statType, targetPerformances)

        val comparisonStats = if (comparePerformances.isEmpty()) 0.toDouble()
        else statAnalyzer.findAverage(statType, comparePerformances)

        return AverageStatSection(
                statType = statType,
                showComparison = targetPerformances.size >= 2 && targetPerformances.size >= 2,
                comparisonRange = comparisonRange,
                targetAverage = targetStats,
                comparisonDiff = targetStats - comparisonStats
        )
    }

    //    Chart
    val averageFieldGoalChartSection = targetPerformances.map { AverageFieldGoalChartSection(it) }

    //    Ranges
    val rangeSection = performances.combineWithLatest(range) { performances, range ->
        RangeSection(
                selectedRange = range,
                performances = performances
        )
    }

    init {
        launchBackground {
            for (day in 1..45) {
                val shotsMade = Random.nextInt(1, 12)
                val shotAttempts = shotsMade + Random.nextInt(1, 8)

                performanceRepository.savePerformance(
                        shotsMade = shotsMade,
                        shotAttempts = shotAttempts,
                        duration = Random.nextInt(30, 50),
                        createTime = LocalDateTime.now().plusDays(day.toLong())
                )
            }

            Timber.d("size: ${performanceRepository.getPerformances().first().size}")
        }
    }

    fun clearPerformances() {
        launchBackground {
            performanceRepository.clearPerformances()
        }
    }

}

enum class LaunchDestination {
    PROFILE_SCREEN,
    HOME_SCREEN
}
