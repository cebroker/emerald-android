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

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.res.ResourcesCompat
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.Selectable
import co.condorlabs.customcomponents.formfield.ValidationResult
import com.google.android.material.textfield.TextInputLayout

abstract class BaseRadioGroupFormField(
    context: Context,
    private val attrs: AttributeSet
) : TextInputLayout(context, attrs), FormField<String> {

    private var mValueChangeListener: ValueChangeListener<String>? = null
    private var selectables: List<Selectable>? = null
    private var radioGroup: RadioGroup? = null
    private var title = EMPTY
    private var spaceBetweenItems = DEFAULT_SPACE_BETWEEN_ITEMS
    private val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    private val labelLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
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
        title = typedArray.getString(R.styleable.BaseRadioGroupFormField_title) ?: EMPTY
        spaceBetweenItems =
            typedArray.getInteger(R.styleable.BaseRadioGroupFormField_space_between_items, DEFAULT_SPACE_BETWEEN_ITEMS)
        typedArray.recycle()
        setup()
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, String.format(MESSAGE_FORMAT_ERROR, title))
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

    final override fun setup() {
        labelLayoutParams.bottomMargin = DEFAULT_PADDING_RADIO_BUTTON
        tvLabel.text = title
        addView(tvLabel, labelLayoutParams)
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

    fun setSelectedItem(itemLabel: String) {
        val currentSelectableList = selectables ?: return

        val itemIndex = currentSelectableList.indexOfFirst {
            it.label == itemLabel
        }

        if (!isAValidIndex(itemIndex)) {
            return
        }

        selectables = currentSelectableList.mapIndexed { index, selectable ->
            selectable.copy(value = index == itemIndex)
        }

        addRadioButtons()
    }

    private fun isAValidIndex(index: Int): Boolean {
        return index >= ZERO
    }

    private fun setDefaultPadding(): Int {
        val paddingDp = DEFAULT_PADDING_RADIO_BUTTON
        val density = context.resources.displayMetrics.density
        return (paddingDp * density).toInt()
    }

    @SuppressLint("ResourceType")
    private fun addRadioButtons() {
        val radioGroup = radioGroup ?: return

        clearPreviousSelection(radioGroup)
        radioGroup.removeAllViews()

        val selectables = selectables ?: return

        selectables.forEachIndexed { index, selectable ->
            radioGroup.addView(
                AppCompatRadioButton(context).apply {
                    isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val attrs = intArrayOf(R.attr.selectableItemBackground)
                        val typedArray = context.obtainStyledAttributes(attrs)
                        val selectableItemBackground = typedArray.getResourceId(0, 0)
                        typedArray.recycle()

                        this.foreground = context.getDrawable(selectableItemBackground)
                    }
                    id = index
                    text = selectable.label
                    isChecked = selectable.value
                    layoutParams = LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                    ).apply {
                        if (index < selectables.size.minus(ONE))
                            setMargins(ZERO, ZERO, ZERO, spaceBetweenItems)
                    }
                    setRadioButtonStateProperties(
                        this,
                        R.color.primaryColor,
                        R.drawable.radio_button_selector_background
                    )
                }
            )
        }
    }

    private fun clearPreviousSelection(radioGroup: RadioGroup) {
        if (radioGroup.childCount > 0) {
            radioGroup.setOnCheckedChangeListener { radioGroup, i ->  }
            radioGroup.clearCheck()
        }
    }

    private fun isValidRadioButtonId(index: Int): Boolean {
        return index <= selectables?.size ?: ZERO && index >= ZERO &&
                selectables?.size ?: ZERO > ZERO
    }

    fun getTitle() = title

    fun enableRadioGroupItems(isEnabled: Boolean) {
        selectables?.forEachIndexed { index, _ ->
            with(radioGroup?.findViewById<AppCompatRadioButton>(index)) {
                if (isEnabled) {
                    setRadioButtonStateProperties(
                        this ?: return,
                        R.color.primaryColor,
                        R.drawable.radio_button_selector_background
                    )
                } else {
                    setRadioButtonStateProperties(
                        this ?: return,
                        R.color.gray_color_with_alpha,
                        R.drawable.radio_button_selector_background_disabled
                    )
                }
            }
        }
    }

    private fun setRadioButtonStateProperties(
        radioButton: AppCompatRadioButton,
        buttonTintColor: Int,
        backgroundDrawable: Int
    ) {
        val resultDefaultPadding = setDefaultPadding()
        with(radioButton) {
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_checked)
                ),
                intArrayOf(
                    resources.getColor(R.color.gray_color_with_alpha),
                    resources.getColor(buttonTintColor)
                )
            )
            this.buttonTintList = colorStateList
            this.invalidate()
            background = ResourcesCompat.getDrawable(resources, backgroundDrawable, null)
            setPadding(resultDefaultPadding, resultDefaultPadding, resultDefaultPadding, resultDefaultPadding)
        }
    }
}
