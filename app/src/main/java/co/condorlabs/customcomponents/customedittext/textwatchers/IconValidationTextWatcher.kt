package co.condorlabs.customcomponents.customedittext.textwatchers

import android.text.Editable
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.ZERO
import co.condorlabs.customcomponents.customedittext.BaseEditTextFormField
import co.condorlabs.customcomponents.customedittext.ValueChangeListener

/**
 * @author Oscar Gallon on 2019-06-27.
 */
class IconValidationTextWatcher(
    private val receiver: BaseEditTextFormField,
    private val valueChangeListener: ValueChangeListener<String>?
) : DefaultTextWatcher(valueChangeListener) {

    override fun afterTextChanged(s: Editable?) {
        super.afterTextChanged(s)
        if (receiver.isValid().isValid) {
            showCheckedIcon()
            receiver.clearError()
        } else {
            hideCheckIcon()
        }
    }

    private fun showCheckedIcon() {
        receiver.editText?.setCompoundDrawablesWithIntrinsicBounds(ZERO, ZERO, R.drawable.ic_checked_circle, ZERO)
    }

    private fun hideCheckIcon() {
        receiver.editText?.setCompoundDrawablesWithIntrinsicBounds(ZERO, ZERO, ZERO, ZERO)
    }
}
