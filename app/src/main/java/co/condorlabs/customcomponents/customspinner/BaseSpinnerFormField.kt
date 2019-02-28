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
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.*

/**
 * @author Oscar Gallon on 2/26/19.
 */
abstract class BaseSpinnerFormField(context: Context, private val mAttrs: AttributeSet) :
    TextInputLayout(context, mAttrs),
    FormField<String> {

    protected var mSpinner: Spinner? = null
    protected var mAdapterHint: String

    private val mLayoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    private val mTVLabel = TextView(context, mAttrs)?.apply {
        id = R.id.tvLabel
    }

    private var mLabelText = EMPTY

    init {
        val typedArray = context.obtainStyledAttributes(
            mAttrs,
            R.styleable.StateSpinnerFormField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )

        mLabelText = typedArray.getString(R.styleable.StateSpinnerFormField_label)
            ?: context.getString(R.string.default_base_hint)
        mAdapterHint = typedArray.getString(R.styleable.StateSpinnerFormField_hint)
            ?: context.getString(R.string.spinner_default_hint)

        typedArray.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setup()
    }

    override fun setup() {
        mTVLabel.text = mLabelText
        addView(mTVLabel, mLayoutParams)
        mSpinner = Spinner(context, mAttrs)
        addView(mSpinner)
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, String.format(MESSAGE_FORMAT_ERROR, mLabelText))
    }

    override fun isValid(): ValidationResult {
        mSpinner?.let {
            if (it.selectedItemPosition == AdapterView.INVALID_POSITION) {
                getErrorValidateResult()
            }
        }

        return ValidationResult(true, EMPTY)
    }

    override fun showError(message: String) {
        this.isErrorEnabled = true
        this.error = message
    }

    override fun clearError() {
        this.isErrorEnabled = false
        this.error = EMPTY
    }
}
