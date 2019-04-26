package co.condorlabs.customcomponents.custombutton

import android.graphics.drawable.GradientDrawable
import co.condorlabs.customcomponents.R

/**
 * @author Oscar Gallon on 2019-04-26.
 */
class ColorGradientDrawable : GradientDrawable() {

    private var solidColor: Int = R.color.white
    private var strokeColor: Int = R.color.defaultButtonBorderColor

    fun setSolidColor(color: Int) {
        this.solidColor = color
        setColor(color)
    }

    fun setStrokeColor(color: Int) {
        this.strokeColor = color
        setStroke(1, color)
    }

    fun getSolidColor(): Int = solidColor

    fun getStrokeColor(): Int = strokeColor

}
