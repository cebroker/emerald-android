package co.condorlabs.customcomponents.graphics.circlegraph

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import co.condorlabs.customcomponents.graphics.circlegraph.model.CircleGraphConfig

/**
 * Created by E Duque on 4/16/20.
 * Condor Labs
 * eduque@condorlabs.io
 */
class CircleGraph @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val displayDensity = resources.displayMetrics.density
    private val borderLineStrokeWidth = 1 * displayDensity
    private val circleGraphPaint = Paint().apply { isAntiAlias = true }
    private var startAngle = 0F
    private var sectionValuesSum = 0
    private var sectionsAngleSeparation = 5F
    private val borderRectF = RectF()
    private val fillRectF = RectF()
    private var circleGraphConfig: CircleGraphConfig? = null

    fun setCircleGraphConfig(circleGraphConfig: CircleGraphConfig) {
        this.circleGraphConfig = circleGraphConfig
        calculateCircleProperties()
        invalidate()
    }

    private fun calculateCircleProperties() {
        circleGraphConfig?.let { circleGraphConfig ->
            sectionValuesSum = circleGraphConfig.circleSections.sumBy { it.value }
            startAngle = when (circleGraphConfig.circleSections.size) {
                1 -> 0F
                2 -> 90F
                3 -> 90F
                else -> 0F
            }
            if (circleGraphConfig.circleSections.size <= 1) {
                sectionsAngleSeparation = 0F
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        borderRectF.set(0F, 0F, width.toFloat(), height.toFloat())
        fillRectF.set(
            0F + borderLineStrokeWidth,
            0F + borderLineStrokeWidth,
            width.toFloat() - borderLineStrokeWidth,
            height.toFloat() - borderLineStrokeWidth
        )
        circleGraphConfig?.let {
            drawCircleGraph(it, canvas)
        } ?: drawEmptyCircleGraph(canvas)
    }

    private fun drawCircleGraph(circleGraphConfig: CircleGraphConfig, canvas: Canvas) {
        if (circleGraphConfig.circleSections.isNotEmpty()) {
            circleGraphConfig.circleSections.forEach {
                val borderColor = ContextCompat.getColor(context, it.borderColor)
                val fillColor = ContextCompat.getColor(context, it.fillColor)
                val sectionSweepAngle =
                    getSectionSweepAngleWithValue(it.value) - sectionsAngleSeparation

                canvas.drawArc(
                    borderRectF,
                    startAngle,
                    sectionSweepAngle,
                    true,
                    circleGraphPaint.apply {
                        color = borderColor
                    })
                canvas.drawArc(
                    fillRectF,
                    startAngle,
                    sectionSweepAngle,
                    true,
                    circleGraphPaint.apply {
                        color = fillColor
                    })

                startAngle += sectionSweepAngle + sectionsAngleSeparation
            }
        }
    }

    private fun drawEmptyCircleGraph(canvas: Canvas) {
        canvas.drawArc(
            borderRectF,
            0F,
            360F,
            true,
            circleGraphPaint.apply {
                color = Color.DKGRAY
            })
        canvas.drawArc(
            fillRectF,
            0F,
            360F,
            true,
            circleGraphPaint.apply { color = Color.GRAY })
    }

    private fun getSectionSweepAngleWithValue(value: Int): Float {
        return (360F * ((value * 100F) / sectionValuesSum)) / 100F
    }
}
