package co.condorlabs.customcomponents.customradiogroup

import android.content.Context
import android.util.AttributeSet

class RadioGroupFormField(context: Context, attrs: AttributeSet) : BaseRadioGroupFormField(context, attrs) {


    override var mIsRequired: Boolean = false

    fun setArrayTextOption(options: Array<CharSequence>){
        textOptions = options
    }

    fun setIsRequired(isRequired: Boolean){
        mIsRequired = isRequired
    }

}
