package co.condorlabs.customcomponents.custombutton

/**
 * @author Oscar Gallon on 2019-05-23.
 */
sealed class ButtonState {

    object Loading : ButtonState()
    object Normal : ButtonState()
}
