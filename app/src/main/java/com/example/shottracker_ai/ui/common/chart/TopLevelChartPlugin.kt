package com.example.shottracker_ai.ui.common.chart

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import androidx.core.content.ContextCompat
import com.example.shottracker_ai.R
import timber.log.Timber

class TopLevelChartPlugin(override val chart: LinearChart) : LinearChartPlugin {

    private val dashDistance = context.resources.getDimension(R.dimen.chart_line_dash_distance)

    private val levelLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.gray_300)
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(dashDistance, dashDistance), 0F)
        strokeWidth = context.resources.getDimension(R.dimen.chart_line_stroke)
    }

    private val levelLinePath = Path()

    override fun onCalculatePaths() {
        with(chart) {
            levelLinePath.reset()
            // for dashed line we take position of first data point or middle if there's no data
            if (chartPoints.isNotEmpty()) {
                chartPoints.minByOrNull { it.y }?.let { minPoint ->
                    val dashedLineY = minPoint.y
                    levelLinePath.moveTo(0f, dashedLineY)
                    levelLinePath.lineTo(minPoint.x, dashedLineY)
                }
            }
        }
    }

    override val requiredTopArea: Float
        get() = context.resources.getDimension(R.dimen.top_level_chart_plugin_padding)

    override fun drawBeforeChart(canvas: Canvas) {
        canvas.drawPath(levelLinePath, levelLinePaint)
    }
}