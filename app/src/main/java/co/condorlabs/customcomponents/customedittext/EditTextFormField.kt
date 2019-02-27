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
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet
import android.widget.EditText
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.DEFAULT_STYLE_ATTR
import co.condorlabs.customcomponents.helper.DEFAULT_STYLE_RES
import co.condorlabs.customcomponents.helper.EMPTY
import co.condorlabs.customcomponents.helper.VALIDATE_EMPTY_ERROR
import java.util.regex.Pattern

abstract class EditTextFormField constructor(context: Context, attrs: AttributeSet) :
    TextInputLayout(context, attrs), TextFormField {

    protected var mRegex: String? = null
    protected var mEditText: EditText? = null
    protected var mHint: String = context.getString(R.string.default_base_hint)

    override var mIsRequired: Boolean = false

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.EditTextFormField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )
        mRegex = typedArray.getString(R.styleable.EditTextFormField_regex)
        mIsRequired = typedArray.getBoolean(R.styleable.EditTextFormField_is_required, false)
        typedArray.recycle()
    }

    override fun isValid(): ValidationResult {
        return when {
            mEditText?.text.toString().isEmpty() && mIsRequired -> ValidationResult(
                false,
                String.format(VALIDATE_EMPTY_ERROR, mHint)
            )
            mIsRequired && mRegex != null && !Pattern.compile(mRegex).matcher(mEditText?.text.toString()).matches() -> getErrorValidateResult()
            else -> ValidationResult(true, EMPTY)
        }
    }

    override fun showError(message: String) {
        this.isErrorEnabled = true
        this.error = message
    }

    override fun clearError() {
        this.isErrorEnabled = false
        this.error = EMPTY
    }

    abstract fun getErrorValidateResult(): ValidationResult

}
