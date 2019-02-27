package co.condorlabs.customcomponents.customedittext

import android.content.Context
import android.util.AttributeSet
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.VALIDATE_CURRENCY_ERROR
import co.condorlabs.customcomponents.helper.masks.PriceTextWatcherMask

class EditTextCurrencyField(context: Context, attrs: AttributeSet) : BaseEditTextFormField(context, attrs) {

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, VALIDATE_CURRENCY_ERROR)
    }

    override fun setup() {
        super.setup()

        val _editText = mEditText?.let { it } ?: return
        _editText.id = R.id.etCurrency
        _editText.addTextChangedListener(PriceTextWatcherMask(_editText))
    }
}
