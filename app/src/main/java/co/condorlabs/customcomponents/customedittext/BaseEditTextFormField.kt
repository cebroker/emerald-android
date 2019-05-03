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
import android.graphics.Typeface
import com.google.android.material.textfield.TextInputLayout
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.EditText
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.*
import java.util.regex.Pattern

/**
 * @author Oscar Gallon on 2/26/19.
 */
open class BaseEditTextFormField(context: Context, private val attrs: AttributeSet) :
    TextInputLayout(ContextThemeWrapper(context, R.style.TextFormFieldTheme), attrs), FormField<String>,
    View.OnFocusChangeListener {

    override var isRequired: Boolean = false

    protected var mRegex: String? = null
    protected var mEditText: EditText? = null
    protected var mHint: String = context.getString(R.string.default_base_hint)
    protected var mValueChangeListener: ValueChangeListener<String>? = null

    private var mMaxLines: Int? = null
    private var mMinLines: Int? = null
    private var mBackgroundAlpha: Int? = null

    private var mMultiline: Boolean = false
    private var mInputType: Int = InputType.TYPE_CLASS_TEXT
    private val mLayoutParams = LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT
    )

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BaseEditTextFormField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )

        mHint = typedArray.getString(R.styleable.BaseEditTextFormField_hint)
            ?: context.getString(R.string.default_base_hint)
        mRegex = typedArray.getString(R.styleable.BaseEditTextFormField_regex)
        isRequired = typedArray.getBoolean(R.styleable.BaseEditTextFormField_is_required, false)
        mInputType = when (typedArray.getString(R.styleable.BaseEditTextFormField_input_type)) {
            "number" -> InputType.TYPE_CLASS_NUMBER
            "numberDecimal" -> InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
            "phone" -> InputType.TYPE_CLASS_PHONE
            "password" -> InputType.TYPE_TEXT_VARIATION_PASSWORD
            else -> InputType.TYPE_CLASS_TEXT
        }
        mMaxLines = typedArray.getString(R.styleable.BaseEditTextFormField_max_lines)?.let { it.toInt() }
        mMinLines = typedArray.getString(R.styleable.BaseEditTextFormField_min_lines)?.let { it.toInt() }
        mBackgroundAlpha = typedArray.getString(R.styleable.BaseEditTextFormField_background_alpha)?.let { it.toInt() }
        mMultiline = typedArray.getBoolean(R.styleable.BaseEditTextFormField_multiline, false)

        typedArray.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setup()
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, String.format(VALIDATE_INCORRECT_ERROR, mHint))
    }

    override fun setup() {
        mEditText = EditText(ContextThemeWrapper(context, R.style.TextFormFieldTheme))
        mEditText?.inputType = mInputType
        setFont(OPEN_SANS_REGULAR)

        val _editText = mEditText?.let { it } ?: return
        _editText.onFocusChangeListener = this

        _editText.apply {
            id = R.id.etBase
            hint = mHint
            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.default_text_size))
            isMultiline(_editText)
            mBackgroundAlpha?.let { background.alpha = it }
        }

        _editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val newValue = s?.toString()?.let { it } ?: return
                mValueChangeListener?.onValueChange(newValue)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        addView(_editText, mLayoutParams)
    }

    private fun isMultiline(editText: EditText) {
        if (mMultiline) {
            editText.apply {
                setSingleLine(false)
                setLines(mMinLines ?: LINES_DEFAULT)
                maxLines = mMaxLines ?: MAXLINES_DEFAULT
            }
        }
    }

    override fun getValue(): String {
        return mEditText?.text?.toString() ?: EMPTY
    }

    override fun isValid(): ValidationResult {
        return when {
            mEditText?.text.toString().isEmpty() && isRequired -> ValidationResult(
                false,
                String.format(VALIDATE_EMPTY_ERROR, mHint)
            )
            mEditText?.text.toString().isNotEmpty() && mRegex != null && !Pattern.compile(mRegex).matcher(mEditText?.text.toString()).matches() -> getErrorValidateResult()
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

    override fun setIsRequired(required: Boolean) {
        isRequired = required
    }

    override fun setValueChangeListener(valueChangeListener: ValueChangeListener<String>) {
        mValueChangeListener = valueChangeListener
    }

    fun setMinLines(minLines: Int) {
        mEditText?.minLines = minLines
    }

    fun setMaxLines(maxLines: Int) {
        mEditText?.maxLines = maxLines
    }

    fun setBackgroundAlpha(backgroundAlpha: Int) {
        mEditText?.background?.alpha = backgroundAlpha
    }

    private fun setFont(fontName: String) {
        val font = Typeface.createFromAsset(context.assets, fontName)
        typeface = font
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            val isValid = isValid()

            if (isValid.error.isNotEmpty()) {
                showError(isValid.error)
            } else {
                clearError()
            }
        }
    }
}
