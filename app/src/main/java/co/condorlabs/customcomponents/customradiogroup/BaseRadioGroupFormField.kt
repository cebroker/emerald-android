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

package co.condorlabs.customcomponents.customradiogroup

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.Selectable
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.*

abstract class BaseRadioGroupFormField(context: Context, private val mAttrs: AttributeSet) :
    TextInputLayout(context, mAttrs), FormField<String> {

    protected var mValueChangeListener: ValueChangeListener<String>? = null

    private var mSelectables: List<Selectable>? = null
    private var mRadioGroup: RadioGroup? = null
    private var mLabelText = EMPTY

    private val mLayoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    private val mTVLabel = TextView(context, mAttrs).apply {
        id = R.id.tvLabelRadioGroup
    }

    init {
        val typedArray = context.obtainStyledAttributes(
            mAttrs,
            R.styleable.BaseRadioGroupFormField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )

        isRequired = typedArray.getBoolean(R.styleable.BaseRadioGroupFormField_is_required, false)
        mLabelText = typedArray.getString(R.styleable.BaseRadioGroupFormField_title) ?: EMPTY

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
            isRequired -> {
                if (mRadioGroup?.checkedRadioButtonId == NO_RADIO_GROUP_SELECTED_VALUE_FOUND_RETURNED_VALUE) {
                    return getErrorValidateResult()
                } else {
                    clearError()
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
        mTVLabel.text = mLabelText
        addView(mTVLabel, mLayoutParams)
        mRadioGroup = RadioGroup(context, mAttrs).apply {
            id = R.id.rgBase
        }

        mRadioGroup?.setOnCheckedChangeListener { _, checkedId ->

            mSelectables?.forEach { it.value = false }

            if (isValidRadioButtonId(checkedId)) {
                val checkedItem = mSelectables?.get(checkedId)?.let { it } ?: return@setOnCheckedChangeListener
                checkedItem.value = true
                mValueChangeListener?.onValueChange(checkedItem.label)
            } else {
                if (isRequired) {
                    showError(getErrorValidateResult().error)
                } else {
                    clearError()
                }
            }
        }

        addView(mRadioGroup, mLayoutParams)
    }

    override fun getValue(): String {
        return mSelectables?.firstOrNull { it.value }?.label ?: EMPTY
    }

    override fun setValueChangeListener(valueChangeListener: ValueChangeListener<String>) {
        mValueChangeListener = valueChangeListener
    }

    fun setSelectables(selectables: List<Selectable>) {
        mSelectables = selectables
        addRadioButtons()
    }

    private fun addRadioButtons() {
        mRadioGroup?.removeAllViews()
        mSelectables?.forEachIndexed { index, selectable ->
            mRadioGroup?.addView(RadioButton(context).apply {
                id = index
                text = selectable.label
                isChecked = selectable.value
            }, mLayoutParams)
        }
    }

    private fun isValidRadioButtonId(index: Int): Boolean {
        return index <= mSelectables?.size ?: ZERO && index >= ZERO &&
                mSelectables?.size ?: ZERO > ZERO
    }
}
