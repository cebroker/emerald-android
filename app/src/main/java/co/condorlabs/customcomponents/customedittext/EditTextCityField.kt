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

package co.condorlabs.customcomponents.customedittext

import android.content.Context
import android.util.AttributeSet
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.EMPTY
import co.condorlabs.customcomponents.helper.VALIDATE_CITY_ERROR
import co.condorlabs.customcomponents.helper.ZERO

/**
 * @author Oscar Gallon on 2/25/19.
 */
class EditTextCityField(context: Context, attrs: AttributeSet) : BaseEditTextFormField(context, attrs) {

    private var mStateName: String? = null
    private var mCities: List<String>? = null


    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, "$VALIDATE_CITY_ERROR ${mStateName ?: EMPTY}")
    }

    override fun isValid(): ValidationResult {
        val emptyValidation = super.isValid()

        if (!emptyValidation.isValid) {
            return emptyValidation
        }

        if (mCities?.filter {
                it.toLowerCase() == editText?.text?.toString()?.toLowerCase() ?: EMPTY
            }?.count() ?: ZERO <= ZERO) {
            return getErrorValidateResult()
        }

        return emptyValidation
    }

    override fun setup() {
        super.setup()
        mEditText?.id = R.id.etCity
    }


    fun setCities(cities: List<String>) {
        mCities = cities
    }

    fun setStateName(state: String) {
        mStateName = state
    }
}
