package co.condorlabs.customcomponents.custombutton

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.*
import androidx.core.content.ContextCompat
import co.condorlabs.customcomponents.DEFAULT_STROKE_WIDTH
import co.condorlabs.customcomponents.R


/**
 * @author Oscar Gallon on 2019-04-26.
 */

sealed class CustomButtonStyle(
    private val backgroundColor: Int,
    private val textColor: Int,
    private val rippleColor: Int = R.color.rippleColor,
    private val strokeColor: Int = backgroundColor,
    private val strokeWidth: Int = DEFAULT_STROKE_WIDTH,
    private val highlightTextColor: Int = textColor
) {
    object DefaultButtonStyle :
        CustomButtonStyle(
            R.color.white,
            R.color.black,
            R.color.defaultRippleColor,
            R.color.defaultButtonBorderColor
        )

    object PrimaryButtonStyle : CustomButtonStyle(R.color.primaryColor, R.color.white, R.color.primaryRippleColor)
    object DangerButtonStyle : CustomButtonStyle(R.color.dangerColor, R.color.white, R.color.dangerRippleColor)
    object InfoButtonStyle : CustomButtonStyle(R.color.infoColor, R.color.white, R.color.infoRippleColor)
    object SuccessButtonStyle : CustomButtonStyle(R.color.successColor, R.color.white, R.color.successRippleColor)
    object WarningButtonStyle : CustomButtonStyle(R.color.warningColor, R.color.white, R.color.warningRippleColor)
    object OverlayButtonStyle :
        CustomButtonStyle(
            R.color.white,
            R.color.primaryColor,
            R.color.overlayRippleColor,
            R.color.primaryColor,
            highlightTextColor = R.color.white
        )

    fun getBackground(context: Context): StateListDrawable {
        val rippleDrawable = getRippleDrawable(context)
        return StateListDrawable().apply {
            addState(
                intArrayOf(android.R.attr.state_pressed),
                getPressedBackground(context)
            )
            addState(
                intArrayOf(android.R.attr.state_focused),
                getPressedBackground(context)
            )
            addState(
                intArrayOf(-android.R.attr.state_pressed),
                rippleDrawable
            )
            addState(
                intArrayOf(-android.R.attr.state_focused),
                rippleDrawable
            )
        }
    }

    fun getTextColor(context: Context): ColorStateList {
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_pressed)
        )

        val colors =
            intArrayOf(ContextCompat.getColor(context, textColor), ContextCompat.getColor(context, highlightTextColor))

        return ColorStateList(states, colors)
    }

    private fun getPressedBackground(context: Context): Drawable {
        return ContextCompat.getDrawable(context, R.drawable.custom_buttom_background).apply {
            (this as? GradientDrawable)?.setColor(ContextCompat.getColor(context, rippleColor))
        } ?: ColorDrawable().apply { color = ContextCompat.getColor(context, rippleColor) }
    }

    private fun getRippleDrawable(context: Context): RippleDrawable {
        val backgroundStyled = ColorGradientDrawable().apply {
            setSolidColor(ContextCompat.getColor(context, backgroundColor))
            setStrokeColor(ContextCompat.getColor(context, strokeColor), strokeWidth)
            shape = GradientDrawable.RECTANGLE
        }

        return RippleDrawable(
            getPressedColor(ContextCompat.getColor(context, rippleColor)),
            backgroundStyled,
            null
        )
    }

    private fun getPressedColor(rippleColor: Int): ColorStateList {
        return ColorStateList(
            arrayOf(intArrayOf()),
            intArrayOf(rippleColor)
        )
    }
}
