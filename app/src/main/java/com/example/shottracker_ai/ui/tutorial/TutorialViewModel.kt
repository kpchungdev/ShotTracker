package com.example.shottracker_ai.ui.tutorial

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.shottracker_ai.R
import com.example.shottracker_ai.data.prefs.SharedPreferenceStorage
import com.example.shottracker_ai.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject internal constructor(
    private val sharedPreferenceStorage: SharedPreferenceStorage
) : BaseViewModel() {

    val instructions = listOf(
        TutorialInstructions(
            imageRes = R.drawable.zone_tutorial,
            title = getString(R.string.walk_into_camera),
            subtitle = getString(
                R.string.enter_one_of_the_camera_zones_to_begin_the_camera_configuration_process
            )
        ),
        TutorialInstructions(
            imageRes = R.drawable.detection_tutorial,
            title = getString(R.string.find_the_hoop_with_your_camera),
            subtitle = getString(
                R.string.point_your_camera_to_the_hoop
            )
        ),
        TutorialInstructions(
            imageRes = R.drawable.finish_tutorial,
            title = getString(R.string.ready_to_practice),
            subtitle = getString(
                R.string.this_button_will_light_up_ready_for_shot_tracking
            )
        )
    )

    val currentPage = MutableLiveData(0)
    val buttonText = currentPage.map {
        if (it == instructions.lastIndex) getString(R.string.play)
        else getString(R.string.action_continue)
    }

    fun isLastPage(): Boolean = currentPage.value == instructions.lastIndex

    fun markReadTutorial() {
        sharedPreferenceStorage.hasViewedTutorial = true
    }

    fun nextPage() {
        currentPage.value?.let {
            currentPage.value = it + 1
        }
    }

    fun prevPage() {
        currentPage.value?.let {
            currentPage.value = it - 1
        }
    }

}