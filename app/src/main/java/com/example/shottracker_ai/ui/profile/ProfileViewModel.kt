package com.example.shottracker_ai.ui.profile

import androidx.lifecycle.*
import com.example.shottracker_ai.repository.UserRepository
import com.example.shottracker_ai.ui.BaseViewModel
import com.example.shottracker_ai.utilities.combineWithLatest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject internal constructor(private val userRepository: UserRepository) : BaseViewModel() {

    val name = MutableLiveData<String>().also { result ->
        launchBackground {
            result.postValue(userRepository.getProfile().name ?: "")
        }
    }

    val image = MutableLiveData<String>().also { result ->
        launchBackground {
            userRepository.getProfile().image
        }
    }

    val defaultImageChoices = MutableLiveData<List<String>>().also { result ->
        launchBackground {
            userRepository.getDefaultProfileImageChoices()
        }
    }

    val saveProfile: LiveData<Unit>
        get() = name.combineWithLatest(image) { name, image ->
            MutableLiveData<Unit>().also { result ->
                launchBackground {
                    result.postValue(userRepository.saveProfile(name, image))
                }
            }
        }.switchMap { it }

    fun changeImage(imagePath: String) {
        image.value = imagePath
    }

}