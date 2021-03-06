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
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import co.condorlabs.customcomponents.R

/**
 * @author Alexis Duque on 3/8/19.
 * @company Condor Labs
 * @email eduque@condorlabs.io
 */

class EditTextPasswordField(context: Context, attrs: AttributeSet) : BaseEditTextFormField(context, attrs) {

    override fun setup() {
        super.setup()
        editText?.id = R.id.etPassword
        editText?.transformationMethod = PasswordTransformationMethod.getInstance()
        editText?.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        textInputLayout?.isPasswordVisibilityToggleEnabled = true
    }
}
