package com.example.shottracker_ai.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.shottracker_ai.data.performance.Performance
import com.example.shottracker_ai.domain.repository.PerformanceRepository
import com.example.shottracker_ai.domain.repository.UserRepository
import com.example.shottracker_ai.domain.service.StatAnalyzer
import com.example.shottracker_ai.ui.BaseViewModel
import com.example.shottracker_ai.ui.home.ui.AverageStatSection
import com.example.shottracker_ai.ui.home.ui.AverageStateSection
import com.example.shottracker_ai.ui.home.ui.StatRange
import com.example.shottracker_ai.ui.home.ui.StatType
import com.example.shottracker_ai.utilities.combineWithLatest
import com.example.shottracker_ai.utilities.toRange
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject internal constructor(
        userRepository: UserRepository,
        performanceRepository: PerformanceRepository,
        private val statAnalyzer: StatAnalyzer,
) : BaseViewModel() {
    val launchDestination: LiveData<LaunchDestination> = MutableLiveData<LaunchDestination>().also { result ->
        launchBackground {
            val destination = if (userRepository.getProfile().name == null) LaunchDestination.PROFILE_SCREEN
            else LaunchDestination.HOME_SCREEN

            result.postValue(destination)
        }
    }

    private val performances = performanceRepository.getPerformances().asLiveData()
    private val range = MutableLiveData(StatRange.ALL)

    private val targetPerformances = performances.combineWithLatest(range) { performances, range ->
        performances.toRange(range)
    }
    private val comparisonPerformances = performances.combineWithLatest(range) { performances, range ->
        when (range) {
            StatRange.ALL -> performances.toRange(StatRange.DAY)
            else -> performances.toRange(StatRange.ALL)
        }
    }

    private val averageFieldGoal = targetPerformances.combineWithLatest(comparisonPerformances) { target, compare ->
        createAverageStatSection(StatType.AVERAGE_FIELD_GOAL, target, compare)
    }

    private val averageShotsMade = targetPerformances.combineWithLatest(comparisonPerformances) { target, compare ->
        createAverageStatSection(StatType.AVERAGE_MADE_SHOTS, target, compare)
    }


    private val averageMinutes = targetPerformances.combineWithLatest(comparisonPerformances) { target, compare ->
        createAverageStatSection(StatType.AVERAGE_MINUTES, target, compare)
    }

    private fun createAverageStatSection(statType: StatType,
                                         targetPerformances: List<Performance>,
                                         comparePerformances: List<Performance>): AverageStatSection {
        val targetFieldGoal = statAnalyzer.findAverage(statType, targetPerformances)
        val comparisonFieldGoal = statAnalyzer.findAverage(statType, comparePerformances)

        return AverageStatSection(
                statType = statType,
                targetAverage = targetFieldGoal,
                comparisonAverage = comparisonFieldGoal
        )
    }




}

enum class LaunchDestination {
    PROFILE_SCREEN,
    HOME_SCREEN
}

