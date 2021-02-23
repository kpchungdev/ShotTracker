package com.example.shottracker_ai.ui.home.ui

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.shottracker_ai.MainApplication
import com.example.shottracker_ai.R

data class AverageStateSection(val iconRes: Int,
                               val title: String,
                               val stat: String,
                               @ColorRes val tintComparisonRes: Int,
                               val lastComparison: String) {

    constructor(statType: StatType,
                currentAverage: Double,
                lastAverageDiff: Double
    ) : this(
            iconRes = statType.drawableRes,
            title = getString(statType.titleRes),
            stat = when (statType) {
                StatType.AVERAGE_FIELD_GOAL -> "${"%.2f".format(currentAverage)}${getString(statType.unitRes)}"
                else -> "$currentAverage"
            },
            tintComparisonRes = when {
                lastAverageDiff == 0.toDouble() -> R.color.gray_500
                lastAverageDiff > 0 -> R.color.green_500
                else -> R.color.red_500
            },
            lastComparison = when (statType) {
                StatType.AVERAGE_FIELD_GOAL -> "${"%.2f".format(lastAverageDiff)}%"
                else -> "$lastAverageDiff${getString(statType.unitRes)}"
            }
    )

    companion object {
        private fun getString(@StringRes stringRes: Int): String {
            return MainApplication.appContext.getString(stringRes)
        }
    }

}