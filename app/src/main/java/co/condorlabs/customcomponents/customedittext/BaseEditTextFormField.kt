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
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.EditText
import android.widget.LinearLayout
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.DEFAULT_STYLE_ATTR
import co.condorlabs.customcomponents.helper.DEFAULT_STYLE_RES
import co.condorlabs.customcomponents.helper.EMPTY
import co.condorlabs.customcomponents.helper.VALIDATE_EMPTY_ERROR
import java.util.regex.Pattern

/**
 * @author Oscar Gallon on 2/26/19.
 */
open class BaseEditTextFormField(context: Context, private val mAttrs: AttributeSet) :
    TextInputLayout(context, mAttrs), FormField<String> {

    override var mIsRequired: Boolean = false

    protected var mRegex: String? = null
    protected var mEditText: EditText? = null
    protected var mHint: String = context.getString(R.string.default_base_hint)

    private var mInputType: Int = InputType.TYPE_CLASS_TEXT
    private val mLayoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    init {
        val typedArray = context.obtainStyledAttributes(
            mAttrs,
            R.styleable.BaseEditTextFormField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )

        mHint = typedArray.getString(R.styleable.BaseEditTextFormField_hint)
            ?: context.getString(R.string.default_base_hint)
        mRegex = typedArray.getString(R.styleable.BaseEditTextFormField_regex)
        mIsRequired = typedArray.getBoolean(R.styleable.BaseCheckboxFormField_is_required, false)
        mInputType = when (typedArray.getString(R.styleable.BaseEditTextFormField_input_type)) {
            "number" -> InputType.TYPE_CLASS_NUMBER
            "phone" -> InputType.TYPE_CLASS_PHONE
            else -> InputType.TYPE_CLASS_TEXT
        }

        typedArray.recycle()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setup()
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, String.format(VALIDATE_EMPTY_ERROR, mHint))
    }

    override fun setup() {
        mEditText = EditText(context, mAttrs)
        mEditText?.inputType = mInputType

        val _editText = mEditText?.let { it } ?: return

        _editText.apply {
            id = R.id.etBase
            hint = mHint
            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.default_text_size))
        }

        addView(_editText, mLayoutParams)
    }

    override fun getValue(): String {
        return mEditText?.text?.toString() ?: EMPTY
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

    fun setMaxLength(length: Int) {
        val filter = InputFilter.LengthFilter(length)
        mEditText?.filters = arrayOf(filter)
    }

    fun setRegex(regex: String) {
        mRegex = regex
    }

    fun setIsRequired(required: Boolean) {
        mIsRequired = required
    }
}
