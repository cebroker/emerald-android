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

package co.condorlabs.customcomponents.formfield

import android.view.View
import co.condorlabs.customcomponents.customedittext.ValueChangeListener

interface FormField<ReturnValueType> {

    var isRequired: Boolean

    fun isValid(): ValidationResult

    fun showError(message: String)

    fun clearError()

    fun setup()

    fun getValue(): ReturnValueType

    fun getErrorValidateResult(): ValidationResult

    fun setValueChangeListener(valueChangeListener: ValueChangeListener<ReturnValueType>)

    fun setIsRequired(required: Boolean)
}
