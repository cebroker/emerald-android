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
import android.widget.EditText
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.MONEY_MAX_AMOUNT
import co.condorlabs.customcomponents.helper.equalThan
import co.condorlabs.customcomponents.helper.lessThan
import java.math.BigDecimal
import java.text.NumberFormat

/**
 * @author Oscar Gallon on 12/20/18.
 */
class PriceTextWatcherMask(private val receiver: EditText) : TextWatcher {

    private var previousText: String = EMPTY
    private val maxAmount = MONEY_MAX_AMOUNT.toBigDecimal()
    private var current: String = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        previousText = s.toString()
    }

    override fun afterTextChanged(s: Editable?) {

        s?.let {
            if (s.toString() != current) {
                receiver.removeTextChangedListener(this)

                val cleanString: String = s.replace("""[$,.]""".toRegex(), "")

                if (cleanString.isNotEmpty()) {
                    val parsed = cleanString.toDouble()
                    val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))
                    current = formatted
                    if (!isAllowedAmount(cleanString.toBigDecimal())) {
                        receiver.setText(NumberFormat.getCurrencyInstance().format((maxAmount.toDouble() / 100)))
                        setSelectionAndListener()
                        return
                    } else {
                        receiver.setText(formatted)
                        receiver.setSelection(formatted.length)
                    }
                }

                receiver.addTextChangedListener(this)

            }
        }
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    private fun setSelectionAndListener() {
        receiver.setSelection(receiver.text.length)
        receiver.addTextChangedListener(this)
    }

    private fun isAllowedAmount(amount: BigDecimal): Boolean {
        return amount.lessThan(maxAmount) || amount.equalThan(maxAmount)
    }
}
