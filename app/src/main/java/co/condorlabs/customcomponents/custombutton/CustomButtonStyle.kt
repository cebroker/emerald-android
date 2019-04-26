package co.condorlabs.customcomponents.custombutton

import co.condorlabs.customcomponents.R

/**
 * @author Oscar Gallon on 2019-04-26.
 */

sealed class CustomButtonStyle(
    val backgroundColor: Int,
    val textColor: Int,
    val rippleColor: Int = R.color.rippleColor,
    val strokeColor: Int = backgroundColor
) {
    object DefaultButtonStyle :
        CustomButtonStyle(
            R.color.white,
            R.color.black,
            R.color.defaultButtonStyleRippleColor,
            R.color.defaultButtonBorderColor
        )

    object PrimaryButtonStyle : CustomButtonStyle(R.color.primaryColor, R.color.white)
    object DangerButtonStyle : CustomButtonStyle(R.color.dangerColor, R.color.white)
    object InfoButtonStyle : CustomButtonStyle(R.color.infoColor, R.color.white)
    object SuccessButtonStyle : CustomButtonStyle(R.color.successColor, R.color.white)
    object WarningButtonStyle : CustomButtonStyle(R.color.warningColor, R.color.white)
}
