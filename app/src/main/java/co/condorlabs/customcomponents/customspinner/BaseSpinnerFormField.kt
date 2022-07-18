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

package co.condorlabs.customcomponents.customspinner

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import androidx.camera.core.CameraX.init
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import com.google.android.material.textfield.TextInputLayout

/**
 * @author Oscar Gallon on 2/26/19.
 */
abstract class BaseSpinnerFormField(context: Context, mAttrs: AttributeSet?) :
    LinearLayout(context, mAttrs),
    FormField<SpinnerData?>, View.OnClickListener {

    var textInputLayout: TextInputLayout? = null

    protected var autoCompleteTextView: AutoCompleteTextView? = null
    protected var hint: String
    protected var spinnerEnable: Boolean = true
    protected var mValueChangeListener: ValueChangeListener<SpinnerData?>? = null
    protected var _spinnerFormFieldListener: SpinnerFormFieldListener? = null

    private var layoutParams = LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT
    )

    init {
        val typedArray = context.obtainStyledAttributes(
            mAttrs,
            R.styleable.SpinnerFormField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )

        hint = typedArray.getString(R.styleable.SpinnerFormField_hint)
            ?: context.getString(R.string.spinner_default_hint)
        spinnerEnable = typedArray.getBoolean(R.styleable.SpinnerFormField_enable, true)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setup()
    }

    override fun setup() {
        textInputLayout =
            LayoutInflater.from(context).inflate(R.layout.base_spinner_form_field, null) as? TextInputLayout
        autoCompleteTextView = textInputLayout?.findViewById(R.id.actvBase)
        autoCompleteTextView?.id = View.generateViewId()
        textInputLayout?.hint = hint
        setFont(OPEN_SANS_REGULAR)
        addView(textInputLayout, layoutParams)
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, String.format(MESSAGE_FORMAT_ERROR, hint))
    }

    override fun isValid(): ValidationResult {
        return ValidationResult(true, EMPTY)
    }

    override fun showError(message: String) {
        textInputLayout?.isErrorEnabled = true
        textInputLayout?.error = message
    }

    override fun clearError() {
        textInputLayout?.isErrorEnabled = false
        textInputLayout?.error = EMPTY
    }

    fun setSpinnerFormFieldListener(spinnerFormFieldListener: SpinnerFormFieldListener) {
        _spinnerFormFieldListener = spinnerFormFieldListener
    }

    override fun setValueChangeListener(valueChangeListener: ValueChangeListener<SpinnerData?>) {
        mValueChangeListener = valueChangeListener
    }

    private fun setFont(fontName: String) {
        val font = Typeface.createFromAsset(context.assets, fontName)
        autoCompleteTextView?.typeface = font
    }
}
