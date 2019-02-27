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

import android.widget.EditText
import co.condorlabs.customcomponents.helper.TextWatcherAdapter
import co.condorlabs.customcomponents.helper.isNumeric
import co.condorlabs.customcomponents.helper.toDollarAmount
import java.text.NumberFormat
import java.util.*

/**
 * @author Oscar Gallon on 12/20/18.
 */
class PriceTextWatcherMask(private val mReceiver: EditText) : TextWatcherAdapter() {

    private val mFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
    ) {
        s?.toString()
                ?.let { text ->
                    mReceiver.removeTextChangedListener(this)
                    val currentyValue = getPriceFromCurrency(text).toDollarAmount()
                    mReceiver.setText(currentyValue)
                    mReceiver.setSelection(currentyValue.length)
                    mReceiver.addTextChangedListener(this)
                }

    }

    private fun getPriceFromCurrency(text: String): String {
        if (text.isNumeric()) {
            return text
        }

        return try {
            mFormatter.parse(text)
                    .toString()
        } catch (exception: Exception) {
            text
        }
    }
}
