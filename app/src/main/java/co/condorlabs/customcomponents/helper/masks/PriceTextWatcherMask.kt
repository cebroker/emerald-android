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
import co.condorlabs.customcomponents.COMMA_AS_DECIMAL
import co.condorlabs.customcomponents.DOLLAR_SYMBOL
import co.condorlabs.customcomponents.DOT_CHARACTER
import co.condorlabs.customcomponents.DOT_STRING
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.MONEY_MAX_AMOUNT
import co.condorlabs.customcomponents.MONEY_TWO_DECIMALS
import co.condorlabs.customcomponents.NON_NUMERICAL_SYMBOLS
import co.condorlabs.customcomponents.THREE_DIGITS
import co.condorlabs.customcomponents.ZERO_CHARACTER
import co.condorlabs.customcomponents.helper.equalThan
import co.condorlabs.customcomponents.helper.lessThan
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * @author Oscar Gallon on 12/20/18.
 */
class PriceTextWatcherMask(private val receiver: EditText) : TextWatcher {

    private val formatter = NumberFormat.getNumberInstance(Locale.US)
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

                if (cleanString.isNotEmpty()){
                    val parsed = cleanString.toDouble()
                    val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))
                    current = formatted
                    receiver.setText(formatted)
                    receiver.setSelection(formatted.length)
                }

                receiver.addTextChangedListener(this)

            }
        }

//        val text = s?.toString() ?: return
//
//        receiver.removeTextChangedListener(this)
//
//
//        try {
//            val formatter = NumberFormat.getCurrencyInstance(Locale.US)
//            formatter.maximumFractionDigits = 2
//            formatter.roundingMode = RoundingMode.FLOOR
//            formatter.currency = Currency.getInstance(Locale.US)
//            val amount = NumberFormat.getNumberInstance(Locale.US).parse(
//                text.replace(
//                    """[$,.]""".toRegex(),
//                    EMPTY
//                )
//            )
//
//            val cleanString: String = s.replace("""[$,.]""".toRegex(), "")
//            val parsed = cleanString.toDouble()
//
//            if (isDeletingDecimalPart(text)) {
//                receiver.setText(text)
//                setSelectionAndListener()
//                return
//            }
//
//
//            val currentlyAmount = formatter.format(amount!!.toDouble() /100)
//
//            if (!isAllowedAmount(getPriceFromCurrency(currentlyAmount))) {
//                setSelectionAndListener()
//                receiver.setText(MONEY_MAX_AMOUNT.toString())
//                return
//            }
//
//            receiver.setText(currentlyAmount)
//
//            if (previousText.length > 3) {
//                when {
//                    previousText[previousText.length - 2] == ZERO_CHARACTER && currentlyAmount[currentlyAmount.length - 2] != ZERO_CHARACTER -> {
//                        receiver.setSelection(receiver.text.length - 1)
//                        receiver.addTextChangedListener(this)
//                    }
//                    previousText[previousText.length - 1] == ZERO_CHARACTER && currentlyAmount[currentlyAmount.length - 1] != ZERO_CHARACTER -> {
//                        receiver.setSelection(receiver.text.length)
//                        receiver.addTextChangedListener(this)
//                    }
//                    else -> {
//                        receiver.setSelection(receiver.text.length - 3)
//                        receiver.addTextChangedListener(this)
//                    }
//                }
//            } else {
//                receiver.setSelection(receiver.text.length - 3)
//                receiver.addTextChangedListener(this)
//            }
//
//        } catch (exception: Throwable) {
//            receiver.setText(previousText)
//            setSelectionAndListener()
//        }
//        setSelectionAndListener()
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    private fun getPriceFromCurrency(text: String): BigDecimal {
        val decimalFormat = DecimalFormat(MONEY_TWO_DECIMALS)
        decimalFormat.roundingMode = RoundingMode.FLOOR
        val amount = formatter.parse(
            text.replace(
                NON_NUMERICAL_SYMBOLS.toRegex(),
                EMPTY
            )
        )

        return decimalFormat.format(amount).replace(COMMA_AS_DECIMAL, ".").toBigDecimal()
    }

    private fun setSelectionAndListener() {
        receiver.setSelection(receiver.text.length)
        receiver.addTextChangedListener(this)
    }

    private fun isAllowedAmount(amount: BigDecimal): Boolean {
        return amount.lessThan(maxAmount) || amount.equalThan(maxAmount)
    }

