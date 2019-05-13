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
import android.text.InputType
import android.util.AttributeSet
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.VALIDATE_EMAIL_ERROR

class EditTextEmailField(context: Context, attrs: AttributeSet) : BaseEditTextFormField(context, attrs) {

    override fun setup() {
        super.setup()
        editText?.id = R.id.etEmail
        setInputType()
        setRegex(android.util.Patterns.EMAIL_ADDRESS.toString())
    }

    private fun setInputType() {
        editText?.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, VALIDATE_EMAIL_ERROR)
    }
}
