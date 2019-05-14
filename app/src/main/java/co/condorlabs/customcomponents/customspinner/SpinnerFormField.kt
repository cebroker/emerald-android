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
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import co.condorlabs.customcomponents.DEFAULT_STYLE_ATTR
import co.condorlabs.customcomponents.DEFAULT_STYLE_RES
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.ValidationResult

/**
 * @author Oscar Gallon on 2/26/19.
 */
class SpinnerFormField(
    context: Context,
    attrs: AttributeSet
) : BaseSpinnerFormField(context, attrs), ItemSelectedListenerAdapter {

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

        autoCompleteTextView?.apply {
            id = R.id.spState
            setAdapter(
                SpinnerFormFieldAdapter(
                    context,
                    android.R.layout.simple_spinner_dropdown_item,
                    hint = mAdapterHint
                )
            )
            onItemSelectedListener = this@SpinnerFormField
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        super.onItemSelected(parent, view, position, id)

        val spinnerData = (autoCompleteTextView?.adapter?.getItem(position) as? SpinnerData)?.let { it } ?: return
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

    fun setData(data: List<SpinnerData>) {
        (autoCompleteTextView?.adapter as? SpinnerFormFieldAdapter)?.replaceStates(data.sortedBy { it.label })
    }

    fun setItemSelectedById(id: String) {
        val data = (autoCompleteTextView?.adapter as? SpinnerFormFieldAdapter)?.getData()?.let { it } ?: return
        val item = data.find { it.id == id }?.let { it } ?: return
        autoCompleteTextView?.setSelection(data.indexOf(item))
    }
}
