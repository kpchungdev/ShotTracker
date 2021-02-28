package com.example.shottracker_ai.ui.home.ui

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.shottracker_ai.MainApplication
import com.example.shottracker_ai.R

data class AverageStatSection(val iconRes: Int,
                               val title: String,
                               val stat: String,
                               @ColorRes val tintComparisonRes: Int,
                               val lastComparison: String) {

    constructor(statType: StatType,
                targetAverage: Double,
                comparisonAverage: Double
    ) : this(
            iconRes = statType.drawableRes,
            title = getString(statType.titleRes),
            stat = when (statType) {
                StatType.AVERAGE_FIELD_GOAL -> "${"%.2f".format(targetAverage)}${getString(statType.unitRes)}"
                else -> "$targetAverage"
            },
            tintComparisonRes = when {
                comparisonAverage == 0.toDouble() -> R.color.gray_500
                comparisonAverage > 0 -> R.color.green_500
                else -> R.color.red_500
            },
            lastComparison = when (statType) {
                StatType.AVERAGE_FIELD_GOAL -> "${"%.2f".format(comparisonAverage)}%"
                else -> "$comparisonAverage${getString(statType.unitRes)}"
            }
    )

    companion object {
        private fun getString(@StringRes stringRes: Int): String {
            return MainApplication.appContext.getString(stringRes)
        }
    }

}