package co.condorlabs.customcomponents.custombutton

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

/**
 * @author Oscar Gallon on 2019-05-23.
 */
class CustomDrawableSpan(drawable: Drawable) : ImageSpan(drawable) {

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        super.draw(canvas, text, start, end, x, top, y, bottom, paint)
        canvas.save()
        val fontMetrics = paint.fontMetricsInt

        val lineHeight = fontMetrics.bottom - fontMetrics.top

        val centerY = y + fontMetrics.bottom - lineHeight / 2
        val transY = centerY - drawable.bounds.height() / 2
        canvas.translate(x, transY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fontMetricsInt: Paint.FontMetricsInt?
    ): Int {
        // get drawable dimensions
        val rect = drawable.bounds
        fontMetricsInt?.let {
            val fontMetrics = paint.fontMetricsInt

            // get a font height
            val lineHeight = fontMetrics.bottom - fontMetrics.top

            //make sure our drawable has height >= font height
            val drHeight = Math.max(lineHeight, rect.bottom - rect.top)

            val centerY = fontMetrics.top + lineHeight / 2

            //adjust font metrics to fit our drawable size
            fontMetricsInt.apply {
                ascent = centerY - drHeight / 2
                descent = centerY + drHeight / 2
                top = ascent
                bottom = descent
            }
        }

        return rect.width()
    }
}
