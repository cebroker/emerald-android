package co.condorlabs.customcomponents.customcheckbox

import android.content.Context
import android.util.AttributeSet

class CheckboxFormField(context: Context, mAttrs: AttributeSet) : BaseCheckboxFormField(context, mAttrs) {


    override var isRequired: Boolean = false

    override fun setIsRequired(isRequired: Boolean) {
        this.isRequired = isRequired
    }
}
