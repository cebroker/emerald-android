package co.condorlabs.customcomponents.customtextview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import co.condorlabs.customcomponents.*

class CustomTextView(context: Context, private val attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    private var typeFont: String? = null

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomTextView,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )
        typeFont = typedArray.getString(R.styleable.CustomTextView_type_text)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setup()
    }

    private fun setup() {
        typeface = Typeface.createFromAsset(context.assets, OPEN_SANS_SEMI_BOLD)
        when (typeFont) {
            TEXT_VIEW_TITLE -> showTitle()
            TEXT_VIEW_SUBTITLE -> showSubtitle()
            else -> defaultText()
        }
    }

    private fun defaultText() {
        typeface = Typeface.createFromAsset(context.assets, OPEN_SANS_REGULAR)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.default_text_size))
        setTextColor(ContextCompat.getColor(context, R.color.subtitleColor))
    }

    private fun showSubtitle() {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.default_text_size))
        setTextColor(ContextCompat.getColor(context, R.color.subtitleColor))
    }

    private fun showTitle() {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.default_title_size))
        setTextColor(ContextCompat.getColor(context, R.color.textColor))
    }
}
