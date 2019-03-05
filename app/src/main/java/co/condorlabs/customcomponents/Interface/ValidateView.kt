package co.condorlabs.customcomponents.Interface

import android.view.ViewGroup
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.helper.ZERO

open class ValidateView : IValidate {


    protected val mFormFields = ArrayList<FormField<Any>>()
    override fun addCustomComponents(viewGroup: ViewGroup) {
        viewGroup.let {
            for (i in ZERO until it.childCount) {
                (it.getChildAt(i) as? FormField<Any>)?.let { formField ->
                    mFormFields.add(formField)
                }
            }
        }
    }

    override fun validateCustomComponents(): Boolean {
        var isValid = true
        mFormFields.forEach {
            it.clearError()
            if (!it.isValid().isValid) {
                it.showError(it.isValid().error)
                isValid = false
            }
        }
        return isValid
    }
}