package co.condorlabs.customcomponents.graphics.barsgraph

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import co.condorlabs.customcomponents.DEFAULT_STYLE_ATTR
import co.condorlabs.customcomponents.DEFAULT_STYLE_RES
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.graphics.barsgraph.defaultgraphobjs.defaultStackedBarsGraphConfigObj
import co.condorlabs.customcomponents.graphics.barsgraph.models.stackedbarsgraph.StackedBarsGraphConfig

/**
 * Created by E Duque on 3/30/20.
 * Condor Labs
 * eduque@condorlabs.io
 */
class StackedBarsGraph @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val density = resources.displayMetrics.density
    private var stackedBarsGraphConfig: StackedBarsGraphConfig? = null
    private var numbersTextSize = 0F
    private var labelsTextSize = 0F
    private var horizontalLinesStrokeWidth = 0F
    private var barsStrokeWidth = 0F
    private var barsMargin = 0F
    private var numberOfHorizontalLines = 0
    private var horizontalLinesSpacingInNumbers = 0
    private var numberOfBars = 0
    private var rangeValue = 0
    private val widthPercentageToDrawLinesAndBars = 80F
    private var barsWithTheSameColor = false
    private var barsColor = NO_ID
    private var horizontalLinesPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        isDither = true
    }
    private var barsPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        isDither = true
    }
    private var numbersPaint = Paint().apply {
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        isDither = true
        textAlign = Paint.Align.RIGHT
    }
    private var labelsPaint = Paint().apply {
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        isDither = true
        textAlign = Paint.Align.CENTER
    }

    // DEFAULT ATTRIBUTES
    private val defaultTextColor = Color.parseColor("#AFAFAF")
    private val defaultHorizontalLinesColor = Color.parseColor("#EAECEC")
    private val defaultTextSize = 12F * density
    private val defaultHorizontalLinesStrokeWidth = 1F * density
    private val defaultBarsStrokeWidth = 20F * density
    private val defaultBarsMargin = 16F * density
    private val defaultBarsColor = Color.GRAY
    private val stackedBarsGraphConfigOfExample = defaultStackedBarsGraphConfigObj

    init {
        attrs?.let { setupAttrs(it) }
    }

    private fun setupAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.StackedBarsGraph,
            DEFAULT_STYLE_ATTR,
            DEFAULT_STYLE_RES
        )
        numbersTextSize =
            typedArray.getDimension(
                R.styleable.StackedBarsGraph_stackedBarsGraphNumbersTextSize,
                defaultTextSize
            )
        labelsTextSize =
            typedArray.getDimension(
                R.styleable.StackedBarsGraph_stackedBarsGraphLabelsTextSize,
                defaultTextSize
            )
        horizontalLinesStrokeWidth = typedArray.getDimension(
            R.styleable.StackedBarsGraph_stackedBarsGraphHorizontalLinesStrokeWidth,
            defaultHorizontalLinesStrokeWidth
        )
        barsStrokeWidth = typedArray.getDimension(
            R.styleable.StackedBarsGraph_stackedBarsGraphBarsStrokeWidth,
            defaultBarsStrokeWidth
        )
        barsMargin =
            typedArray.getDimension(
                R.styleable.StackedBarsGraph_stackedBarsGraphBarsMargin,
                defaultBarsMargin
            )
        barsWithTheSameColor =
            typedArray.getBoolean(R.styleable.StackedBarsGraph_stackedBarsWithTheSameColor, false)
        barsColor =
            typedArray.getColor(R.styleable.StackedBarsGraph_stackedBarsColor, defaultBarsColor)
        val numbersColor =
            typedArray.getColor(
                R.styleable.StackedBarsGraph_stackedBarsGraphNumbersColor,
                defaultTextColor
            )
        val labelsColor =
            typedArray.getColor(
                R.styleable.StackedBarsGraph_stackedBarsGraphLabelsColor,
                defaultTextColor
            )
        val horizontalLinesColor = typedArray.getColor(
            R.styleable.StackedBarsGraph_stackedBarsGraphHorizontalLinesColor,
            defaultHorizontalLinesColor
        )
        typedArray.recycle()

        numbersPaint.run { textSize = numbersTextSize; color = numbersColor }
        labelsPaint.run { textSize = labelsTextSize; color = labelsColor }
        horizontalLinesPaint.run {
            strokeWidth = horizontalLinesStrokeWidth; color = horizontalLinesColor
        }
        barsPaint.strokeWidth = barsStrokeWidth
        setupConfig(stackedBarsGraphConfigOfExample)
    }

    fun setupConfig(stackedBarsGraphConfig: StackedBarsGraphConfig) {
        with(stackedBarsGraphConfig) {
            this@StackedBarsGraph.stackedBarsGraphConfig = stackedBarsGraphConfig
            val maxValueOfTheSumOfBarsValues =
                stackedBars.map { bar ->
                    bar.data.sumBy { it.value }
                        .toFloat()
                }
                    .max() ?: 0F
            numberOfHorizontalLines =
                if (horizontalLines < 0) {
                    0
                } else {
                    if (horizontalLines > 10) {
                        10
                    } else {
                        horizontalLines
                    }
                }
            numberOfBars = stackedBars.size
            horizontalLinesSpacingInNumbers =
                kotlin.math.ceil((maxValueOfTheSumOfBarsValues / (numberOfHorizontalLines)))
                    .toInt()
            rangeValue = horizontalLinesSpacingInNumbers * (numberOfHorizontalLines)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val viewMeasuredWidth = this.measuredWidth
        val viewMeasuredHeight = this.measuredHeight
        val horizontalLinePositionY =
            getPixelsValueWithPercentage(viewMeasuredHeight, widthPercentageToDrawLinesAndBars)
        val horizontalLinesMarginWithParenTop =
            getPixelsValueWithPercentage(viewMeasuredHeight, 10F)
        val horizontalLinesSpacingInPixels =
            (horizontalLinePositionY - horizontalLinesMarginWithParenTop) / numberOfHorizontalLines

        drawHorizontalLines(
            viewMeasuredWidth,
            horizontalLinePositionY,
            horizontalLinesSpacingInPixels,
            canvas
        )

        stackedBarsGraphConfig?.let { stackedBarsGraphConfig ->
            drawBars(
                viewMeasuredWidth,
                viewMeasuredHeight,
                horizontalLinesSpacingInPixels,
                stackedBarsGraphConfig,
                canvas
            )
        }

        super.onDraw(canvas)
    }

    private fun drawHorizontalLines(
        viewMeasuredWidth: Int,
        startY: Float,
        horizontalLinesSpacingInPixels: Float,
        canvas: Canvas
    ) {
        var horizontalLinePositionY = startY
        val horizontalLineStartPositionX = getPixelsValueWithPercentage(viewMeasuredWidth, 10F)
        val horizontalLineEndPositionX = getPixelsValueWithPercentage(viewMeasuredWidth, 90F)
        val numberToDrawPositionX = getPixelsValueWithPercentage(viewMeasuredWidth, 8F)
        val numberToDrawMarginOffset = (numbersTextSize / 3)
        var horizontalLinesCount = 0
        var numberToDraw = 0

        do {
            canvas.drawLine(
                horizontalLineStartPositionX, horizontalLinePositionY,
                horizontalLineEndPositionX, horizontalLinePositionY,
                horizontalLinesPaint
            )

            canvas.drawText(
                numberToDraw.toString(), numberToDrawPositionX,
                horizontalLinePositionY + numberToDrawMarginOffset,
                numbersPaint
            )

            horizontalLinePositionY -= horizontalLinesSpacingInPixels
            numberToDraw += horizontalLinesSpacingInNumbers
            horizontalLinesCount++
        } while (horizontalLinesCount <= numberOfHorizontalLines)
    }

    private fun drawBars(
        viewMeasuredWidth: Int,
        viewMeasuredHeight: Int,
        horizontalLinesSpacingInPixels: Float,
        stackedBarsGraphConfig: StackedBarsGraphConfig,
        canvas: Canvas
    ) {
        val pixelRangeToDrawBars = horizontalLinesSpacingInPixels * (numberOfHorizontalLines)
        val strokeWidthPercentageValueOfBars =
            ((barsStrokeWidth + barsMargin) * 100) / viewMeasuredWidth
        val barsSpacingInPixels = getPixelsValueWithPercentage(
            viewMeasuredWidth,
            widthPercentageToDrawLinesAndBars - strokeWidthPercentageValueOfBars
        ) / (numberOfBars - 1)
        var barPositionX = getPixelsValueWithPercentage(viewMeasuredWidth, 10F)
        val barMarginOffset = (barsStrokeWidth + barsMargin) / 2
        val labelsPositionY = getPixelsValueWithPercentage(viewMeasuredHeight, 90F)
        val barStartPositionX =
            getPixelsValueWithPercentage(viewMeasuredHeight, widthPercentageToDrawLinesAndBars)

        stackedBarsGraphConfig.stackedBars.forEach { bar ->
            val totalSumOfBarSections = bar.data.sumBy { it.value }
            var incrementalSumOfBarSections = 0
            val barPositionXWithOffset = barPositionX + barMarginOffset

            bar.data.forEach { barSection ->
                val barPercentageToDraw =
                    getBarHeightPercentage(
                        totalSumOfBarSections.toFloat() - incrementalSumOfBarSections
                    )
                val pixelsToSubtractFromY = getPixelsValueWithPercentage(
                    pixelRangeToDrawBars.toInt(),
                    barPercentageToDraw
                )
                val barStartPositionY = barStartPositionX - pixelsToSubtractFromY

                canvas.drawLine(
                    barPositionXWithOffset,
                    barStartPositionX,
                    barPositionXWithOffset,
                    barStartPositionY,
                    barsPaint.apply {
                        color = if (barsWithTheSameColor) barsColor else barSection.color
                    }
                )

                incrementalSumOfBarSections += barSection.value
            }

            canvas.drawText(
                bar.label ?: "",
                barPositionXWithOffset,
                labelsPositionY,
                labelsPaint
            )

            barPositionX += barsSpacingInPixels
        }

    }

    private fun getBarHeightPercentage(barValue: Float): Float {
        return (barValue * 100F) / rangeValue
    }

    private fun getPixelsValueWithPercentage(
        valor: Int,
        percentage: Float
    ): Float {
        return (valor * percentage) / 100
    }
}
