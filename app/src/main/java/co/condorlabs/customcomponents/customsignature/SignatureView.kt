package co.condorlabs.customcomponents.customsignature

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import co.condorlabs.customcomponents.*


class SignatureView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var paint = Paint()
    private var path = Path()
    private var drawLineAndText = true
    private var currentX = 0f
    private var currentY = 0f
    private var startX = 0f
    private var startY = 0f
    var canvas: Canvas? = null
    private var onActionMoveListener: OnActionMoveListener? = null

    init {
        setUpPaint()
    }

    private fun setUpPaint() {
        paint.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = SIGNATURE_DRAW_STROKE_WIDTH
            isAntiAlias = true
        }
    }

    fun setOnActionMoveListener(listener: OnActionMoveListener) {
        if(onActionMoveListener == null) {
            onActionMoveListener = listener
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (drawLineAndText)
            drawLineAndText(canvas)
        canvas.drawPath(path, paint)
        this.canvas = canvas
    }

    private fun actionDown(x: Float, y: Float) {
        path.moveTo(x, y)
        currentX = x
        currentY = y
        if(onActionMoveListener != null) {
            onActionMoveListener?.onDrawnSignature()
            onActionMoveListener = null
        }
    }

    private fun actionMove(x: Float, y: Float) {
        path.quadTo(currentX, currentY, (x + currentX) / 2, (y + currentY) / 2)
        currentX = x
        currentY = y
    }

    private fun actionUp() {
        path.lineTo(currentX, currentY)
        if (startX == currentX && startY == currentY) {
            path.lineTo(currentX, currentY + 2)
            path.lineTo(currentX + 1, currentY + 2)
            path.lineTo(currentX + 1, currentY)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = x
                startY = y
                actionDown(x, y)
            }
            MotionEvent.ACTION_MOVE -> actionMove(x, y)
            MotionEvent.ACTION_UP -> actionUp()
        }
        invalidate()
        return true
    }

    fun clearCanvas() {
        path.reset()
        invalidate()
    }

    private fun drawLineAndText(canvas: Canvas) {
        paint.color = Color.GRAY
        paint.style = Paint.Style.FILL
        paint.strokeWidth = SIGNATURE_LINE_STROKE_WIDTH

        val lineStartX = calculateLineStartX(canvas.width)
        val lineStopX = width - lineStartX
        val lineY = calculateLineY(canvas.height)
        canvas.drawLine(lineStartX, lineY, lineStopX, lineY, paint)

        val pixel = calculatePixel()
        paint.textSize = pixel
        paint.fontFeatureSettings = SIGNATURE_CANVAS_FONT

        canvas.drawText(SIGNATURE_INDICATOR, lineStartX, lineY - (pixel / 2), paint)

        paint.textAlign = Paint.Align.CENTER

        canvas.drawText(SIGNATURE_USE_YOUR_FINGER_TO_DRAW, (canvas.width / 2).toFloat(), lineY + (2 * pixel), paint)
        setUpPaint()
    }

    private fun calculateLineStartX(canvasWidth: Int) = (canvasWidth * SIGNATURE_CANVAS_WIDTH_PERCENTAGE).toFloat()

    private fun calculateLineY(canvasHeight: Int) = (canvasHeight * SIGNATURE_CANVAS_HEIGHT_PERCENTAGE).toFloat()

    private fun calculatePixel() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        SIGNATURE_CANVAS_DIP_VALUE,
        resources.displayMetrics
    ) * SIGNATURE_CANVAS_DIP_FACTOR

    fun stopDrawingLineAndText() {
        drawLineAndText = false
        setBackgroundColor(Color.WHITE)
        background = null
    }
}
