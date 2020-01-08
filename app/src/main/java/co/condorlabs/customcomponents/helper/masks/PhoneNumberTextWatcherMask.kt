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

package co.condorlabs.customcomponents.helper.masks

import android.text.Editable
import android.widget.EditText
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.helper.*

class PhoneNumberTextWatcherMask(private val mReceiver: EditText) : TextWatcherAdapter() {

    override fun afterTextChanged(s: Editable?) {
        s?.let { text ->
            mReceiver.removeTextChangedListener(this)
            var resultadog = text.toString().replace(PHONE_NUMBER_SEPARATOR_TOKEN, "")
            var resultadod = resultadog.replace("(", "")
            var resultado = resultadod.replace(")", "")

            when (resultado.length) {
                in PHONE_NUMBER_REGEX_FIRST_GROUP_RANGE_BOTTOM..PHONE_NUMBER_REGEX_FIRST_GROUP_RANGE_TOP -> {
                    resultado = resultado.replaceFirst(
                        PHONE_NUMBER_REGEX_FIRST_AND_SECOND_GROUP_MATCHER.toRegex(),
                        PHONE_NUMBER_REGEX_FIRST_GROUP_REPLACEMENT_MATCHER
                    )
                }
                in PHONE_NUMBER_REGEX_SECOND_GROUP_RANGE_BOTTOM..PHONE_NUMBER_REGEX_SECOND_GROUP_RANGE_TOP -> {
                    resultado = resultado.replaceFirst(
                        "$PHONE_NUMBER_REGEX_FIRST_AND_SECOND_GROUP_MATCHER$PHONE_NUMBER_REGEX_SECOND_GROUP_MATCHER".toRegex(),
                        PHONE_NUMBER_REGEX_SECOND_GROUP_REPLACEMENT_MATCHER
                    )
                }
                in PHONE_NUMBER_REGEX_THIRD_GROUP_RANGE_BOTTOM..PHONE_NUMBER_REGEX_THIRD_GROUP_RANGE_TOP -> {
                    resultado = resultado.replaceFirst(
                        (
                                "$PHONE_NUMBER_REGEX_FIRST_AND_SECOND_GROUP_MATCHER" + "$PHONE_NUMBER_REGEX_SECOND_GROUP_MATCHER" +
                                        "$PHONE_NUMBER_REGEX_THIRD_GROUP_MATCHER").toRegex(),
                        PHONE_NUMBER_REGEX_THIRD_GROUP_REPLACEMENT_MATCHER
                    )
                }
            }

            text.replace(FIRST_EDITTEXT_SELECTION_CHARACTER, text.length, resultado)

            mReceiver.addTextChangedListener(this)
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        s?.let {
            if (it.length == 3 && count == 1) {
                val result = "($s)"
                mReceiver.setText(result)
                mReceiver.setSelection(5)
            } else if (
                it.length == PHONE_NUMBER_FORMAT_SECOND_HYPHEN_INDEX
            ) {
                mReceiver.append(HYPHEN)
            }
        }
    }
}
