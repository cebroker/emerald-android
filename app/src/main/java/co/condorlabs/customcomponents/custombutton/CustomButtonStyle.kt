package co.condorlabs.customcomponents.custombutton

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import co.condorlabs.customcomponents.*

/**
 * @author Oscar Gallon on 2019-04-26.
 */

sealed class CustomButtonStyle(
    val backgroundColor: Int,
    private val textColor: Int,
    private val rippleColor: Int = R.color.rippleColor,
    private val strokeColor: Int = backgroundColor,
    private val strokeWidth: Int = DEFAULT_STROKE_WIDTH,
    private val highlightTextColor: Int = textColor
) {
    object DefaultButtonStyle :
        CustomButtonStyle(
            R.color.white,
            R.color.textColor,
            R.color.defaultRippleColor,
            strokeColor = R.color.defaultButtonBorderColor
        ) {
        override fun getTextColor(context: Context): ColorStateList {
            return ColorStateList.valueOf(Color.BLACK)
        }
    }

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
    object FlatPrimaryButtonStyle :
        CustomButtonStyle(
            R.color.white,
            R.color.primaryColor,
            R.color.white
        )

    object ShapeWhiteButtonStyle : CustomButtonStyle(
        R.color.transparent,
        R.color.white,
        R.color.white,
        R.color.white)

    object ShapeButtonStyle : CustomButtonStyle(
        R.color.transparent,
        R.color.white,
        R.color.transparent,
        R.color.transparent)

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

    fun getProgressDrawable(context: Context, background: Drawable): Drawable {
        val progressDrawable = CircularProgressDrawable(context).apply {
            setStyle(CircularProgressDrawable.LARGE)
            setColorSchemeColors(ContextCompat.getColor(context, textColor))
            val size = (centerRadius + strokeWidth).toInt() * PROGRESS_DRAWABLE_SIZE_MULTIPLIER
            setBounds(ZERO, ZERO, size, size)
            start()
        }

        val horizontalInset =
            (background.intrinsicWidth - progressDrawable.intrinsicWidth) / PROGRESS_DRAWABLE_DIAMETER_DIVIDER

        return LayerDrawable(arrayOf(background, progressDrawable))
            .apply {
                setLayerInset(ZERO, ZERO, ZERO, ZERO, progressDrawable.intrinsicHeight)
                setLayerInset(
                    PROGRESS_DRAWABLE_LAYER_INSET_INDEX,
                    horizontalInset,
                    progressDrawable.intrinsicHeight,
                    horizontalInset,
                    ZERO
                )
            }
    }

    private fun getColorGradientBackgroundDrawable(context: Context): Drawable {
        return ColorGradientDrawable().apply {
            setSolidColor(ContextCompat.getColor(context, backgroundColor))
            setStrokeColor(ContextCompat.getColor(context, strokeColor), strokeWidth)
            shape = GradientDrawable.RECTANGLE
            cornerRadius = context.resources.getDimension(R.dimen.button_radius)
        }
    }

    open fun getTextColor(context: Context): ColorStateList {
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(-android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_focused)
        )

        val colors =
            intArrayOf(
                ContextCompat.getColor(context, textColor), ContextCompat.getColor(context, highlightTextColor),
                ContextCompat.getColor(context, textColor), ContextCompat.getColor(context, highlightTextColor)
            )

        return ColorStateList(states, colors)
    }

    private fun getPressedBackground(context: Context): Drawable {
        return ContextCompat.getDrawable(context, R.drawable.custom_buttom_background).apply {
            (this as? GradientDrawable)?.setColor(ContextCompat.getColor(context, rippleColor))
        } ?: ColorDrawable().apply { color = ContextCompat.getColor(context, rippleColor) }
    }

    private fun getRippleDrawable(context: Context): RippleDrawable {
        return RippleDrawable(
            getPressedColor(ContextCompat.getColor(context, rippleColor)),
            getColorGradientBackgroundDrawable(context),
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
