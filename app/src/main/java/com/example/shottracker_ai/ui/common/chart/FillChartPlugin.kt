package com.example.shottracker_ai.ui.common.chart

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.core.content.ContextCompat
import com.example.shottracker_ai.R

class FillChartPlugin(override val chart: LinearChart) : LinearChartPlugin {

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.orange_300)
        style = Paint.Style.FILL
        strokeWidth = chart.lineWidth
    }

    private val fillChartPath = Path()

    override fun onCalculatePaths() {
        with(chart) {
            if (chartPoints.isNotEmpty()) {
                fillChartPath.reset()
                chartPoints.forEachIndexed { index, pointF ->
                    if (index == 0) fillChartPath.moveTo(pointF.x, pointF.y)
                    else fillChartPath.lineTo(pointF.x, pointF.y)
                }

                fillChartPath.lineTo(width.toFloat(), height.toFloat())
                fillChartPath.lineTo(0.toFloat(), height.toFloat())
                fillChartPath.lineTo(chartPoints[0].x, chartPoints[0].y)
            }
        }
    }

    override fun drawBeforeChart(canvas: Canvas) {
        canvas.drawPath(fillChartPath, fillPaint)
    }
}