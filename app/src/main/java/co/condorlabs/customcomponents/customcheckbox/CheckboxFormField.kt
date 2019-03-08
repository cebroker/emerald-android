package co.condorlabs.customcomponents.customcheckbox

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.CheckBox
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.formfield.Selectable

class CheckboxFormField(context: Context, mAttrs: AttributeSet) : BaseCheckboxFormField(context, mAttrs) {


    override var mIsRequired: Boolean = false

    fun setIsRequired(isRequired: Boolean) {
        mIsRequired = isRequired
    }
}
