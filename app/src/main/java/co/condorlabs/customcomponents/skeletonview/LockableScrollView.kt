package co.condorlabs.customcomponents.skeletonview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

/**
 * @author Oscar Gallon on 2019-05-17.
 */
class c : ScrollView {

    var isScrollable = true

    constructor(context: Context) : super(context) {}

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isScrollable) super.onTouchEvent(ev) else isScrollable
            }
            else -> super.onTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (!isScrollable)
            false
        else
            super.onInterceptTouchEvent(ev)
    }
}
