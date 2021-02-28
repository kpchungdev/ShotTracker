package com.example.shottracker_ai.utilities

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.shottracker_ai.data.performance.Performance
import com.example.shottracker_ai.ui.home.ui.StatRange
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
@SmallTest
class PerformanceExtTest {

    val performances = mutableListOf<Performance>().also { list ->
        for (i in 1..400) {
            list.add(Performance(LocalDateTime.now(), 5, 10, 40, .50))
        }
    }

    @Test
    fun testRangeAll() {
        assertEquals(400, performances.toRange(StatRange.ALL).size)
    }

    @Test
    fun testRangeWeek() {
        assertEquals(7, performances.toRange(StatRange.WEEK).size)
    }

    @Test
    fun testRangeMonth() {
        assertEquals(30, performances.toRange(StatRange.MONTH).size)
    }

    @Test
    fun testRangeYear() {
        assertEquals(365, performances.toRange(StatRange.YEAR).size)
    }

}