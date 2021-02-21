package com.example.shottracker_ai.domain.repository

import android.net.Uri
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
            fileManager.doesFileExists(it)
        } ?: run {
            val filePath = fileManager.saveAsset(Profile.defaultProfileImageFromAssets)
            fileManager.saveFile(filePath, Profile.imageFileName).also { savedImagePath ->
                sharedPreferenceStorage.userImagePath = savedImagePath
            }
        } ?: error("profile image should have been generated")

        return Profile(sharedPreferenceStorage.userName, imagePath)
    }

    suspend fun getDefaultProfileImageChoices(): List<Uri> {
        return Profile.defaultProfileImageChoicesFromAssets.map { imageName ->
            fileManager.getImagePath(imageName) ?: fileManager.saveAsset(imageName)
        }
    }

    //    Might an error saving gallery image
    suspend fun saveProfile(name: String, imagePath: Uri) {
        sharedPreferenceStorage.userName = name
        sharedPreferenceStorage.userImagePath = fileManager.saveFile(imagePath, Profile.imageFileName)
    }


}