//    override fun afterTextChanged(s: Editable?) {
//        val text = s?.toString() ?: return
//
//        receiver.removeTextChangedListener(this)
//
//        if (text.isEmpty()) {
//            receiver.setText(DOLLAR_SYMBOL)
//            setSelectionAndListener()
//            return
//        }
//
//        if (text == DOLLAR_SYMBOL) {
//            setSelectionAndListener()
//            return
//        }
//
//        if (text == "$DOLLAR_SYMBOL$DOT_STRING") {
//            receiver.setText(previousText)
//            setSelectionAndListener()
//            return
//        }
//
//        try {
//            val currentlyAmount = getPriceFromCurrency(text)
//
//            if (previousText == EMPTY) {
//                if (isAllowedAmount(currentlyAmount)) {
//                    receiver.setText(currentlyAmount.toDollarAmount())
//                    setSelectionAndListener()
//                    return
//                }
//                receiver.setText(maxAmount.toDollarAmount())
//                setSelectionAndListener()
//                return
//            }
//
//            if (!isAllowedAmount(currentlyAmount)) {
//                receiver.setText(previousText)
//                setSelectionAndListener()
//                return
//            }
//
//            if (text.last() == DOT_CHARACTER) {
//                if (isMaxAmount(currentlyAmount)) {
//                    receiver.setText(maxAmount.toDollarAmount())
//                    setSelectionAndListener()
//                    return
//                }
//                receiver.setText(text)
//                setSelectionAndListener()
//                return
//            }
//
//            if (isTypingThirdDecimalDigit(text)) {
//                receiver.setText(previousText)
//                setSelectionAndListener()
//                return
//            }
//
//            if (isDeletingDecimalPart(text)) {
//                receiver.setText(text)
//                setSelectionAndListener()
//                return
//            }
//
//            if (text[text.lastIndex - 1] == DOT_CHARACTER && !isZeroLastDecimal(text)) {
//                receiver.setText(currentlyAmount.toDollarAmount().plus("0"))
//                setSelectionAndListener()
//                return
//            }
//
//            if (text.last() == ZERO_CHARACTER && text[text.lastIndex - 1] == '$'){
//                receiver.setText(text)
//                setSelectionAndListener()
//                return
//            }
//
//            if (text.last() == ZERO_CHARACTER && text != ZERO_CHARACTER.toString()) {
//                when {
//                    text[text.lastIndex - 1] == DOT_CHARACTER && isZeroLastDecimal(text) -> receiver.setText(
//                        currentlyAmount.toDollarAmount().plus(".0")
//                    )
//                    isZeroLastDecimal(text) -> receiver.setText(currentlyAmount.toDollarAmount().plus(".00"))
//                    text[text.lastIndex - 2] == DOT_CHARACTER && text[text.lastIndex - 1] == ZERO_CHARACTER -> receiver.setText(previousText)
//                    else -> {
//                        receiver.setText(currentlyAmount.toDollarAmount())
//                    }
//                }
//                setSelectionAndListener()
//                return
//            }
//            receiver.setText(currentlyAmount.toDollarAmount())
//        } catch (exception: Throwable) {
//            receiver.setText(previousText)
//            setSelectionAndListener()
//        }
//
//        setSelectionAndListener()
//    }


    private fun isMaxAmount(amount: BigDecimal): Boolean {
        return amount.equalThan(maxAmount)
    }

    private fun isTypingThirdDecimalDigit(newText: String): Boolean {
        return newText.substringAfter(DOT_STRING).matches(THREE_DIGITS.toRegex())
    }

    private fun isDeletingDecimalPart(newText: String): Boolean {
        if (!newText.contains(DOT_STRING)) {
            return false
        }
        return if (newText.substringAfter(DOT_STRING).isNotEmpty()) {
            newText.substringAfter(DOT_STRING).toInt() < previousText.substringAfter(DOT_STRING).toInt()
        } else {
            false
        }
    }

    private fun isZeroLastDecimal(newText: String): Boolean {
        return if (newText.contains(DOT_CHARACTER)) {
            newText.last() == ZERO_CHARACTER
        } else {
            false
        }
    }
}
