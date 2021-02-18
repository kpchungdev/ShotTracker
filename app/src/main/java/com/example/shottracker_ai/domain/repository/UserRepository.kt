package com.example.shottracker_ai.domain.repository

import android.net.Uri
import androidx.core.net.toUri
import com.example.shottracker_ai.data.prefs.SharedPreferenceStorage
import com.example.shottracker_ai.ui.profile.Profile
import com.example.shottracker_ai.domain.service.FileManager
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val sharedPreferenceStorage: SharedPreferenceStorage,
    private val fileManager: FileManager
) {

    suspend fun getProfile(): Profile {
        val imagePath = sharedPreferenceStorage.userImagePath?.takeIf {
            fileManager.doesImageFileExists(it)
        } ?: run {
            val filePath = fileManager.saveAssetImage(Profile.defaultProfileImageFromAssets)
            fileManager.saveUserImage(filePath).also { savedImagePath ->
                sharedPreferenceStorage.userImagePath = savedImagePath
            }
        }

        return Profile(sharedPreferenceStorage.userName, imagePath)
    }

    suspend fun getDefaultProfileImageChoices(): List<Uri> {
        return Profile.defaultProfileImageChoicesFromAssets.map { imageName ->
            fileManager.getImagePath(imageName) ?: fileManager.saveAssetImage(imageName)
        }
    }

    //    Might an error saving gallery image
    suspend fun saveProfile(name: String, imagePath: Uri) {
        sharedPreferenceStorage.userName = name
        sharedPreferenceStorage.userImagePath = fileManager.saveUserImage(imagePath)
    }


}