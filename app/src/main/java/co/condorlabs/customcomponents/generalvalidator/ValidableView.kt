package co.condorlabs.customcomponents.generalvalidator

import android.view.ViewGroup
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.helper.ZERO

interface ValidableView {

    val mFormFields: ArrayList<FormField<*>>

    fun addCustomComponents(viewGroup: ViewGroup) {

        if (mFormFields.size == ZERO) {
            viewGroup.let {
                for (i in ZERO until it.childCount) {
                    (it.getChildAt(i) as? FormField<*>)?.let { formField ->
                        mFormFields.add(formField)
                    }
                }
            }
        }
    }

    fun areComponentsValid(): Boolean {

        mFormFields.forEach {
            if (!it.isValid().isValid) {
                it.showError(it.isValid().error)
            } else {
                it.clearError()
            }
        }

        return true
    }
}
