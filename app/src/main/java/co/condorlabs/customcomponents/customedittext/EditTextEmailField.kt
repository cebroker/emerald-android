package co.condorlabs.customcomponents.customedittext

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.VALIDATE_EMAIL_ERROR

class EditTextEmailField(context: Context, attrs: AttributeSet) : BaseEditTextFormField(context, attrs) {

    override fun setup() {
        super.setup()
        mEditText?.id = R.id.etEmail
        setInputType()
        setRegex(android.util.Patterns.EMAIL_ADDRESS.toString())
    }

    private fun setInputType() {
        mEditText?.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }


    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, VALIDATE_EMAIL_ERROR)
    }
}
