package co.condorlabs.customcomponents.graphics.barsgraph

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import co.condorlabs.customcomponents.DEFAULT_STYLE_ATTR
import co.condorlabs.customcomponents.DEFAULT_STYLE_RES
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.graphics.barsgraph.defaultgraphobjs.defaultBarsGraphConfigObj
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

    private val density = resources.displayMetrics.density
    private var barsGraphConfig: BarsGraphConfig? = null
    private var numbersTextSize = 0F
    private var labelsTextSize = 0F
    private var horizontalLinesStrokeWidth = 0F
    private var barsStrokeWidth = 0F
    private var barsMargin = 0F
    private var barsStrokeWidthMiddle = 0F
    private var barsWithTheSameColor = false
    private var barsColor = NO_ID
    private var barsStrokeWidthColor = NO_ID
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
    private val widthPercentageToDrawLinesAndBars = 100F
    private val barsBorderStrokeWith = 2F * density
    private val barsBorderCornerRadius = 5F * density
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
    private val defaultHorizontalLinesColor = Color.parseColor("#EAECEC")
    private val defaultLabelTextSize = 18F * density
    private val defaultCountTextSize = 38F * density
    private val defaultHorizontalLinesStrokeWidth = 1F * density
    private val defaultBarsStrokeWidth = 80F * density
    private val defaultBarsMargin = 16F * density
    private val defaultBarsColor = Color.GRAY
    private val defaultBarsStrokeWidthColor = Color.DKGRAY
    private val barsGraphConfigOfExample = defaultBarsGraphConfigObj

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
        barsWithTheSameColor =
            typedArray.getBoolean(R.styleable.BarsGraph_barsWithTheSameColor, false)
        barsColor =
            typedArray.getColor(R.styleable.BarsGraph_barsColor, defaultBarsColor)
        barsStrokeWidthColor =
            typedArray.getColor(
                R.styleable.BarsGraph_barsStrokeWidthColor,
                defaultBarsStrokeWidthColor
            )
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
                if (horizontalLines < 0) {
                    0
                } else {
                    if (horizontalLines > 10) {
                        10
                    } else {
                        horizontalLines
                    }
                }
            val valueOfTheMostBigBar = bars.maxByOrNull { it.value }?.value?.toFloat() ?: 0F
            val horizontalLinesSpacingInNumbers =
                kotlin.math.ceil((valueOfTheMostBigBar / (numberOfHorizontalLines))).toInt()
            rangeValue = horizontalLinesSpacingInNumbers * (numberOfHorizontalLines)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val viewMeasuredWidth = this.measuredWidth
        val viewMeasuredHeight = this.measuredHeight
        val horizontalLinePositionY = getPixelsValueWithPercentage(viewMeasuredHeight, 50F)

        drawHorizontalLine(horizontalLinePositionY, viewMeasuredWidth, canvas)

        barsGraphConfig?.let { barsGraphConfig ->
            drawBars(
                viewMeasuredWidth,
                viewMeasuredHeight,
                horizontalLinePositionY,
                barsGraphConfig,
                canvas
            )
        }

        super.onDraw(canvas)
    }

    private fun drawHorizontalLine(startY: Float, viewMeasuredWidth: Int, canvas: Canvas) {
        val startX = getPixelsValueWithPercentage(viewMeasuredWidth, 0F)
        val endX = getPixelsValueWithPercentage(viewMeasuredWidth, 100F)
        canvas.drawLine(startX, startY, endX, startY, horizontalLinesPaint)
    }

    private fun drawBars(
        viewMeasuredWidth: Int,
        viewMeasuredHeight: Int,
        horizontalLinePositionY: Float,
        barsGraphConfig: BarsGraphConfig,
        canvas: Canvas
    ) {
        val horizontalLinesMarginWithParenTop =
            getPixelsValueWithPercentage(viewMeasuredHeight, 0F)
        val horizontalLinesSpacingInPixels =
            (horizontalLinePositionY - horizontalLinesMarginWithParenTop) / numberOfHorizontalLines

        barsStrokeWidthMiddle = barsStrokeWidth / 2
        val pixelRangeToDrawBars = horizontalLinesSpacingInPixels * (numberOfHorizontalLines)
        val strokeWidthPercentageValueOfBars =
            ((barsStrokeWidth + barsMargin) * 100) / viewMeasuredWidth
        val barsSpacingInPixels = getPixelsValueWithPercentage(
            viewMeasuredWidth,
            widthPercentageToDrawLinesAndBars - strokeWidthPercentageValueOfBars
        ) / (numberOfBars - 1)
        var barPositionX = getPixelsValueWithPercentage(viewMeasuredWidth, 0F)
        val barMarginOffset = (barsStrokeWidth + barsMargin) / 2
        val labelsPositionY = getPixelsValueWithPercentage(viewMeasuredHeight, 60F)
        val countLabelsPositionY = getPixelsValueWithPercentage(viewMeasuredHeight, 85F)
        val barStartPositionX = getPixelsValueWithPercentage(viewMeasuredHeight, 50F)

        barsGraphConfig.bars.forEach { bar ->
            val totalSumOfBarSections = bar.value
            val barPositionXWithOffset = barPositionX + barMarginOffset
            val barPercentageToDraw = getBarHeightPercentage(totalSumOfBarSections.toFloat())
            val pixelsToSubtractFromY =
                getPixelsValueWithPercentage(pixelRangeToDrawBars.toInt(), barPercentageToDraw)
            val barStartPositionY = barStartPositionX - pixelsToSubtractFromY

            bar.left = barPositionXWithOffset - barsStrokeWidthMiddle
            bar.top = barStartPositionY
            bar.right = barPositionXWithOffset + barsStrokeWidthMiddle
            bar.bottom = countLabelsPositionY
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
                barsPaint.apply {
                    color =
                        if (barsWithTheSameColor) barsStrokeWidthColor else bar.strokeColor
                }
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
                barsPaint.apply { color = if (barsWithTheSameColor) barsColor else bar.fillColor }
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
                countLabelsPaint.apply {
                    color = if (barsWithTheSameColor) barsStrokeWidthColor else bar.strokeColor
                }
            )

            barPositionX += barsSpacingInPixels
        }
    }

    private fun getBarHeightPercentage(barValue: Float): Float {
        return (barValue * 100F) / rangeValue
    }

    private fun getPixelsValueWithPercentage(value: Int, percentage: Float): Float {
        return (value * percentage) / 100F
    }

    fun setOnBarClickListener(callback: (bar: Bar) -> Unit) {
        setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                barsGraphConfig?.bars?.firstOrNull {
                    it.contains(
                        motionEvent.x,
                        motionEvent.y
                    )
                }?.let(callback)
            }
            true
        }
    }
}
