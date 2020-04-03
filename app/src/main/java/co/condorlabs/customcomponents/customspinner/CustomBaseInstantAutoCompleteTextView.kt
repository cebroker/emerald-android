package co.condorlabs.customcomponents.customspinner

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import co.condorlabs.customcomponents.R

/**
 * @author Oscar Gallon on 2019-05-14.
 */
class CustomBaseInstantAutoCompleteTextView(context: Context, attrs: AttributeSet) :
    AutoCompleteTextView(context, attrs) {

    private var isEnable: Boolean = true

    init {
        isCursorVisible = false
        keyListener = null
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.body))
        dismissDropDown()
        clearFocus()
    }

    override fun enoughToFilter(): Boolean {
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isEnable) {
            performFiltering(text, 0)
            showDropDown()
        }
        if (event?.action == MotionEvent.ACTION_DOWN) {
            hideKeyBoard()
        }
        return super.onTouchEvent(event)
    }

    fun setEnable(isEnable: Boolean) {
        this.isEnable = isEnable
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused && adapter != null) {
            performFiltering(text, 0)
        }
    }

    private fun hideKeyBoard() {
        val inputMethodManager = this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
    }
}
