package com.example.shottracker_ai.ui.home.ranges

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.transition.TransitionManager
import com.example.shottracker_ai.R
import com.example.shottracker_ai.data.performance.Performance
import com.example.shottracker_ai.databinding.SectionRangeBinding
import com.example.shottracker_ai.ui.home.stats.StatRange
import timber.log.Timber

interface EventHandler {
    fun changeRange(range: StatRange)
}

data class RangeSection(
        val selectedRange: StatRange,
        val enables: Map<StatRange, Boolean>,
        val showIndicator: Boolean
) {

    constructor(selectedRange: StatRange, performances: List<Performance>) : this(
            selectedRange = selectedRange,
            enables = mapOf<StatRange, Boolean>(
                    StatRange.ALL to performances.isNotEmpty(),
                    StatRange.WEEK to (performances.size >= 7),
                    StatRange.MONTH to (performances.size >= 30),
                    StatRange.YEAR to (performances.size >= 365)
            ),
            showIndicator = performances.isNotEmpty()
    )
}

fun SectionRangeBinding.setUp(
        fragment: Fragment,
        eventHandler: EventHandler,
        section: LiveData<RangeSection>
) {
    this.eventHandler = eventHandler

    val context = fragment.requireContext()

    val whiteColor = context.getColor(R.color.white)
    val orangeColor = context.getColor(R.color.orange_500)
    val grayColor = context.getColor(R.color.gray_300)

    val rangeViews = mapOf(
            StatRange.ALL to listOf(labelAll),
            StatRange.WEEK to listOf(layoutWeek, labelWeek, iconWeek),
            StatRange.MONTH to listOf(layoutMonthly, labelMonthly, iconMonthly),
            StatRange.YEAR to listOf(layoutYearly, labelYear, iconYearly)
    )

    section.observe(fragment) { section ->
        section.enables.forEach { statRange, enabled ->
//            Initiate colors
            rangeViews[statRange]?.let { views ->
                views.forEach { view ->
                    when (view) {
                        is TextView -> view.setTextColor(if (enabled) orangeColor else grayColor)
                        is ImageView -> view.imageTintList = ColorStateList.valueOf(if (enabled) orangeColor else grayColor)
                    }

                    view.isEnabled = enabled
                }
            }
        }

        //            Selector
        selector.isEnabled = section.showIndicator

//            Animate to selected range
        selectedLabelView(section.selectedRange)?.let { selectedRangeView ->
            val constraint = ConstraintSet().also {
                it.clone(layoutChartRanges)

                it.connect(R.id.selector, ConstraintSet.START, selectedRangeView, ConstraintSet.START)
                it.connect(R.id.selector, ConstraintSet.END, selectedRangeView, ConstraintSet.END)
                it.connect(R.id.selector, ConstraintSet.TOP, selectedRangeView, ConstraintSet.TOP)
                it.connect(R.id.selector, ConstraintSet.BOTTOM, selectedRangeView, ConstraintSet.BOTTOM)
            }

            TransitionManager.beginDelayedTransition(layoutChartRanges)
            constraint.applyTo(layoutChartRanges)

            //                Previous text color
            previousRangeViews(context).forEach { view ->
                when (view) {
                    is TextView -> view.setTextColor(orangeColor)
                    is ImageView -> view.imageTintList = ColorStateList.valueOf(orangeColor)
                }
            }

            //                After text color
            rangeViews[section.selectedRange]?.forEach { view ->
                when (view) {
                    is TextView -> view.setTextColor(whiteColor)
                    is ImageView -> view.imageTintList = ColorStateList.valueOf(whiteColor)
                }
            }

        }
    }
}

private fun selectedLabelView(selectedRange: StatRange) = when (selectedRange) {
    StatRange.ALL -> R.id.labelAll
    StatRange.WEEK -> R.id.layoutWeek
    StatRange.MONTH -> R.id.layoutMonthly
    StatRange.YEAR -> R.id.layoutYearly
    else -> null
}

private fun SectionRangeBinding.previousRangeViews(context: Context): List<View> {
    val whiteColor = context.getColor(R.color.white)

    return when {
        labelAll.currentTextColor == whiteColor -> listOf(labelAll)
        labelWeek.currentTextColor == whiteColor -> listOf(labelWeek, iconWeek)
        labelMonthly.currentTextColor == whiteColor -> listOf(labelMonthly, iconMonthly)
        labelYear.currentTextColor == whiteColor -> listOf(labelYear, iconYearly)
        else -> listOf()
    }
}