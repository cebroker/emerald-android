package co.condorlabs.customcomponents.custombutton

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.widget.Button
import co.condorlabs.customcomponents.*

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
        val backgroundStyled = ColorGradientDrawable().apply {
            setSolidColor(ContextCompat.getColor(context, style.backgroundColor))
            setStrokeColor(ContextCompat.getColor(context, style.strokeColor))
            shape = GradientDrawable.RECTANGLE
        }
        background = RippleDrawable(
            getPressedColor(ContextCompat.getColor(context, style.rippleColor)),
            backgroundStyled,
            null
        )
        setTextColor(ContextCompat.getColor(context, style.textColor))
    }

    private fun getPressedColor(rippleColor: Int): ColorStateList {
        return ColorStateList(
            arrayOf(intArrayOf()),
            intArrayOf(rippleColor)
        )
    }

    private fun setFont() {
        val font = Typeface.createFromAsset(context.assets, OPEN_SANS_SEMI_BOLD)
        typeface = font
    }
}
