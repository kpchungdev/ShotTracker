package com.example.shottracker_ai.repository

import com.example.shottracker_ai.data.prefs.SharedPreferenceStorage
import com.example.shottracker_ai.ui.profile.Profile
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val sharedPreferenceStorage: SharedPreferenceStorage,
        private val mediaRepository: MediaRepository
) {

    suspend fun getProfile(): Profile {
        val imagePath = sharedPreferenceStorage.userImagePath ?: run {
            val filePath = mediaRepository.saveAssetImage(Profile.defaultProfileImageFromAssets)
            mediaRepository.saveUserImage(filePath)
        }

        return Profile(sharedPreferenceStorage.userName, imagePath)
    }

    suspend fun getDefaultProfileImageChoices(): List<String> {
        return Profile.defaultProfileImageChoicesFromAssets.map { imageName ->
            mediaRepository.getImagePath(imageName) ?: mediaRepository.saveAssetImage(imageName)
        }
    }

//    Might an error saving gallery image
    suspend fun saveProfile(name: String, imagePath: String) {
        sharedPreferenceStorage.userName = name
        sharedPreferenceStorage.userImagePath = mediaRepository.saveUserImage(imagePath)
    }


}