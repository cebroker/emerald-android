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
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.EMPTY
import co.condorlabs.customcomponents.helper.STATE_SPINNER_HINT_POSITION

/**
 * @author Oscar Gallon on 2/26/19.
 */
class SpinnerFormField(context: Context, attrs: AttributeSet) :
    BaseSpinnerFormField(context, attrs), ItemSelectedListenerAdapter {

    override var isRequired: Boolean = false
    private var firstEvaluation: Boolean = true

    override fun setup() {
        super.setup()
        mSpinner?.id = R.id.spState
        mSpinner?.adapter =
            SpinnerFormFieldAdapter(context, android.R.layout.simple_spinner_dropdown_item, mHint = mAdapterHint)
        mSpinner?.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        super.onItemSelected(parent, view, position, id)
        val spinnerData = (mSpinner?.getItemAtPosition(position) as? SpinnerData)?.let { it } ?: return
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
        mSpinner?.let {
            if (it.selectedItemPosition <= STATE_SPINNER_HINT_POSITION && isRequired) {
                return getErrorValidateResult()
            }
        }

        return ValidationResult(true, EMPTY)
    }

    override fun getValue(): SpinnerData? {
        val spinner = mSpinner?.let { it } ?: return null

        return if (!isValid().isValid) {
            null
        } else {
            spinner.selectedItem as? SpinnerData
        }
    }

    override fun setIsRequired(required: Boolean) {
        isRequired = required
    }

    fun setData(data: List<SpinnerData>) {
        (mSpinner?.adapter as? SpinnerFormFieldAdapter)?.replaceStates(data.sortedBy { it.label })
    }

    fun setItemSelectedById(id: String) {
        val data = (mSpinner?.adapter as? SpinnerFormFieldAdapter)?.getData()?.let { it } ?: return
        val item = data.find { it.id == id }?.let { it } ?: return
        mSpinner?.setSelection(data.indexOf(item))
    }
}
