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
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.Selectable
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.*

abstract class BaseCheckboxFormField(context: Context, attrs: AttributeSet) :
    TextInputLayout(context, attrs), FormField<List<Selectable>>, CompoundButton.OnCheckedChangeListener {

    private var mSelectables: List<Selectable>? = null
    private var mLabelText: String? = EMPTY


    private val mTVLabel = TextView(context, attrs).apply {
        id = R.id.tvLabel
    }

    private val mLayoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BaseCheckboxFormField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )

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
                if (mSelectables?.filter { !it.value }?.size ?: ZERO == mSelectables?.size ?: ZERO) {
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
    }

    override fun getValue(): List<Selectable> {
        return mSelectables ?: arrayListOf()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val checkbox = buttonView?.let { it } ?: return

        val selectableChecked = mSelectables?.find {
            it.label == checkbox.text
        }?.let { it } ?: return


        selectableChecked.value = !selectableChecked.value
    }

    fun setSelectables(selectables: List<Selectable>) {
        mSelectables = selectables
        addCheckboxes()
    }

    private fun addCheckboxes() {
        removeAllViews()
        mSelectables?.forEachIndexed { index, selectable ->
            addView(CheckBox(context).apply {
                id = index
                text = selectable.label
                setOnCheckedChangeListener(this@BaseCheckboxFormField)
            }, mLayoutParams)
        }

    }
}
