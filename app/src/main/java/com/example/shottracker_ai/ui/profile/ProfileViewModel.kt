package com.example.shottracker_ai.ui.profile

import android.net.Uri
import androidx.lifecycle.*
import com.example.shottracker_ai.domain.repository.UserRepository
import com.example.shottracker_ai.ui.BaseViewModel
import com.example.shottracker_ai.utilities.combineWithLatest
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject internal constructor(
    private val userRepository: UserRepository
    ) : BaseViewModel() {

    val name = MutableLiveData<String>().also { result ->
        launchBackground {
            result.postValue(userRepository.getProfile().name ?: "")
        }
    }

    val profileImage = MutableLiveData<Uri>().also { result ->
        launchBackground {
            result.postValue(userRepository.getProfile().image)
        }
    }

    val defaultImageChoices: LiveData<List<Uri>> = MutableLiveData<List<Uri>>().also { result ->
        launchBackground {
            result.postValue(userRepository.getDefaultProfileImageChoices())
        }
    }

    val canSaveProfile = name.map {
        Timber.d("canSaveProfile: ${it.isNotBlank()}")
        it.isNotBlank()
    }

    val saveProfile: LiveData<Unit>
        get() = name.combineWithLatest(profileImage) { name, image ->
            MutableLiveData<Unit>().also { result ->
                launchBackground {
                    result.postValue(userRepository.saveProfile(name, image))
                }
            }
        }.switchMap { it }

    fun changeProfileImage(imagePath: Uri) {
        profileImage.value = imagePath
    }

}