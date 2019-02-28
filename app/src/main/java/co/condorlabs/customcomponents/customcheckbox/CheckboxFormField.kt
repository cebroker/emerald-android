package co.condorlabs.customcomponents.customcheckbox

import android.content.Context
import android.util.AttributeSet
import co.condorlabs.customcomponents.formfield.Selectable
import java.util.*

class CheckboxFormField(context: Context, mAttrs: AttributeSet) : BaseCheckboxFormField(context, mAttrs) {

    override var mIsRequired: Boolean = false

    fun setArrayTextOption(options: Array<CharSequence>) {
        textOptions = options
    }

    fun setIsRequired(isRequired: Boolean) {
        mIsRequired = isRequired
    }

    override fun getValue(): List<Selectable> {
        val values = ArrayList<Selectable>()

        return values
    }


}
