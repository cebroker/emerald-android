package co.condorlabs.customcomponents.custombutton

import co.condorlabs.customcomponents.*

/**
 * @author Oscar Gallon on 2019-04-26.
 */
class CustomButtonStyleFactory {

    fun getCustomColorFromType(type: ButtonType): CustomButtonStyle {
        return when (type) {
            BUTTON_PRIMARY_TYPE -> CustomButtonStyle.PrimaryButtonStyle
            BUTTON_DANGER_TYPE -> CustomButtonStyle.DangerButtonStyle
            BUTTON_WARNING_TYPE -> CustomButtonStyle.WarningButtonStyle
            BUTTON_INFO_TYPE -> CustomButtonStyle.InfoButtonStyle
            BUTTON_SUCCESS_TYPE -> CustomButtonStyle.SuccessButtonStyle
            else -> CustomButtonStyle.DefaultButtonStyle
        }
    }
}
