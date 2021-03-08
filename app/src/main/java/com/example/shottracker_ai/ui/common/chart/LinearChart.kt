package com.example.shottracker_ai.ui.common.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.example.shottracker_ai.R

data class ChartData(val value: Float)

interface LinearChartPlugin {

    val chart: LinearChart

    val context get() = chart.context
    val resources get() = chart.resources


    // if plugin requires any space on top
    val requiredTopArea: Float get() = 0f

    fun onCalculatePaths() {}
    fun drawBeforeChart(canvas: Canvas) {}
    fun drawAfterChart(canvas: Canvas) {}

}

class LinearChart @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val plugins = mutableListOf<LinearChartPlugin>()

    var lineWidth: Float = 0f
    var lineColor: Int = -1
        set(value) {
            field = value
            chartPaint = createChartPaint()
            invalidate()
        }

    init {
        context.withStyledAttributes(set = attrs, attrs = R.styleable.LinearChart) {
            lineColor = getColor(R.styleable.LinearChart_lineColor, ContextCompat.getColor(context, R.color.orange_500))
            lineWidth = getDimensionPixelSize(R.styleable.LinearChart_lineWidth, 2).toFloat()
        }
    }

    var normalizedChartData: List<ChartData> = emptyList()

    var chartData: List<ChartData> = emptyList()
        set(value) {
            field = value
            val maxValue = field.maxByOrNull { it.value }?.value ?: 0.0F
            val minValue = field.minByOrNull { it.value }?.value ?: 0.0F

            val fullRange: Float = maxValue.takeIf { maxValue == minValue } ?: maxValue - minValue

            normalizedChartData = value.map { ChartData((maxValue - it.value) / fullRange) }
            calculatePaths()
            invalidate()
        }

    private fun createChartPaint() = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = lineColor
        style = Paint.Style.STROKE
        strokeWidth = lineWidth
    }

    private var chartPaint = createChartPaint()

    private val chartPath = Path()

    val maxChartHeight get() = height.toFloat() - paddingBottom - chartTopPoint
    open val chartTopPoint get() = paddingTop.toFloat() + plugins.sumByDouble { it.requiredTopArea.toDouble() }.toFloat()

    val segmentDistance get() = width.toFloat() / (normalizedChartData.size - 1)

    fun chartY(segment: Int, maxHeight: Float = this.maxChartHeight) = normalizedChartData[segment].value * maxHeight



    protected open fun calculatePaths() {
        when {
            normalizedChartData.size == 1 -> {
                val maxHeight = this.maxChartHeight / 2
                val oneDayDistance = this.segmentDistance
                chartPath.reset()

                val y = chartY(0, maxHeight)
                chartPath.moveTo(0f, maxChartHeight)
                chartPath.lineTo(normalizedChartData[0].value * oneDayDistance, y)
            }
            normalizedChartData.isNotEmpty() -> {
                val maxHeight = this.maxChartHeight
                val oneDayDistance = this.segmentDistance
                chartPath.reset()
                chartPath.moveTo(0f, chartY(0))
                (1 until normalizedChartData.size).forEach { segment -> chartPath.lineTo(segment * oneDayDistance, chartY(segment, maxHeight)) }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            calculatePaths()
        }
    }

    override fun onDraw(canvas: Canvas) {
        plugins.forEach { it.drawBeforeChart(canvas) }
        canvas.drawPath(chartPath, chartPaint)
        plugins.forEach { it.drawAfterChart(canvas) }
    }

}