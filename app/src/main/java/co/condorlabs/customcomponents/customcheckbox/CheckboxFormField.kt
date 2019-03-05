package co.condorlabs.customcomponents.customcheckbox

import android.content.Context
import android.util.AttributeSet
import co.condorlabs.customcomponents.customedittext.ValueChangeListener

class CheckboxFormField(context: Context, mAttrs: AttributeSet) : BaseCheckboxFormField(context, mAttrs) {

    override var mIsRequired: Boolean = false

    fun setIsRequired(isRequired: Boolean) {
        mIsRequired = isRequired
    }
}
