package co.condorlabs.customcomponents.customtextview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import co.condorlabs.customcomponents.*

class CustomTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    private var textType: Int = ZERO

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomTextView,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )
        textType = typedArray.getInt(R.styleable.CustomTextView_type_text, ZERO)
        typedArray.recycle()
        setStyle()
    }

    private fun setStyle() {
        CustomTextViewStyleFactory.getStyleFromType(textType).apply {
            typeface = Typeface.createFromAsset(context.assets, getFontType())
            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(getFontSize()))
            setTextColor(ContextCompat.getColor(context, getColorText()))
        }
    }

    fun setCustomTextViewType(typeText: Int) {
        this.textType = typeText
        setStyle()
    }

    fun getTextType() = textType
}
