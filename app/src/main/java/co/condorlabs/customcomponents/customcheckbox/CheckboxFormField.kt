package co.condorlabs.customcomponents.customcheckbox

import android.content.Context
import android.util.AttributeSet

class CheckboxFormField(context: Context, mAttrs: AttributeSet) : BaseCheckboxFormField(context, mAttrs) {

    override var mIsRequired: Boolean = false

    fun setArrayTextOption(options: Array<CharSequence>){
        textOptions = options
    }

    fun setIsRequired(isRequired: Boolean){
        mIsRequired = isRequired
    }

}