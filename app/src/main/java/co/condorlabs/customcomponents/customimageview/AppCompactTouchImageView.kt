package co.condorlabs.customcomponents.customimageview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Created by E Duque on 5/13/20.
 * Condor Labs
 * eduque@condorlabs.io
 */
class AppCompactTouchImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var currentScale = 1F
    private var defaultScale = 1F
    private var viewWidth = 0
    private var viewHeight = 0
    private var drawableWidth = 0
    private var drawableHeight = 0
    private var currentTranslationOnX = 0F
    private var currentTranslationOnY = 0F
    private var defaultTranslationOnX = 0F
    private var defaultTranslationOnY = 0F
    private var savedTranslationOnX = 0F
    private var savedTranslationOnY = 0F
    private var eventStartPoint = PointF()
    private var midpoint = PointF()
    private var mode = NONE
    private var lastClickTime = 0L
    private val intervalDoubleClick = 300
    private val animationsTime = 200L
    private val scaleGestureDetector: ScaleGestureDetector
    private val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            detector?.let { zoom(it) }
            return true
        }
    }

    init {
        isClickable = true
        scaleType = ScaleType.MATRIX
        scaleGestureDetector = ScaleGestureDetector(context, listener)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        setupInitialValues()
    }

    private fun setupInitialValues() {
        if (drawable == null) return

        drawableWidth = drawable.intrinsicWidth
        drawableHeight = drawable.intrinsicHeight
        viewWidth = measuredWidth
        viewHeight = measuredHeight
        defaultScale = getScaleFactor()
        defaultTranslationOnY = viewHeight / 2F - (drawableHeight * defaultScale) / 2F
        defaultTranslationOnX = viewWidth / 2F - (drawableWidth * defaultScale) / 2F
        currentScale = defaultScale
        currentTranslationOnX = defaultTranslationOnX
        currentTranslationOnY = defaultTranslationOnY

        updateImageMatrix()
    }

    private fun getScaleFactor(): Float {
        return if (drawableWidth <= viewWidth && drawableHeight <= viewHeight) {
            1F
        } else if (drawableWidth <= drawableHeight) {
            viewHeight.toFloat() / drawableHeight
        } else {
            var scale = viewWidth.toFloat() / drawableWidth

            if (drawableHeight * scale > viewHeight) {
                viewHeight.toFloat() / drawableHeight
            } else {
                scale
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                eventStartPoint[event.x] = event.y
                savedTranslationOnX = currentTranslationOnX
                savedTranslationOnY = currentTranslationOnY
                mode = DRAG
            }
            MotionEvent.ACTION_UP -> {
                onClick(event)
                mode = NONE
            }
            MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                midPoint(midpoint, event)
                mode = ZOOM
            }
            MotionEvent.ACTION_MOVE -> {
                if (mode == DRAG && currentScale != defaultScale) {
                    val currentDrawableWidth = drawableWidth * currentScale
                    val currentDrawableHeight = drawableHeight * currentScale

                    if (drawableWidth <= drawableHeight) {
                        currentTranslationOnY = if (currentDrawableHeight <= viewHeight) {
                            (viewHeight - currentDrawableHeight) / 2
                        } else {
                            min(
                                0F,
                                max(
                                    viewHeight - currentDrawableHeight,
                                    savedTranslationOnY + event.y - eventStartPoint.y
                                )
                            )
                        }
                        if (currentDrawableWidth > viewWidth) {
                            currentTranslationOnX = min(
                                0F,
                                max(
                                    viewWidth - currentDrawableWidth,
                                    savedTranslationOnX + event.x - eventStartPoint.x
                                )
                            )
                        }
                    } else {
                        currentTranslationOnX = if (currentDrawableWidth <= viewWidth) {
                            (viewWidth - currentDrawableWidth) / 2
                        } else {
                            min(
                                0F,
                                max(
                                    viewWidth - currentDrawableWidth,
                                    savedTranslationOnX + event.x - eventStartPoint.x
                                )
                            )
                        }

                        if (currentDrawableHeight > viewHeight) {
                            currentTranslationOnY = min(
                                0F,
                                max(
                                    viewHeight - currentDrawableHeight,
                                    savedTranslationOnY + event.y - eventStartPoint.y
                                )
                            )
                        }
                    }

                    updateImageMatrix()
                }
            }
        }

        return true
    }

    private fun onClick(event: MotionEvent): Boolean {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < intervalDoubleClick) {
            doubleTap(event)
        }
        lastClickTime = clickTime
        return true
    }

    private fun doubleTap(event: MotionEvent) {
        if (currentScale != defaultScale) {
            val currentScaleValueAnimator = valueAnimator(currentScale, defaultScale) {
                currentScale = it
                updateImageMatrix()
            }
            val currentTranslationOnXValueAnimator =
                valueAnimator(currentTranslationOnX, defaultTranslationOnX) {
                    currentTranslationOnX = it
                    updateImageMatrix()
                }
            val currentTranslationOnYValueAnimator =
                valueAnimator(currentTranslationOnY, defaultTranslationOnY) {
                    currentTranslationOnY = it
                    updateImageMatrix()
                }

            currentScaleValueAnimator.start()
            currentTranslationOnXValueAnimator.start()
            currentTranslationOnYValueAnimator.start()
        } else {
            val scaleValue: Float
            val translationXFrom: Float
            val translationYFrom: Float
            var translationXTo = 0F
            var translationYTo = 0F

            if (drawableWidth <= drawableHeight || drawableHeight * currentScale > viewHeight) {
                val scaleOfChane =
                    min(MAX_ZOOM, viewWidth.toFloat() / (drawableWidth * defaultScale))
                val defaultDrawableHeight = drawableHeight * defaultScale
                val finalDrawableHeight = defaultDrawableHeight * scaleOfChane

                scaleValue = min(MAX_ZOOM, viewWidth.toFloat() / drawableWidth)
                translationXFrom = currentTranslationOnX
                translationYFrom = currentTranslationOnY
                translationYTo =
                    event.y - (finalDrawableHeight * ((event.y * 100) / viewHeight)) / 100

                if (drawableWidth * scaleValue <= viewWidth) {
                    val defaultDrawableWidth = drawableWidth * defaultScale
                    val finalDrawableWidth = defaultDrawableWidth * scaleOfChane
                    translationXTo =
                        event.x - (finalDrawableWidth * ((event.x * 100) / viewWidth)) / 100
                }
            } else {
                val scaleOfChane =
                    min(MAX_ZOOM, viewHeight.toFloat() / (drawableHeight * defaultScale))
                val defaultDrawableWidth = drawableWidth * defaultScale
                val finalDrawableWidth = defaultDrawableWidth * scaleOfChane

                scaleValue = min(MAX_ZOOM, viewHeight.toFloat() / drawableHeight)
                translationXFrom = currentTranslationOnX
                translationYFrom = currentTranslationOnY
                translationXTo =
                    event.x - (finalDrawableWidth * ((event.x * 100) / viewWidth)) / 100

                if (drawableHeight * scaleValue <= viewHeight) {
                    val defaultDrawableHeight = drawableHeight * defaultScale
                    val finalDrawableHeight = defaultDrawableHeight * scaleOfChane
                    translationYTo =
                        event.y - (finalDrawableHeight * ((event.y * 100) / viewHeight)) / 100
                }
            }

            val scaleValueAnimator = valueAnimator(defaultScale, scaleValue) {
                currentScale = it
                updateImageMatrix()
            }
            val translationXValueAnimator = valueAnimator(translationXFrom, translationXTo) {
                currentTranslationOnX = it
                updateImageMatrix()
            }
            val translationYValueAnimator = valueAnimator(translationYFrom, translationYTo) {
                currentTranslationOnY = it
                updateImageMatrix()
            }

            scaleValueAnimator.start()
            translationXValueAnimator.start()
            translationYValueAnimator.start()
        }
    }

    private fun zoom(detector: ScaleGestureDetector) {
        val scaleSaved = currentScale
        val drawableWidthSaved = drawableWidth * scaleSaved
        val drawableHeightSaved = drawableHeight * scaleSaved

        currentScale *= detector.scaleFactor
        currentScale = max(defaultScale, min(currentScale, MAX_ZOOM))

        val finalDrawableWidth = drawableWidth * currentScale
        val finalDrawableHeight = drawableHeight * currentScale

        if (drawableWidth <= drawableHeight || finalDrawableHeight > viewHeight) {
            val dPosY = abs(currentTranslationOnY) + midpoint.y
            val pDY = (dPosY * 100) / drawableHeightSaved
            val dPosYF = (finalDrawableHeight * pDY) / 100
            currentTranslationOnY = if (drawableHeight <= viewHeight) {
                (viewHeight - finalDrawableHeight) / 2
            } else {
                min(0F, max(viewHeight - finalDrawableHeight, midpoint.y - dPosYF))
            }

            if (currentTranslationOnX < 0) {
                val dPosX = abs(currentTranslationOnX) + midpoint.x
                val pDX = (dPosX * 100) / drawableWidthSaved
                val dPosXF = (finalDrawableWidth * pDX) / 100
                currentTranslationOnX =
                    min(0F, max(viewWidth - finalDrawableWidth, midpoint.x - dPosXF))
            } else {
                if (currentScale > scaleSaved) {
                    currentTranslationOnX += (drawableWidthSaved - finalDrawableWidth) / 2
                } else if (currentScale < scaleSaved) {
                    if (finalDrawableWidth > viewWidth && currentTranslationOnX == 0F) {
                        currentTranslationOnX = 0F
                    } else {
                        currentTranslationOnX += (drawableWidthSaved - finalDrawableWidth) / 2
                    }
                }
            }
        } else {
            val dPosX = abs(currentTranslationOnX) + midpoint.x
            val pDX = (dPosX * 100) / drawableWidthSaved
            val dPosXF = (finalDrawableWidth * pDX) / 100
            currentTranslationOnX = if (drawableWidth <= viewWidth) {
                (viewWidth - finalDrawableWidth) / 2
            } else {
                min(0F, max(viewWidth - finalDrawableWidth, midpoint.x - dPosXF))
            }

            if (currentTranslationOnY < 0) {
                val dPosY = abs(currentTranslationOnY) + midpoint.y
                val pDY = (dPosY * 100) / drawableHeightSaved
                val dPosYF = (finalDrawableHeight * pDY) / 100
                currentTranslationOnY =
                    min(0F, max(viewHeight - finalDrawableHeight, midpoint.y - dPosYF))
            } else {
                if (currentScale > scaleSaved) {
                    currentTranslationOnY += (drawableHeightSaved - finalDrawableHeight) / 2
                } else if (currentScale < scaleSaved) {
                    if (finalDrawableHeight > viewHeight && currentTranslationOnY == 0F) {
                        currentTranslationOnY = 0F
                    } else {
                        currentTranslationOnY += (drawableHeightSaved - finalDrawableHeight) / 2
                    }
                }
            }
        }

        updateImageMatrix()
    }

    private fun updateImageMatrix() {
        imageMatrix = matrix.apply {
            setScale(currentScale, currentScale)
            postTranslate(currentTranslationOnX, currentTranslationOnY)
        }
    }

    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point[x / 2] = y / 2
    }

    private fun valueAnimator(
        from: Float,
        to: Float,
        onValueChanged: (Float) -> Unit
    ): ValueAnimator {
        return ValueAnimator.ofFloat(from, to).apply {
            duration = animationsTime
            addUpdateListener { onValueChanged(it.animatedValue as Float) }
        }
    }

    companion object {
        const val NONE = 0
        const val DRAG = 1
        const val ZOOM = 2
        const val MAX_ZOOM = 5F
    }
}
