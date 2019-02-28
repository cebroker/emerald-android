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

package co.condorlabs.customcomponents.customcheckbox

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.Selectable
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.*

abstract class BaseCheckboxFormField(context: Context, private val mAttrs: AttributeSet) :
    TextInputLayout(context, mAttrs), FormField<List<Selectable>> {

    protected var textOptions: Array<CharSequence>? = null
    private var mOptionsCount = 0
    private var mLabelText: String? = EMPTY

    private val mTVLabel = TextView(context, mAttrs).apply {
        id = R.id.tvLabel
    }

    private val mLayoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )


    init {
        val typedArray = context.obtainStyledAttributes(
            mAttrs,
            R.styleable.BaseCheckboxFormField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )
        mOptionsCount = typedArray.getInt(R.styleable.BaseCheckboxFormField_count_options, 0)
        textOptions = typedArray.getTextArray(R.styleable.BaseCheckboxFormField_values)
        mLabelText = typedArray.getString(R.styleable.BaseCheckboxFormField_title)

        typedArray.recycle()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setup()
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, String.format(MESSAGE_FORMAT_ERROR, mLabelText))
    }

    override fun isValid(): ValidationResult {
        when {
            mIsRequired -> {
                if (mOptionsCount == countIfIsChecked()) {
                    return getErrorValidateResult()
                }
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

    override fun setup() {
        mLabelText?.let {
            mTVLabel.text = it
            addView(mTVLabel, mLayoutParams)
        }

        for (i in VIEW_GROUP_FIRST_VIEW_POSITION until mOptionsCount) {
            addView(CheckBox(context).apply {
                id = i
                text = textOptions?.get(i) ?: i.toString()
            }, mLayoutParams)
        }
    }


    private fun countIfIsChecked(): Int {
        var count = 0
        (VIEW_GROUP_FIRST_VIEW_POSITION until childCount).forEach { i ->
            (getChildAt(i) as? CheckBox)?.let {
                if (!it.isChecked) {
                    count++
                }
            }
        }

        return count
    }
}
