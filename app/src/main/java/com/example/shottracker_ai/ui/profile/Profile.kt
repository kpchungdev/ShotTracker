package com.example.shottracker_ai.ui.profile

data class Profile(val name: String?,
                   val image: String) {

    companion object {
        const val defaultProfileImageFromAssets = "happy.png"

        val defaultProfileImageChoicesFromAssets = listOf(
                "happy.png",
                "neutral.png",
                "smile.png",
                "smirk.png"
        )

    }

}
