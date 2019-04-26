package co.condorlabs.customcomponents.custombutton

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.Button
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.helper.*


/**
 * @author Oscar Gallon on 2019-04-26.
 */
class CustomButton(context: Context, attrs: AttributeSet) : Button(context, attrs) {

    private var type: ButtonType = BUTTON_DEFAULT_TYPE
    private val customButtonStyleFactory = CustomButtonStyleFactory()

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomButton,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )

        type = typedArray.getString(R.styleable.CustomButton_type)
            ?: context.getString(R.string.default_base_hint)

        typedArray.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        renderStyle()
    }

    fun setType(type: ButtonType) {
        this.type = type
        setup()
    }

    private fun setup() {
        renderStyle()
        setFont()
    }

    private fun renderStyle() {
        val style = customButtonStyleFactory.getCustomColorFromType(type)
        background = ColorGradientDrawable().apply {
            setSolidColor(ContextCompat.getColor(context, style.backgroundColor))
            setStrokeColor(ContextCompat.getColor(context, style.strokeColor))
            shape = GradientDrawable.RECTANGLE
        }
        setTextColor(ContextCompat.getColor(context, style.textColor))
    }

    private fun setFont() {
        val font = Typeface.createFromAsset(context.assets, OPEN_SANS_SEMI_BOLD)
        typeface = font
    }
}
