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
import co.condorlabs.customcomponents.helper.*
import java.util.*

class DateTextWatcherMask(private val mReceiver: EditText) : TextWatcherAdapter() {

    private var mCurrentDate = EMPTY

    private val mFormat = DATE_MASK_DATE_FORMAT_WITHOUT_SLASH

    private val mCalendar = Calendar.getInstance()

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        mReceiver.removeTextChangedListener(this)

        if (!s.isNullOrBlank() && s.toString() != mCurrentDate) {
            var digitsFromDateTyped = s.toString()
                    .replace(NO_DIGITS_REGEX.toRegex(), EMPTY)
            val digitsFromCurrentDateOnReceiver = mCurrentDate
                    .replace(NO_DIGITS_REGEX.toRegex(), EMPTY)

            val digitsFromDateTypedLength = digitsFromDateTyped.length
            var sel = digitsFromDateTypedLength
            var i = DATE_MASK_LOOP_STEP

            while (i <= digitsFromDateTypedLength && i < DATE_MASK_JUST_DIGITS_LENGTH) {
                sel++
                i += DATE_MASK_LOOP_STEP
            }

            if (digitsFromDateTyped == digitsFromCurrentDateOnReceiver) sel--

            if (digitsFromDateTyped.length < DATE_MASK_LENGTH) {
                digitsFromDateTyped += mFormat.substring(digitsFromDateTyped.length)
            } else {

                var day = Integer.parseInt(digitsFromDateTyped
                        .substring(DATE_MASK_DAY_INITIAL_INDEX, DATE_MASK_DAY_FINAL_INDEX))
                var mon = Integer.parseInt(digitsFromDateTyped
                        .substring(DATE_MASK_MONTH_INITIAL_INDEX, DATE_MASK_MONTH_FINAL_INDEX))
                var year = Integer.parseInt(digitsFromDateTyped
                        .substring(DATE_MASK_YEAR_INITIAL_INDEX, DATE_MASK_YEAR_FINAL_INDEX))

                mon = when {
                    mon < DATE_MASK_MIN_MONTH_INDEX -> DATE_MASK_MIN_MONTH_INDEX
                    mon > DATE_MASK_MAX_MONTH_INDEX -> DATE_MASK_MAX_MONTH_INDEX
                    else -> mon
                }

                year = when {
                    year < DATE_MASK_MIN_YEAR -> DATE_MASK_MIN_YEAR
                    year > DATE_MASK_MAX_YEAR -> DATE_MASK_MAX_YEAR
                    else -> year
                }

                mCalendar.set(Calendar.MONTH, mon - DATE_MASK_MONTH_INDEX_DEFAULT_AGGREGATOR_VALUE)
                mCalendar.set(Calendar.YEAR, year)

                day = when {
                    day > mCalendar.getActualMaximum(Calendar.DATE) -> mCalendar.getActualMaximum(Calendar.DATE)
                    else -> day
                }

                mCalendar.set(Calendar.DAY_OF_MONTH, day)

                digitsFromDateTyped = String.format(DATE_MASK_DIGITS_STRING_FORMAT, mon, day, year)
            }

            digitsFromDateTyped = "${digitsFromDateTyped.substring(
                DATE_MASK_MONTH_INITIAL_INDEX,
                    DATE_MASK_MONTH_FINAL_INDEX
            )}$SLASH" +
                    "${digitsFromDateTyped.substring(
                        DATE_MASK_DAY_INITIAL_INDEX,
                            DATE_MASK_DAY_FINAL_INDEX
                    )}$SLASH" +
                    digitsFromDateTyped.substring(
                        DATE_MASK_YEAR_INITIAL_INDEX,
                            DATE_MASK_YEAR_FINAL_INDEX
                    )


            sel = if (sel < DATE_MASK_SELECTION_MIN_INDEX) DATE_MASK_SELECTION_MIN_INDEX else sel
            mCurrentDate = digitsFromDateTyped
            mReceiver.setText(mCurrentDate)
            mReceiver.setSelection(
                    if (digitsFromDateTypedLength == ONE) ONE else if (sel < mCurrentDate.length) sel else mCurrentDate.length
            )
        }

        mReceiver.addTextChangedListener(this)
    }
}
