package co.condorlabs.customcomponents.customedittext

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.DIGITS_PHONE
import co.condorlabs.customcomponents.helper.MAX_LENGHT
import co.condorlabs.customcomponents.helper.VALIDATE_LENGTH_ERROR
import co.condorlabs.customcomponents.helper.masks.PhoneNumberTextWatcherMask


class EditTextPhoneField(context: Context, attrs: AttributeSet) : BaseEditTextFormField(context, attrs) {


    override fun setup() {
        super.setup()
        mEditText?.id = R.id.etPhone
        setInputType()
        setMaxLength()
        setPhoneMask()
        setDigits()
    }

    private fun setDigits() {
        this.editText?.keyListener = DigitsKeyListener.getInstance(DIGITS_PHONE)
    }

    private fun setInputType() {
        mEditText?.inputType = InputType.TYPE_CLASS_NUMBER
    }

    private fun setPhoneMask() {
        this.editText?.apply {
            addTextChangedListener(PhoneNumberTextWatcherMask(this))
        }
    }

    private fun setMaxLength() {
        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[0] = InputFilter.LengthFilter(MAX_LENGHT)
        mEditText?.filters = filterArray
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, VALIDATE_LENGTH_ERROR)
    }
}
