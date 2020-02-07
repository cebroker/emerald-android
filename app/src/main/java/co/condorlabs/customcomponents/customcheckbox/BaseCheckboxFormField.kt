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
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.Selectable
import co.condorlabs.customcomponents.formfield.ValidationResult
import com.google.android.material.textfield.TextInputLayout

abstract class BaseCheckboxFormField(context: Context, attrs: AttributeSet) :
    TextInputLayout(context, attrs), FormField<List<Selectable>>,
    CompoundButton.OnCheckedChangeListener {

    private var checkboxValueChangeListener: ValueChangeListener<List<Selectable>>? = null

    private var selectables: List<Selectable>? = null
    private var labelText: String? = EMPTY

    private val txtViewLabel = TextView(context, attrs).apply {
        id = R.id.tvLabel
    }

    private val layoutParams = LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT
    )

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BaseCheckboxFormField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )
        isRequired = typedArray.getBoolean(R.styleable.BaseCheckboxFormField_is_required, false)
        labelText = typedArray.getString(R.styleable.BaseCheckboxFormField_title)

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
                if (selectables?.filter { !it.value }?.size ?: ZERO == selectables?.size ?: ZERO) {
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
        labelText?.let {
            txtViewLabel.text = it
        }
    }

    override fun getValue(): List<Selectable> {
        return selectables ?: arrayListOf()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val checkbox = buttonView?.let { it } ?: return

        val selectableChecked = selectables?.find {
            it.label == checkbox.text
        }?.let { it } ?: return

        selectableChecked.value = !selectableChecked.value

        val isValid = isValid()

        if (isValid.error.isNotEmpty()) {
            showError(isValid.error)
        } else {
            clearError()
        }

        val selectables = selectables?.let { it } ?: return

        checkboxValueChangeListener?.onValueChange(selectables)
    }

    override fun setValueChangeListener(valueChangeListener: ValueChangeListener<List<Selectable>>) {
        checkboxValueChangeListener = valueChangeListener
    }

    fun setSelectables(selectables: List<Selectable>) {
        this.selectables = selectables
        addCheckboxes()
    }

    private fun setPaddingTop(): Int {
        val paddingDp = PADDING_TOP
        val density = context.resources.displayMetrics.density
        return (paddingDp * density).toInt()
    }

    private fun setFont() {
        val font = Typeface.createFromAsset(context.assets, OPEN_SANS_SEMI_BOLD)
        typeface = font
    }

    private fun addCheckboxes() {
        removeAllViews()
        setFont()
        addView(txtViewLabel, layoutParams)
        selectables?.forEachIndexed { index, selectable ->
            addView(AppCompatCheckBox(context).apply {
                id = index
                text = selectable.label
                isChecked = selectable.value
                gravity = Gravity.TOP
                setPadding(
                    DEFAULT_PADDING, setPaddingTop(),
                    DEFAULT_PADDING,
                    DEFAULT_PADDING
                )
                setStyleCheckBox(this, R.color.primaryColor)
                setOnCheckedChangeListener(this@BaseCheckboxFormField)
            }, layoutParams)
        }
    }

    private fun setStyleCheckBox(
        checkBox: AppCompatCheckBox,
        buttonTintColor: Int
    ) {
        with(checkBox) {
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(android.R.attr.state_checked)
                ),
                intArrayOf(
                    ContextCompat.getColor(context, R.color.gray_color_with_alpha),
                    ContextCompat.getColor(context, R.color.gray_color_with_alpha),
                    ContextCompat.getColor(context, buttonTintColor)
                )
            )
            this.buttonTintList = colorStateList
        }
    }
}
