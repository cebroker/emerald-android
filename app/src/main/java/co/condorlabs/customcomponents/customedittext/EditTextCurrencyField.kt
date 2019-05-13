/*
 * Copyright 2019 CondorLabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.condorlabs.customcomponents.customedittext

import android.content.Context
import android.util.AttributeSet
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.DOLLAR_SYMBOL
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.VALIDATE_CURRENCY_ERROR
import co.condorlabs.customcomponents.VALIDATE_EMPTY_ERROR
import co.condorlabs.customcomponents.helper.masks.PriceTextWatcherMask
import java.util.regex.Pattern

class EditTextCurrencyField(context: Context, attrs: AttributeSet) : BaseEditTextFormField(context, attrs) {

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, VALIDATE_CURRENCY_ERROR)
    }

    override fun setup() {
        super.setup()

        val _editText = editText?.let { it } ?: return
        _editText.id = R.id.etCurrency
        _editText.addTextChangedListener(PriceTextWatcherMask(_editText))
        _editText.setText(DOLLAR_SYMBOL)
    }

    override fun isValid(): ValidationResult {
        return when {
            isFieldEmpty(editText?.text.toString()) && isRequired -> ValidationResult(
                false,
                String.format(VALIDATE_EMPTY_ERROR, hint)
            )
            editText?.text.toString().isNotEmpty() && _regex != null &&
                    !Pattern.compile(_regex).matcher(editText?.text.toString()).matches() -> getErrorValidateResult()
            else -> ValidationResult(true, EMPTY)
        }
    }

    private fun isFieldEmpty(text: String?): Boolean {
        return text == DOLLAR_SYMBOL
    }
}
