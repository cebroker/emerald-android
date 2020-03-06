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
import android.text.TextWatcher
import co.condorlabs.customcomponents.CHAR_TO_DESCRIBE_A_NUMBER
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.ZERO

/**
 * @author Alexis Duque on 2020-01-22.
 * @company Condor Labs.
 * @email eduque@condorlabs.io.
 */
class TextWatcherMask(
    private val mask: String,
    val onCursorPositionChangeRequired: (Int) -> Unit
) : TextWatcher {
    private var isRunning = false
    private var isDeleting = false
    private var specialCharacters = EMPTY
    private val charToDescribeANumber = CHAR_TO_DESCRIBE_A_NUMBER
    private var maskPos = ZERO
    private var numPos = ZERO
    private var start = ZERO
    private var after = ZERO

    init {
        specialCharacters = mask.replace(charToDescribeANumber.toString(), EMPTY)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        if (!isRunning) {
            maskPos = ZERO
            numPos = ZERO
            this.start = start
            this.after = after
            isDeleting = count > after
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        if (isRunning) return

        val text = editable?.toString() ?: return
        val numbers = textWithoutSpecialCharacters(text, specialCharacters)
        val finalText = StringBuilder()
        var finalSelection = after

        if (numbers.isEmpty()) {
            isRunning = true
            editable.clear()
            isRunning = false
            return
        }

        while (maskPos < mask.length) {
            if (mask[maskPos] == charToDescribeANumber) {
                if (numPos < numbers.length) {
                    finalText.append(numbers[numPos])
                    numPos++
                    finalSelection++
                } else {
                    break
                }
            } else {
                finalText.append(mask[maskPos])
                finalSelection++
            }
            maskPos++
        }

        isRunning = true
        editable.clear()
        editable.append(finalText)
        isRunning = false

        if (isDeleting) {
            onCursorPositionChangeRequired(start)
        } else {
            onCursorPositionChangeRequired(finalSelection - after)
        }
    }

    private fun textWithoutSpecialCharacters(original: String, characters: String): String {
        var stringFinal = original

        characters.indices.forEach { i ->
            stringFinal = stringFinal.replace(characters[i].toString(), EMPTY)
        }

        return stringFinal
    }
}
