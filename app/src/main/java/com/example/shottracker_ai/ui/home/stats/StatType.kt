package com.example.shottracker_ai.ui.home.stats

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.shottracker_ai.R

enum class StatType {
    AVERAGE_FIELD_GOAL, AVERAGE_MADE_SHOTS, AVERAGE_MINUTES;

    val drawableRes: Int
        @DrawableRes
        get() = when (this) {
            AVERAGE_FIELD_GOAL -> R.drawable.ic_field_goal
            AVERAGE_MADE_SHOTS -> R.drawable.ic_made_shots
            AVERAGE_MINUTES -> R.drawable.ic_minutes
        }

    val titleRes: Int
        @StringRes
        get() = when (this) {
            AVERAGE_FIELD_GOAL -> R.string.average_field_goal
            AVERAGE_MADE_SHOTS -> R.string.average_makes
            AVERAGE_MINUTES -> R.string.average_minutes
        }

    val unitRes: Int
        @StringRes
        get() = when (this) {
            AVERAGE_FIELD_GOAL -> R.string.percent
            AVERAGE_MADE_SHOTS -> R.string.shots
            AVERAGE_MINUTES -> R.string.minutes
        }

}