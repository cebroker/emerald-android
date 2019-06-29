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
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.customedittext.textwatchers.DefaultTextWatcher
import co.condorlabs.customcomponents.customedittext.textwatchers.IconValidationTextWatcher
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

/**
 * @author Oscar Gallon on 2/26/19.
 */
open class BaseEditTextFormField(context: Context, private val attrs: AttributeSet) :
    LinearLayout(context, attrs),
    FormField<String>,
    View.OnFocusChangeListener {

    var hint: String = context.getString(R.string.default_base_hint)
    var textInputLayout: TextInputLayout? = null
    var editText: EditText? = null

    override var isRequired: Boolean = false

    protected var _valueChangeListener: ValueChangeListener<String>? = null

    private var maxLines: Int? = null
    private var minLines: Int? = null
    private var backgroundAlpha: Int? = null
    private var isMultiline: Boolean = false
    private var inputType: Int = InputType.TYPE_CLASS_TEXT
    private var placeholder: String? = null
    private var layoutParams = LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT
    )
    private var showValidationIcon: Boolean = false
    private var textWatcher: DefaultTextWatcher? = null
    protected val regexListToMatch = HashSet<String>()

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BaseEditTextFormField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )

        hint = typedArray.getString(R.styleable.BaseEditTextFormField_hint)
            ?: context.getString(R.string.default_base_hint)
        val regex = typedArray.getString(R.styleable.BaseEditTextFormField_regex)
        isRequired = typedArray.getBoolean(R.styleable.BaseEditTextFormField_is_required, false)
        inputType = when (typedArray.getString(R.styleable.BaseEditTextFormField_input_type)) {
            "number" -> InputType.TYPE_CLASS_NUMBER
            "numberDecimal" -> InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
            "phone" -> InputType.TYPE_CLASS_PHONE
            "password" -> InputType.TYPE_TEXT_VARIATION_PASSWORD
            else -> InputType.TYPE_CLASS_TEXT
        }
        maxLines = typedArray.getString(R.styleable.BaseEditTextFormField_max_lines)?.toInt()
        minLines = typedArray.getString(R.styleable.BaseEditTextFormField_min_lines)?.toInt()
        backgroundAlpha =
            typedArray.getString(R.styleable.BaseEditTextFormField_background_alpha)?.toInt()
        isMultiline = typedArray.getBoolean(R.styleable.BaseEditTextFormField_multiline, false)
        placeholder = typedArray.getString(R.styleable.BaseEditTextFormField_placeholder)
        showValidationIcon = typedArray.getBoolean(R.styleable.BaseEditTextFormField_show_validation_icon, false)

        typedArray.recycle()

        regex?.let {
            regexListToMatch.add(it)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setup()
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, String.format(VALIDATE_INCORRECT_ERROR, hint))
    }

    override fun setup() {
        textInputLayout =
            LayoutInflater.from(context).inflate(R.layout.base_edit_text_form_field, null) as? TextInputLayout
        editText = textInputLayout?.editText
        editText?.inputType = inputType
        editText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.body))
        setFont(OPEN_SANS_REGULAR)

        val wrappedTextInputLayout = textInputLayout ?: return
        val wrappedEditText = editText?.let { it } ?: return

        wrappedEditText.onFocusChangeListener = this
        wrappedTextInputLayout.hint = this@BaseEditTextFormField.hint

        wrappedEditText.apply {
            id = View.generateViewId()
            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.body))
            isMultiline(wrappedEditText)
            tag = BaseEditTextFormField::class.java.name
            backgroundAlpha?.let { background.alpha = it }
        }

        textWatcher = if (showValidationIcon) {
            IconValidationTextWatcher(this, _valueChangeListener)
        } else {
            DefaultTextWatcher(_valueChangeListener)
        }

        wrappedEditText.addTextChangedListener(textWatcher)

        invalidate()
        addView(wrappedTextInputLayout, layoutParams)
    }

    private fun isMultiline(editText: EditText) {
        if (isMultiline) {
            editText.apply {
                setSingleLine(false)
                setLines(this@BaseEditTextFormField.minLines ?: LINES_DEFAULT)
                maxLines = this@BaseEditTextFormField.maxLines ?: MAXLINES_DEFAULT
            }
        }
    }

    override fun getValue(): String {
        return editText?.text?.toString() ?: EMPTY
    }

    override fun isValid(): ValidationResult {
        return when {
            editText?.text.toString().isEmpty() && isRequired -> ValidationResult(
                false,
                VALIDATE_EMPTY_ERROR
            )
            editText?.text.toString().isNotEmpty() &&  !doesTextMatchWithRegex(editText?.text.toString()) -> getErrorValidateResult()
            else -> ValidationResult(true, EMPTY)
        }
    }

    override fun showError(message: String) {
        textInputLayout?.isErrorEnabled = true
        textInputLayout?.error = message
    }

    override fun clearError() {
        textInputLayout?.isErrorEnabled = false
        textInputLayout?.error = EMPTY
    }

    fun setMaxLength(length: Int) {
        val filter = InputFilter.LengthFilter(length)
        editText?.filters = arrayOf(filter)
    }

    fun setRegex(regex: String) {
        regexListToMatch.add(regex)
    }

    fun setRegex(regex: List<String>) {
        regexListToMatch.addAll(regex)
    }

    override fun setIsRequired(required: Boolean) {
        isRequired = required
    }

    override fun setValueChangeListener(valueChangeListener: ValueChangeListener<String>) {
        _valueChangeListener = valueChangeListener
        textWatcher?.setValueChangeListener(valueChangeListener)
    }

    fun setMinLines(minLines: Int) {
        editText?.minLines = minLines
    }

    fun setMaxLines(maxLines: Int) {
        editText?.maxLines = maxLines
    }

    fun setBackgroundAlpha(backgroundAlpha: Int) {
        editText?.background?.alpha = backgroundAlpha
    }

    fun setPlaceholder(placeholder: String) {
        this.placeholder = placeholder
    }

    fun setEnable(isEnable: Boolean) {
        editText?.isEnabled = isEnable
        textInputLayout?.isEnabled = isEnable
    }

    private fun setFont(fontName: String) {
        val font = Typeface.createFromAsset(context.assets, fontName)
        textInputLayout?.typeface = font
        editText?.typeface = font
    }

    private fun doesTextMatchWithRegex(candidateText: String?): Boolean {
        if(candidateText == null ){
            return false
        }

        if (regexListToMatch.isEmpty()) {
            return true
        }

        return regexListToMatch.any { regex ->
            Pattern.compile(regex).matcher(candidateText).matches()
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            handleNoFocusOnView()
            hidePlaceholder()
        } else {
            showPlaceholder()
        }
    }

    private fun showPlaceholder() {
        textInputLayout?.editText?.postDelayed({
            textInputLayout?.editText?.hint = placeholder ?: EMPTY
        }, MILLISECONDS_TO_SHOW_PLACE_HOLDER)
    }

    private fun hidePlaceholder() {
        textInputLayout?.editText?.hint = EMPTY
    }

    private fun handleNoFocusOnView() {
        val isValid = isValid()

        if (isValid.error.isNotEmpty()) {
            showError(isValid.error)
        } else {
            clearError()
        }
    }
}
