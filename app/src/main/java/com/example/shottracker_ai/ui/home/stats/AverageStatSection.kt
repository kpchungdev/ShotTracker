package com.example.shottracker_ai.ui.home.stats

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.shottracker_ai.MainApplication
import com.example.shottracker_ai.R
import java.text.DecimalFormat

private val decimalFormat = DecimalFormat("#.#")

data class AverageStatSection(val iconRes: Int,
                              val title: String,
                              val stat: String,
                              @ColorRes val tintComparisonRes: Int,
                              val showComparison: Boolean,
                              val comparison: String,
                              val comparisonRes: Int,
                              val comparisonLabel: String) {

    constructor(statType: StatType,
                targetAverage: Double,
                showComparison: Boolean,
                comparisonRange: StatRange,
                comparisonDiff: Double
    ) : this(
            iconRes = statType.drawableRes,
            title = getString(statType.titleRes),
            stat = when (statType) {
                StatType.AVERAGE_FIELD_GOAL -> "${decimalFormat.format(targetAverage * 100)}${getString(statType.unitRes)}"
                else -> "${decimalFormat.format(targetAverage)}"
            },
            tintComparisonRes = when {
                comparisonDiff == 0.toDouble() -> R.color.gray_500
                comparisonDiff > 0 -> R.color.green_500
                else -> R.color.red_500
            },
            showComparison = showComparison,
            comparison = when (statType) {
                StatType.AVERAGE_FIELD_GOAL -> "${decimalFormat.format(comparisonDiff)}${getString(statType.unitRes)}"
                else -> "${decimalFormat.format(comparisonDiff)} ${getString(statType.unitRes)}"
            },
            comparisonRes = when {
                comparisonDiff < 0 -> R.drawable.ic_arrow_down
                else -> R.drawable.ic_arrow_up
            },
            comparisonLabel = when (comparisonRange) {
                StatRange.DAY -> getString(R.string.last_average)
                StatRange.ALL -> getString(R.string.total_average)
                else -> error("no comparison label for comparison range: $comparisonRange")
            }
    )

    companion object {
        private fun getString(@StringRes stringRes: Int): String {
            return MainApplication.appContext.getString(stringRes)
        }
    }

}