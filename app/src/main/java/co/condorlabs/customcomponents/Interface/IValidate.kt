package co.condorlabs.customcomponents.Interface

import android.view.ViewGroup

interface IValidate {

    fun addCustomComponents(viewGroup: ViewGroup)

    fun validateCustomComponents(): Boolean
}
