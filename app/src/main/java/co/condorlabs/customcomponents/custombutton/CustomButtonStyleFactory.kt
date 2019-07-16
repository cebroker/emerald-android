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
            BUTTON_OVERLAY_TYPE -> CustomButtonStyle.OverlayButtonStyle
            BUTTON_PRIMARY_INVERTED_TYPE -> CustomButtonStyle.PrimaryInvertedButtonStyle
            BUTTON_WHITE_SHAPE_TYPE -> CustomButtonStyle.WhiteShapeButtonStyle
            else -> CustomButtonStyle.DefaultButtonStyle
        }
    }
}
