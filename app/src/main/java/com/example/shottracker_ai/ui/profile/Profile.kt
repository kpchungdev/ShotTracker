package com.example.shottracker_ai.ui.profile

import android.net.Uri

data class Profile(
    val name: String?,
    val image: Uri
) {

    companion object {
        const val imageFileName = "user_image.png"
        const val defaultProfileImageFromAssets = "happy.png"
        val defaultProfileImageChoicesFromAssets = listOf(
            "happy.png",
            "smile.png",
            "smirk.png",
            "neutral.png",
        )
    }

}
