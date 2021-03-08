package com.example.shottracker_ai.ui.common.chart

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import androidx.core.content.ContextCompat
import com.example.shottracker_ai.R

class TopLevelChartPlugin(override val chart: LinearChart) : LinearChartPlugin {

    private val levelLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.gray_300)
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0F)
        strokeWidth = 1F
    }

    private val levelLinePath = Path()


    override fun onCalculatePaths() {
        with(chart) {
            levelLinePath.reset()
            // for dashed line we take position of first data point or middle if there's no data
            if (normalizedChartData.isNotEmpty()) {
                val maxValue = chartData.maxByOrNull { it.value }
                val lastMaxValueIndex = chartData.indexOfLast { it.value == maxValue?.value }

                val dashedLineY = chartY(lastMaxValueIndex)
                levelLinePath.moveTo(0f, dashedLineY)
                levelLinePath.lineTo(chart.width.toFloat(), dashedLineY)
            }
        }
    }

    override val requiredTopArea: Float
        get() = 5F

    override fun drawBeforeChart(canvas: Canvas) {
        canvas.drawPath(levelLinePath, levelLinePaint)
    }
}