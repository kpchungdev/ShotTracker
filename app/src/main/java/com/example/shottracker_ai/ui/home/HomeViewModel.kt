package com.example.shottracker_ai.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shottracker_ai.domain.repository.UserRepository
import com.example.shottracker_ai.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject internal constructor(
        userRepository: UserRepository
) : BaseViewModel() {
    val launchDestination: LiveData<LaunchDestination> = MutableLiveData<LaunchDestination>().also { result ->
        launchBackground {
            val destination = if (userRepository.getProfile().name == null) LaunchDestination.PROFILE_SCREEN
            else LaunchDestination.HOME_SCREEN

            result.postValue(destination)
        }
    }
}

enum class LaunchDestination {
    PROFILE_SCREEN,
    HOME_SCREEN
}