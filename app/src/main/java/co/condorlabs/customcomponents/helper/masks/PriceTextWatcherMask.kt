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
import co.condorlabs.customcomponents.helper.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * @author Oscar Gallon on 12/20/18.
 */
class PriceTextWatcherMask(private val receiver: EditText) : TextWatcherAdapter() {

    private val formatter = NumberFormat.getNumberInstance(Locale.US)
    private var previousText: String = EMPTY
    private val maxAmount = MONEY_MAX_AMOUNT.toBigDecimal()

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        super.beforeTextChanged(s, start, count, after)
        previousText = s.toString()
    }

    override fun afterTextChanged(s: Editable?) {
        val text = s?.toString() ?: return

        receiver.removeTextChangedListener(this)

        if (text.isEmpty()) {
            receiver.setText(DOLLAR_SYMBOL)
            setSelectionAndListener()
            return
        }

        if (text == DOLLAR_SYMBOL) {
            setSelectionAndListener()
            return
        }

        try {
            val currentlyAmount = getPriceFromCurrency(text)

            if (!isAllowedAmount(currentlyAmount)) {
                receiver.setText(previousText)
                setSelectionAndListener()
                return
            }

            if (text.last() == '.') {
                if (isMaxAmount(currentlyAmount)) {
                    receiver.setText(maxAmount.toDollarAmount())
                    setSelectionAndListener()
                    return
                }
                receiver.setText(text)
                setSelectionAndListener()
                return
            }

            if (isTypingThirdDecimalDigit(text)) {
                receiver.setText(previousText)
                setSelectionAndListener()
                return
            }

            if (isDeletingDecimalPart(text)) {
                receiver.setText(text)
                setSelectionAndListener()
                return
            }

            if (text.last() == '0') {
                when {
                    previousText.last() == '.' -> receiver.setText(text)
                    isZeroLastDecimal(text) -> receiver.setText(text)
                    else -> {
                        receiver.setText(currentlyAmount.toDollarAmount())
                    }
                }
                setSelectionAndListener()
                return
            }
            receiver.setText(currentlyAmount.toDollarAmount())
        } catch (exception: Throwable) {
            receiver.setText(previousText)
            setSelectionAndListener()
        }

        setSelectionAndListener()
    }

    private fun getPriceFromCurrency(text: String): BigDecimal {
        val decimalFormat = DecimalFormat(MONEY_TWO_DECIMALS)
        decimalFormat.roundingMode = RoundingMode.FLOOR
        val amount = formatter.parse(text.replace(NON_NUMERICAL_SYMBOLS.toRegex(), EMPTY))

        return decimalFormat.format(amount).replace(COMMA_AS_DECIMAL, ".").toBigDecimal()
    }

    private fun setSelectionAndListener() {
        receiver.setSelection(receiver.text.length)
        receiver.addTextChangedListener(this)
    }

    private fun isAllowedAmount(amount: BigDecimal): Boolean {
        return amount.lessThan(maxAmount) || amount.equalThan(maxAmount)
    }

    private fun isMaxAmount(amount: BigDecimal): Boolean {
        return amount.equalThan(maxAmount)
    }

    private fun isTypingThirdDecimalDigit(newText: String): Boolean {
        return newText.substringAfter(".").matches(THREE_DIGITS.toRegex())
    }

    private fun isDeletingDecimalPart(newText: String): Boolean {
        if (!newText.contains(".")) {
            return false
        }
        return newText.substringAfter(".") < previousText.substringAfter(".")
    }

    private fun isZeroLastDecimal(newText: String): Boolean {
        return newText.substringAfter(".").matches(ZERO_AFTER_DIGIT.toRegex())
    }
}
