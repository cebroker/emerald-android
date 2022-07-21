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
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import co.condorlabs.customcomponents.DEFAULT_STYLE_ATTR
import co.condorlabs.customcomponents.DEFAULT_STYLE_RES
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.ZERO
import co.condorlabs.customcomponents.formfield.ValidationResult

/**
 * @author Oscar Gallon on 2/26/19.
 */
class SpinnerFormField(
    context: Context,
    attrs: AttributeSet
) : BaseSpinnerFormField(context, attrs), ItemSelectedListenerAdapter,
    AdapterView.OnItemClickListener {

    override var isRequired: Boolean = false
    private var firstEvaluation: Boolean = true
    private var selectedItem: SpinnerData? = null

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.SpinnerFormField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )

        isRequired = typedArray.getBoolean(R.styleable.SpinnerFormField_is_required, false)
        typedArray.recycle()
    }

    override fun setup() {
        super.setup()
        this.setOnClickListener(this)

        autoCompleteTextView?.onItemClickListener = this
        autoCompleteTextView?.apply {
            setAdapter(
                SpinnerFormFieldAdapter(
                    context,
                    android.R.layout.simple_spinner_dropdown_item,
                    hint = context.getString(R.string.spinner_default_hint)
                )
            )
            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.body))
            onItemSelectedListener = this@SpinnerFormField
        }
        setEnable(spinnerEnable)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onItemSelected(parent, view, position, id)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        super.onItemSelected(parent, view, position, id)

        val adapter = (autoCompleteTextView?.adapter as? SpinnerFormFieldAdapter) ?: return

        if (adapter.isHintPosition(position)) {
            clearField()
            return
        }

        val spinnerData = adapter.getItem(position) ?: return

        selectedItem = spinnerData
        mValueChangeListener?.onValueChange(spinnerData)

        if (firstEvaluation) {
            firstEvaluation = false
        } else {
            val isValid = isValid()

            if (isValid.error.isNotEmpty()) {
                showError(isValid.error)
            } else {
                clearError()
            }
        }
    }

    override fun isValid(): ValidationResult {
        if (!isRequired) {
            return ValidationResult(true, EMPTY)
        }

        val wrappedSelectedItem = selectedItem ?: return getErrorValidateResult()

        val adapter = (autoCompleteTextView?.adapter as? SpinnerFormFieldAdapter)
            ?: return getErrorValidateResult()

        val selectedItemPosition = adapter.getPosition(wrappedSelectedItem)

        if (adapter.isHintPosition(selectedItemPosition)) {
            return getErrorValidateResult()
        }

        if ((autoCompleteTextView?.text?.isEmpty() == true)) {
            return getErrorValidateResult()
        }

        return ValidationResult(true, EMPTY)
    }

    override fun getValue(): SpinnerData? {
        return if (!isValid().isValid) {
            null
        } else {
            selectedItem
        }
    }

    override fun setIsRequired(required: Boolean) {
        isRequired = required
    }

    override fun onClick(v: View?) {
        autoCompleteTextView?.performClick()
    }

    fun setData(data: List<SpinnerData>, isAlphabeticSorted: Boolean = true) {
        (autoCompleteTextView?.adapter as? SpinnerFormFieldAdapter)?.replaceStates(
            data.sortedBy {
                it.takeIf { isAlphabeticSorted }?.label
            }
        )
    }

    fun clearField() {
        autoCompleteTextView?.setText(EMPTY)
        selectedItem = null
        _spinnerFormFieldListener?.onFieldCleared()
        clearFocus()
    }

    fun setItemSelectedById(id: String) {
        val data =
            (autoCompleteTextView?.adapter as? SpinnerFormFieldAdapter)?.getData()?.let { it }
                ?: return
        val item = data.find { it.id == id }?.let { it } ?: return
        autoCompleteTextView?.setText(item.label, false)
        selectedItem = item
        mValueChangeListener?.onValueChange(selectedItem)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!focused) {
            (autoCompleteTextView as? CustomBaseInstantAutoCompleteTextView)?.dismissDropDown()
        }
    }

    fun setEnable(isEnable: Boolean) {
        textInputLayout?.isEnabled = isEnable
        (autoCompleteTextView as? CustomBaseInstantAutoCompleteTextView)?.setEnable(isEnable)
        if (isEnable) {
            autoCompleteTextView?.setCompoundDrawablesWithIntrinsicBounds(
                ZERO,
                ZERO,
                R.drawable.ic_down_arrow,
                ZERO
            )
        } else {
            autoCompleteTextView?.setCompoundDrawablesWithIntrinsicBounds(
                ZERO,
                ZERO,
                R.drawable.ic_gray_down_arrow,
                ZERO
            )
        }
    }

    fun setHintValue(text: String) {
        hint = text
    }
}
