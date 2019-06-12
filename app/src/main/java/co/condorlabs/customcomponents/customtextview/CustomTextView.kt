package co.condorlabs.customcomponents.customtextview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import co.condorlabs.customcomponents.*

class CustomTextView(context: Context, private val attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    private var typeText: Int = NO_ID

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomTextView,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )
        typeText = typedArray.getInt(R.styleable.CustomTextView_type_text, View.NO_ID)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setup()
        val DEFAULT_BOLD: Typeface
    }

    private fun setup() {
        typeface = Typeface.createFromAsset(context.assets, OPEN_SANS_SEMI_BOLD)
        when (typeText) {
            TYPE_TITLE -> showTitle()
            TYPE_SUBTITLE -> showSubtitle()
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

    fun setCustomTextViewType(typeText: Int){
        this.typeText = typeText
        setup()
    }
}

