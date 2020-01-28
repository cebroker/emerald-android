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
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.masks.PhoneNumberTextWatcherMask

class EditTextPhoneField(context: Context, attrs: AttributeSet) :
    BaseEditTextFormField(context, attrs) {

    private var mask: String = DEFAULT_PHONE_MASK

    override var text: String? = EMPTY
        set(value) {
            field = value
            val phoneHasNumbers = value?.any { it.isDigit() } ?: false

            if (!value.isNullOrEmpty() && phoneHasNumbers) {
                textInputLayout?.editText?.setText(value.filter { it.isDigit() })
                showPlaceholder()
            } else {
                textInputLayout?.editText?.text = null
                resetPlaceholder()
            }
        }
        get() = getValue()

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EditTextPhoneField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        ).apply {
            getString(R.styleable.EditTextPhoneField_phone_mask)?.let { mask_value ->
                mask = mask_value
            }
            recycle()
        }

        regexListToMatch.add(PHONE_NUMBER_REGEX)
    }

    override fun setup() {
        super.setup()
        editText?.id = R.id.etPhone
        setInputType()
        setPhoneMask()
        setDigits()
    }

    private fun setDigits() {
        this.editText?.keyListener = DigitsKeyListener.getInstance(PHONE_DIGITS)
    }

    private fun setInputType() {
        editText?.inputType = InputType.TYPE_CLASS_NUMBER
    }

    private fun setPhoneMask() {
        this.editText?.apply {
            addTextChangedListener(PhoneNumberTextWatcherMask(mask) {
                try {
                    setSelection(it)
                } catch (t: IndexOutOfBoundsException) {
                    throw PhoneDigitPositionException()
                }
            })
        }
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, VALIDATE_LENGTH_ERROR)
    }

    override fun getValue(): String {
        return this.editText?.text.toString()
            .filterNot { character -> PHONE_STRING_NON_DIGITS.contains(character) }
    }
}
