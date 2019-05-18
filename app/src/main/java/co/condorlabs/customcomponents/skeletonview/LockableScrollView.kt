package co.condorlabs.customcomponents.skeletonview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

/**
 * @author Oscar Gallon on 2019-05-17.
 */
class LockableScrollView : ScrollView {

    // true if we can scroll the LockableScrollView
    // false if we cannot scroll
    var isScrollable = true

    constructor(context: Context) : super(context) {}

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs) {
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    fun setScrollingEnabled(scrollable: Boolean) {
        this.isScrollable = scrollable
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                // if we can scroll pass the event to the superclass
                if (isScrollable) super.onTouchEvent(ev) else isScrollable
                // only continue to handle the touch event if scrolling enabled
                // scrollable is always false at this point
            }
            else -> super.onTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        return if (!isScrollable)
            false
        else
            super.onInterceptTouchEvent(ev)
    }
}
