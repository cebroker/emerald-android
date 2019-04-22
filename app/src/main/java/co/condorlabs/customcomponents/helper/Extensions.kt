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

package co.condorlabs.customcomponents.helper

import android.text.TextUtils
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun String?.isNumeric(): Boolean {
    this?.let {
        return TextUtils.isDigitsOnly(this)
    }

    return false
}

fun BigDecimal.toDollarAmount(): String {

    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    val df = formatter as DecimalFormat
    df.applyPattern(MONEY_FORMAT)

    return df.format(this)
}

fun BigDecimal.lessThan(value: BigDecimal): Boolean {
    return compareTo(value) == -1
}

fun BigDecimal.equalThan(value: BigDecimal): Boolean {
    return compareTo(value) == 0
}
