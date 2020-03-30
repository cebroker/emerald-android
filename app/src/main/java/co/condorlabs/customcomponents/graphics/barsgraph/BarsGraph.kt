package co.condorlabs.customcomponents.graphics.barsgraph

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import co.condorlabs.customcomponents.DEFAULT_STYLE_ATTR
import co.condorlabs.customcomponents.DEFAULT_STYLE_RES
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.graphics.barsgraph.models.barsgraph.Bar
import co.condorlabs.customcomponents.graphics.barsgraph.models.barsgraph.BarsGraphConfig

/**
 * Created by E Duque on 3/30/20.
 * Condor Labs
 * eduque@condorlabs.io
 */
class BarsGraph @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var barsGraphConfig: BarsGraphConfig? = null
    private var numbersTextSize = 0F
    private var labelsTextSize = 0F
    private var horizontalLinesStrokeWidth = 0F
    private var barsStrokeWidth = 0F
    private var barsMargin = 0F
    private var barsStrokeWidthMiddle = 0F
    private var horizontalLinesPaint = Paint().apply {
        strokeWidth = horizontalLinesStrokeWidth
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        isDither = true
    }
    private var barsPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        isDither = true
    }
    private var labelsPaint = Paint().apply {
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        isDither = true
        textSize = labelsTextSize
        textAlign = Paint.Align.CENTER
    }
    private var countLabelsPaint = Paint().apply {
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        isDither = true
        textAlign = Paint.Align.CENTER
    }
    private var numberOfHorizontalLines = 1
    private var numberOfBars = 0
    private var rangeValue = 0
    private val widthPercentageToDrawLinesAndBars = 80F
    private val barsBorderStrokeWith = 8F
    private val barsBorderCornerRadius = 20F
    private val internalBarsBorderCornerRadius = barsBorderCornerRadius - barsBorderStrokeWith
    private val corners = floatArrayOf(
        barsBorderCornerRadius, barsBorderCornerRadius,
        barsBorderCornerRadius, barsBorderCornerRadius,
        0F, 0F,
        0F, 0F
    )
    private val cornersTwo = floatArrayOf(
        internalBarsBorderCornerRadius, internalBarsBorderCornerRadius,
        internalBarsBorderCornerRadius, internalBarsBorderCornerRadius,
        0F, 0F,
        0F, 0F
    )

    // DEFAULT ATTRIBUTES
    private val defaultHorizontalLinesColor = Color.parseColor("#99c9c9c9")
    private val defaultLabelTextSize = 42F
    private val defaultCountTextSize = 92F
    private val defaultHorizontalLinesStrokeWidth = 2F
    private val defaultBarsStrokeWidth = 200F
    private val defaultBarsMargin = 50F
    private val barsGraphConfigOfExample = BarsGraphConfig(
        1,
        listOf(
            Bar("ONE", 38, Color.GRAY, Color.DKGRAY),
            Bar("TWO", 10, Color.DKGRAY, Color.BLUE)
        )
    )

    init {
        attrs?.let { setupAttrs(it) }
    }

    private fun setupAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BarsGraph,
            DEFAULT_STYLE_ATTR,
            DEFAULT_STYLE_RES
        )
        numbersTextSize =
            typedArray.getDimension(
                R.styleable.BarsGraph_barsGraphNumbersTextSize,
                defaultCountTextSize
            )
        labelsTextSize =
            typedArray.getDimension(
                R.styleable.BarsGraph_barsGraphLabelsTextSize,
                defaultLabelTextSize
            )
        horizontalLinesStrokeWidth = typedArray.getDimension(
            R.styleable.BarsGraph_barsGraphHorizontalLinesStrokeWidth,
            defaultHorizontalLinesStrokeWidth
        )
        barsStrokeWidth = typedArray.getDimension(
            R.styleable.BarsGraph_barsGraphBarsStrokeWidth,
            defaultBarsStrokeWidth
        )
        barsMargin =
            typedArray.getDimension(R.styleable.BarsGraph_barsGraphBarsMargin, defaultBarsMargin)
        if (typedArray.getBoolean(R.styleable.BarsGraph_barsGraphNumbersBold, false)) {
            countLabelsPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val labelsColor =
            typedArray.getColor(R.styleable.BarsGraph_barsGraphLabelsColor, Color.BLACK)
        val horizontalLinesColor = typedArray.getColor(
            R.styleable.BarsGraph_barsGraphHorizontalLinesColor,
            defaultHorizontalLinesColor
        )
        typedArray.recycle()

        countLabelsPaint.textSize = numbersTextSize
        labelsPaint.run { textSize = labelsTextSize; color = labelsColor }
        horizontalLinesPaint.run {
            strokeWidth = horizontalLinesStrokeWidth; color = horizontalLinesColor
        }
        barsPaint.strokeWidth = barsStrokeWidth
        setupConfig(barsGraphConfigOfExample)
    }

    fun setupConfig(barsGraphConfig: BarsGraphConfig) {
        with(barsGraphConfig) {
            this@BarsGraph.barsGraphConfig = barsGraphConfig

            numberOfBars = bars.size
            numberOfHorizontalLines =
                if (horizontalLines < 0) 0 else if (horizontalLines > 10) 10 else horizontalLines
            val valueOfTheMostBigBar = bars.maxBy { it.value }?.value?.toFloat() ?: 0F
            val horizontalLinesSpacingInNumbers =
                kotlin.math.ceil((valueOfTheMostBigBar / (numberOfHorizontalLines))).toInt()
            rangeValue = horizontalLinesSpacingInNumbers * (numberOfHorizontalLines)
        }
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val viewMeasuredWidth = this.measuredWidth
        val viewMeasuredHeight = this.measuredHeight

        // DRAW HORIZONTAL LINE
        val horizontalLinePositionY = getPixelsValueWithPercentage(viewMeasuredHeight, 50F)
        val horizontalLinesMarginWithParenTop =
            getPixelsValueWithPercentage(viewMeasuredHeight, 10F)
        val horizontalLinesSpacingInPixels =
            (horizontalLinePositionY - horizontalLinesMarginWithParenTop) / numberOfHorizontalLines
        val horizontalLineStartPositionX = getPixelsValueWithPercentage(viewMeasuredWidth, 10F)
        val horizontalLineEndPositionX = getPixelsValueWithPercentage(viewMeasuredWidth, 90F)

        canvas.drawLine(
            horizontalLineStartPositionX,
            horizontalLinePositionY,
            horizontalLineEndPositionX,
            horizontalLinePositionY,
            horizontalLinesPaint
        )

        // DRAW BARS
        barsGraphConfig?.let { chartConfig ->
            barsStrokeWidthMiddle = barsStrokeWidth / 2
            val pixelRangeToDrawBars = horizontalLinesSpacingInPixels * (numberOfHorizontalLines)
            val strokeWidthPercentageValueOfBars =
                ((barsStrokeWidth + barsMargin) * 100) / viewMeasuredWidth
            val barsSpacingInPixels = getPixelsValueWithPercentage(
                viewMeasuredWidth,
                widthPercentageToDrawLinesAndBars - strokeWidthPercentageValueOfBars
            ) / (numberOfBars - 1)
            var barPositionX = getPixelsValueWithPercentage(viewMeasuredWidth, 10F)
            val barMarginOffset = (barsStrokeWidth + barsMargin) / 2
            val labelsPositionY = getPixelsValueWithPercentage(viewMeasuredHeight, 60F)
            val countLabelsPositionY = getPixelsValueWithPercentage(viewMeasuredHeight, 85F)
            val barStartPositionX = getPixelsValueWithPercentage(viewMeasuredHeight, 50F)

            chartConfig.bars.forEach { bar ->
                val totalSumOfBarSections = bar.value
                val barPositionXWithOffset = barPositionX + barMarginOffset
                val barPercentageToDraw = getBarHeightPercentage(totalSumOfBarSections.toFloat())
                val pixelsToSubtractFromY =
                    getPixelsValueWithPercentage(pixelRangeToDrawBars.toInt(), barPercentageToDraw)
                val barStartPositionY = barStartPositionX - pixelsToSubtractFromY

                canvas.drawPath(
                    Path().apply {
                        addRoundRect(
                            RectF(
                                barPositionXWithOffset - barsStrokeWidthMiddle,
                                barStartPositionY,
                                barPositionXWithOffset + barsStrokeWidthMiddle,
                                barStartPositionX
                            ), corners, Path.Direction.CW
                        )
                    },
                    barsPaint.apply { color = bar.strokeColor }
                )

                canvas.drawPath(
                    Path().apply {
                        addRoundRect(
                            RectF(
                                barPositionXWithOffset - barsStrokeWidthMiddle + barsBorderStrokeWith,
                                barStartPositionY + barsBorderStrokeWith,
                                barPositionXWithOffset + barsStrokeWidthMiddle - barsBorderStrokeWith,
                                barStartPositionX
                            ), cornersTwo, Path.Direction.CW
                        )
                    },
                    barsPaint.apply { color = bar.fillColor }
                )

                canvas.drawText(
                    bar.label ?: "",
                    barPositionXWithOffset,
                    labelsPositionY,
                    labelsPaint
                )

                canvas.drawText(
                    bar.value.toString(),
                    barPositionXWithOffset,
                    countLabelsPositionY,
                    countLabelsPaint.apply { color = bar.strokeColor }
                )

                barPositionX += barsSpacingInPixels
            }
        }

        super.onDraw(canvas)
    }

    private fun getBarHeightPercentage(barValue: Float): Float {
        return (barValue * 100F) / rangeValue
    }

    private fun getPixelsValueWithPercentage(valor: Int, percentage: Float): Float {
        return (valor * percentage) / 100F
    }
}
