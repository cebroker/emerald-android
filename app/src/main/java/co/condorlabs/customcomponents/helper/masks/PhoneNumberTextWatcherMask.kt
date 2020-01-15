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
import co.condorlabs.customcomponents.helper.TextWatcherAdapter

class PhoneNumberTextWatcherMask(
    private val receiver: EditText
) : TextWatcherAdapter() {

    override fun afterTextChanged(s: Editable?) {
        s?.let { text ->
            receiver.removeTextChangedListener(this)
            val hadParenthesis = text.toString().firstOrNull() == CHAR_OPENING_PARENTHESIS
            var firstGroupMask = false
            var result = text.toString()
                .replace(PHONE_NUMBER_SEPARATOR_TOKEN, "")
                .replace(OPENING_PARENTHESIS, "")
                .replace(CLOSING_PARENTHESIS, "")

            when (result.length) {
                in PHONE_NUMBER_REGEX_FIRST_GROUP_RANGE_BOTTOM..PHONE_NUMBER_REGEX_FIRST_GROUP_RANGE_TOP -> {
                    result = result.replaceFirst(
                        PHONE_NUMBER_REGEX_FIRST_AND_SECOND_GROUP_MATCHER.toRegex(),
                        PHONE_NUMBER_REGEX_FIRST_GROUP_REPLACEMENT_MATCHER
                    )
                    firstGroupMask = true
                }
                in PHONE_NUMBER_REGEX_SECOND_GROUP_RANGE_BOTTOM..PHONE_NUMBER_REGEX_SECOND_GROUP_RANGE_TOP -> {
                    result = result.replaceFirst(
                        "$PHONE_NUMBER_REGEX_FIRST_AND_SECOND_GROUP_MATCHER$PHONE_NUMBER_REGEX_SECOND_GROUP_MATCHER".toRegex(),
                        PHONE_NUMBER_REGEX_SECOND_GROUP_REPLACEMENT_MATCHER
                    )
                }
                in PHONE_NUMBER_REGEX_THIRD_GROUP_RANGE_BOTTOM..PHONE_NUMBER_REGEX_THIRD_GROUP_RANGE_TOP -> {
                    result = result.replaceFirst(
                        ("$PHONE_NUMBER_REGEX_FIRST_AND_SECOND_GROUP_MATCHER$PHONE_NUMBER_REGEX_SECOND_GROUP_MATCHER$PHONE_NUMBER_REGEX_THIRD_GROUP_MATCHER").toRegex(),
                        PHONE_NUMBER_REGEX_THIRD_GROUP_REPLACEMENT_MATCHER
                    )
                }
            }

            text.replace(FIRST_EDITTEXT_SELECTION_CHARACTER, text.length, result)

            receiver.addTextChangedListener(this)
            if (hadParenthesis and (firstGroupMask)) {
                try {
                    receiver.setSelection(AFTER_CLOSING_PARENTHESIS)
                } catch (t: Throwable) {
                    receiver.setSelection(result.length)
                }
            }
        }
    }
}
