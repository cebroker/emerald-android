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
import android.view.View
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.masks.PriceTextWatcherMask

class EditTextCurrencyField(context: Context, attrs: AttributeSet) :
    BaseEditTextFormField(context, attrs) {

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, VALIDATE_CURRENCY_ERROR)
    }

    override fun setup() {
        super.setup()

        val _editText = editText?.let { it } ?: return
        _editText.id = R.id.etCurrency
        _editText.addTextChangedListener(PriceTextWatcherMask(_editText))
    }

    override fun isValid(): ValidationResult {
        return when {
            isNotFilledWithDigits(editText?.text.toString()) && isRequired -> ValidationResult(
                false,
                String.format(VALIDATE_EMPTY_ERROR, hint)
            )
            editText?.text.toString().isNotEmpty() && !doesTextMatchWithRegex(editText?.text.toString()) -> getErrorValidateResult()
            else -> ValidationResult(true, EMPTY)
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        super.onFocusChange(v, hasFocus)
        if (hasFocus) {
            if (editText?.text.isNullOrEmpty()) {
                editText?.setText(DOLLAR_SYMBOL)
            }
        } else {
            if (editText?.text?.contains('.') == false && editText?.text?.contentEquals("$") == false) {
                editText?.text = editText?.text?.append(".00")
            }
        }
    }

    private fun isNotFilledWithDigits(text: String?): Boolean {
        return text == DOLLAR_SYMBOL || text == EMPTY
    }
}
