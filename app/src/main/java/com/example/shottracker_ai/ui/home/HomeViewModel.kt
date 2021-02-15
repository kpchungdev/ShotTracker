package com.example.shottracker_ai.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.shottracker_ai.domain.IsProfileCompleteUseCase
import com.example.shottracker_ai.result.Event
import com.example.shottracker_ai.result.data
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject internal constructor(
    isProfileCompleteUseCase: IsProfileCompleteUseCase
) : ViewModel() {
    val launchDestination: LiveData<Event<LaunchDestination>> = liveData {
        val result = isProfileCompleteUseCase(Unit)
        if (result.data == false) {
            emit(Event(LaunchDestination.PROFILE_SCREEN))
        } else {
            emit(Event(LaunchDestination.HOME_SCREEN))
        }
    }
}

enum class LaunchDestination {
    PROFILE_SCREEN,
    HOME_SCREEN
}