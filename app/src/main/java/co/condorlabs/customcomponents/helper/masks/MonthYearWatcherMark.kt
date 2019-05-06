package co.condorlabs.customcomponents.helper.masks

import android.widget.EditText
import co.condorlabs.customcomponents.helper.*
import java.util.*

class MonthYearWatcherMark(private val mReceiver: EditText) : TextWatcherAdapter() {

    private var currentDate = EMPTY

    private val mFormat = MONTH_YEAR_FORMAT_WITHOUT_SLASH

    private val mCalendar = Calendar.getInstance()

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        mReceiver.removeTextChangedListener(this)

        if (!s.isNullOrBlank() && s.toString() != currentDate) {
            var digitsFromDateTyped = s.toString()
                .replace(NO_DIGITS_REGEX.toRegex(), EMPTY)
            val digitsFromCurrentDateOnReceiver = currentDate
                .replace(NO_DIGITS_REGEX.toRegex(), EMPTY)

            val digitsFromDateTypedLength = digitsFromDateTyped.length
            var sel = digitsFromDateTypedLength
            var i = DATE_MASK_LOOP_STEP

            while (i <= digitsFromDateTypedLength && i < MONTH_YEAR_MASK_JUST_DIGITS_LENGTH) {
                sel++
                i += DATE_MASK_LOOP_STEP
            }

            if (digitsFromDateTyped == digitsFromCurrentDateOnReceiver) sel--

            if (digitsFromDateTyped.length < MONTH_YEAR_MASK_LENGTH) {
                digitsFromDateTyped += mFormat.substring(digitsFromDateTyped.length)
            } else {

                var mon = Integer.parseInt(digitsFromDateTyped
                    .substring(MONTH_YEAR_MASK_MONTH_INITIAL_INDEX, MONTH_YEAR_MASK_MONTH_FINAL_INDEX))
                var year = Integer.parseInt(digitsFromDateTyped
                    .substring(MONTH_YEAR_MASK_YEAR_INITIAL_INDEX, MONTH_YEAR_MASK_YEAR_FINAL_INDEX))

                mon = when {
                    mon < DATE_MASK_MIN_MONTH_INDEX -> DATE_MASK_MIN_MONTH_INDEX
                    mon > DATE_MASK_MAX_MONTH_INDEX -> DATE_MASK_MAX_MONTH_INDEX
                    else -> mon
                }

                year = when {
                    year < MIN_YEAR -> MIN_YEAR
                    year > MAX_YEAR -> MAX_YEAR
                    else -> year
                }

                mCalendar.set(Calendar.MONTH, mon - DATE_MASK_MONTH_INDEX_DEFAULT_AGGREGATOR_VALUE)
                mCalendar.set(Calendar.YEAR, year)

                mCalendar.set(Calendar.DAY_OF_MONTH, 1)

                digitsFromDateTyped = String.format(MONTH_YEAR_MASK_DIGITS_STRING_FORMAT, mon, year)
            }

            digitsFromDateTyped = "${digitsFromDateTyped.substring(
                MONTH_YEAR_MASK_MONTH_INITIAL_INDEX,
                MONTH_YEAR_MASK_MONTH_FINAL_INDEX
            )}$SLASH" +
                    digitsFromDateTyped.substring(
                        MONTH_YEAR_MASK_YEAR_INITIAL_INDEX,
                        MONTH_YEAR_MASK_YEAR_FINAL_INDEX
                    )

            sel = if (sel < DATE_MASK_SELECTION_MIN_INDEX) DATE_MASK_SELECTION_MIN_INDEX else sel
            currentDate = digitsFromDateTyped
            mReceiver.setText(currentDate)
            mReceiver.setSelection(
                if (digitsFromDateTypedLength == ONE) ONE else if (sel < currentDate.length) sel else currentDate.length
            )
        }

        mReceiver.addTextChangedListener(this)
    }
}
