package co.condorlabs.customcomponents.helper.masks

import android.widget.EditText
import co.condorlabs.customcomponents.helper.*
import java.util.*

class MonthYearWatcherMark(private val mReceiver: EditText) : TextWatcherAdapter() {

    private var currentDate = EMPTY

    private val format = MONTH_YEAR_FORMAT_WITHOUT_SLASH

    private val calendar = Calendar.getInstance()

    override fun onTextChanged(newText: CharSequence?, start: Int, before: Int, count: Int) {
        mReceiver.removeTextChangedListener(this)

        if (!newText.isNullOrBlank() && newText.toString() != currentDate) {
            var digitsFromDateTyped = newText.toString()
                .replace(NO_DIGITS_REGEX.toRegex(), EMPTY)
            val digitsFromCurrentDateOnReceiver = currentDate
                .replace(NO_DIGITS_REGEX.toRegex(), EMPTY)

            val digitsFromDateTypedLength = digitsFromDateTyped.length
            var selected = digitsFromDateTypedLength
            var loopStepIndex = DATE_MASK_LOOP_STEP

            while (loopStepIndex <= digitsFromDateTypedLength && loopStepIndex < MONTH_YEAR_MASK_JUST_DIGITS_LENGTH) {
                selected++
                loopStepIndex += DATE_MASK_LOOP_STEP
            }

            if (digitsFromDateTyped == digitsFromCurrentDateOnReceiver) selected--

            if (digitsFromDateTyped.length < MONTH_YEAR_MASK_LENGTH) {
                digitsFromDateTyped += format.substring(digitsFromDateTyped.length)
            } else {

                var month = Integer.parseInt(digitsFromDateTyped
                    .substring(MONTH_YEAR_MASK_MONTH_INITIAL_INDEX, MONTH_YEAR_MASK_MONTH_FINAL_INDEX))
                var year = Integer.parseInt(digitsFromDateTyped
                    .substring(MONTH_YEAR_MASK_YEAR_INITIAL_INDEX, MONTH_YEAR_MASK_YEAR_FINAL_INDEX))

                month = when {
                    month < DATE_MASK_MIN_MONTH_INDEX -> DATE_MASK_MIN_MONTH_INDEX
                    month > DATE_MASK_MAX_MONTH_INDEX -> DATE_MASK_MAX_MONTH_INDEX
                    else -> month
                }

                year = when {
                    year < MIN_YEAR -> MIN_YEAR
                    year > MAX_YEAR -> MAX_YEAR
                    else -> year
                }

                calendar.set(Calendar.MONTH, month - DATE_MASK_MONTH_INDEX_DEFAULT_AGGREGATOR_VALUE)
                calendar.set(Calendar.YEAR, year)

                calendar.set(Calendar.DAY_OF_MONTH, FIRST_DAY_OF_MONTH)

                digitsFromDateTyped = String.format(MONTH_YEAR_MASK_DIGITS_STRING_FORMAT, month, year)
            }

            digitsFromDateTyped = "${digitsFromDateTyped.substring(
                MONTH_YEAR_MASK_MONTH_INITIAL_INDEX,
                MONTH_YEAR_MASK_MONTH_FINAL_INDEX
            )}$SLASH" +
                    digitsFromDateTyped.substring(
                        MONTH_YEAR_MASK_YEAR_INITIAL_INDEX,
                        MONTH_YEAR_MASK_YEAR_FINAL_INDEX
                    )

            selected = if (selected < DATE_MASK_SELECTION_MIN_INDEX) DATE_MASK_SELECTION_MIN_INDEX else selected
            currentDate = digitsFromDateTyped
            mReceiver.setText(currentDate)
            mReceiver.setSelection(
                if (digitsFromDateTypedLength == ONE) ONE else if (selected < currentDate.length) selected else currentDate.length
            )
        }

        mReceiver.addTextChangedListener(this)
    }
}
