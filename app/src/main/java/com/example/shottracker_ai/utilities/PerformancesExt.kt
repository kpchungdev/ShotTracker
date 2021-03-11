package com.example.shottracker_ai.utilities

import com.example.shottracker_ai.data.performance.Performance
import com.example.shottracker_ai.ui.home.stats.StatRange

fun List<Performance>.toRange(statRange: StatRange) =
        when (statRange) {
            StatRange.ALL -> this
            StatRange.DAY_AGO -> this.subList(0, this.lastIndex)
            StatRange.WEEK -> this.takeLast(7)
            StatRange.MONTH -> this.takeLast(30)
            StatRange.YEAR -> this.takeLast(365)
        }
