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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.Selectable
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.*

abstract class BaseRadioGroupFormField(
    context: Context, private val attrs: AttributeSet
) : TextInputLayout(context, attrs), FormField<String> {

    protected var mValueChangeListener: ValueChangeListener<String>? = null
    private var selectables: List<Selectable>? = null
    private var radioGroup: RadioGroup? = null
    private var labelText = EMPTY
    private var spaceBetweenItems = DEFAULT_SPACE_BETWEEN_ITEMS
    private val layoutParams = LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT
    )
    private val tvLabel = TextView(context, attrs).apply {
        id = R.id.tvLabelRadioGroup
    }

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BaseRadioGroupFormField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )

        isRequired = typedArray.getBoolean(R.styleable.BaseRadioGroupFormField_is_required, false)
        labelText = typedArray.getString(R.styleable.BaseRadioGroupFormField_title) ?: EMPTY
        spaceBetweenItems =
            typedArray.getInteger(R.styleable.BaseRadioGroupFormField_space_between_items, DEFAULT_SPACE_BETWEEN_ITEMS)
        typedArray.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setup()
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, String.format(MESSAGE_FORMAT_ERROR, labelText))
    }

    override fun isValid(): ValidationResult {
        when {
            isRequired -> {
                if (radioGroup?.checkedRadioButtonId == NO_RADIO_GROUP_SELECTED_VALUE_FOUND_RETURNED_VALUE) {
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
        tvLabel.text = labelText
        addView(tvLabel, layoutParams)
        radioGroup = RadioGroup(context, attrs).apply {
            id = R.id.rgBase
        }

        radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            selectables?.forEach { it.value = false }

            if (isValidRadioButtonId(checkedId)) {
                val checkedItem = selectables?.get(checkedId)?.let { it } ?: return@setOnCheckedChangeListener
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

        addView(radioGroup, layoutParams)
    }

    override fun getValue(): String {
        return selectables?.firstOrNull { it.value }?.label ?: EMPTY
    }

    override fun setValueChangeListener(valueChangeListener: ValueChangeListener<String>) {
        mValueChangeListener = valueChangeListener
    }

    fun setSelectables(selectables: List<Selectable>) {
        this.selectables = selectables
        addRadioButtons()
    }

    private fun addRadioButtons() {
        radioGroup?.removeAllViews()
        selectables?.forEachIndexed { index, selectable ->
            radioGroup?.addView(
                RadioButton(context, null, ZERO, R.style.radio_button_custom_style).apply {
                    id = index
                    text = selectable.label
                    isChecked = selectable.value
                    layoutParams = LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                    ).apply {
                        if (index < (selectables?.size?.minus(ONE) ?: ZERO))
                            setMargins(ZERO, ZERO, ZERO, spaceBetweenItems)
                    }
                }
            )
        }
    }

    private fun isValidRadioButtonId(index: Int): Boolean {
        return index <= selectables?.size ?: ZERO && index >= ZERO &&
                selectables?.size ?: ZERO > ZERO
    }
}
