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

package co.condorlabs.customcomponents.customedittext

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.masks.DateTextWatcherMask
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class EditTextDateField(context: Context, attrs: AttributeSet) : BaseEditTextFormField(context, attrs),
    DatePickerDialog.OnDateSetListener {

    private var mIconDrawable: Drawable? = null
    private var mDateTextWatcherMask: DateTextWatcherMask? = null
    private var mLowerLimit: Long? = null
    private var mUpperLimit: Long? = null
    private var mSimpleDateFormat: SimpleDateFormat? = null
    private var mDateFormat = DEFAULT_DATE_FORMAT

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EditTextDateField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        ).apply {
            try {
                mIconDrawable = getDrawable(R.styleable.EditTextDateField_picker_icon)
            } finally {
                recycle()
            }
        }

        if (regexListToMatch.isEmpty()) {
            regexListToMatch.add(DATE_REGEX)
        }

        mSimpleDateFormat = SimpleDateFormat(mDateFormat, Locale.getDefault())
    }

    override fun setup() {
        super.setup()
        editText?.id = View.generateViewId()
        setupPicker()
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, VALIDATE_DATE_ERROR)
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        val receiver = editText?.let { it } ?: return
        val dateTextWatcherMask = mDateTextWatcherMask?.let { it } ?: return

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        receiver.removeTextChangedListener(dateTextWatcherMask)
        receiver.setText(
            SimpleDateFormat(
                context.getString(R.string.show_date_format),
                Locale.US
            ).format(calendar.time)
        )

        _valueChangeListener?.onValueChange(getValue())

        receiver.addTextChangedListener(dateTextWatcherMask)
        receiver.setSelection(receiver.text.length)
    }

    fun setLowerLimit(limit: Long) {
        mLowerLimit = limit
    }

    fun setLowerLimit(limit: String, format: String = context.getString(R.string.show_date_format)) {
        try {
            mLowerLimit = parseDate(format, limit)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setUpperLimit(limit: Long) {
        mUpperLimit = limit
    }

    fun setUpperLimit(limit: String, format: String = context.getString(R.string.show_date_format)) {
        try {
            mUpperLimit = parseDate(format, limit)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun isValid(): ValidationResult {
        val result = super.isValid()

        if (result.isValid && (isRequired || getValue().isNotEmpty())) {
            val actualDate = mSimpleDateFormat?.parse(getValue()) ?: return result

            if (isDateBeforeOtherOneInMilliseconds(actualDate, mLowerLimit)) {
                return ValidationResult(
                    false,
                    String.format(
                        VALIDATE_LOWER_LIMIT_DATE_ERROR,
                        hint,
                        getFormatedDateFromMilliseconds(mLowerLimit ?: ZERO.toLong())
                    )
                )
            }

            if (isDateAfterOtherOneInMilliseconds(actualDate, mUpperLimit)) {
                return ValidationResult(
                    false,
                    String.format(
                        VALIDATE_UPPER_LIMIT_DATE_ERROR,
                        hint,
                        getFormatedDateFromMilliseconds(mUpperLimit ?: ZERO.toLong())
                    )
                )
            }
        }

        return result
    }

    fun getLowerLimit(): Long? = mLowerLimit

    fun getUpperLimit(): Long? = mUpperLimit

    private fun isDateBeforeOtherOneInMilliseconds(actual: Date, otherOne: Long?): Boolean {
        val lowerLimit = otherOne?.let { it } ?: return false
        return actual.time < lowerLimit
    }

    private fun isDateAfterOtherOneInMilliseconds(actual: Date, otherOne: Long?): Boolean {
        val upperLimit = otherOne?.let { it } ?: return false
        return actual.time > upperLimit
    }

    private fun getFormatedDateFromMilliseconds(milliseconds: Long): String {
        return try {
            mSimpleDateFormat?.format(Date().apply {
                time = milliseconds
            }) ?: EMPTY
        } catch (e: Exception) {
            e.printStackTrace()
            EMPTY
        }
    }

    @Throws(ParseException::class)
    private fun parseDate(format: String, candidate: String): Long {
        return SimpleDateFormat(format, Locale.getDefault()).parse(candidate).time
    }

    private fun setupPicker() {

        val receiver = editText?.let { it } ?: return

        mDateTextWatcherMask = DateTextWatcherMask(receiver).apply {
            receiver.addTextChangedListener(this)
        }

        val drawable = mIconDrawable?.let { it } ?: ContextCompat.getDrawable(context, R.drawable.ic_date)?.let { it }
        ?: throw RuntimeException(context.getString(R.string.dateformfield_no_icon_error_message))

        receiver.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            drawable,
            null
        )

        receiver.inputType = InputType.TYPE_CLASS_NUMBER
        receiver.maxEms = DATE_MASK_MAX_EMS

        val showDateFormat = context.getString(R.string.show_date_format)

        this.editText?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    if (wasCalendarDrawableClicked(v, event)) {
                        val calendar = Calendar.getInstance()

                        val dialogCanBeOpenOnEditTextText = if (receiver.text?.isEmpty() == false) {
                            val dateTyped = receiver.text?.replace(
                                SLASH.toRegex(),
                                EMPTY
                            )
                            TextUtils.isDigitsOnly(dateTyped)
                        } else {
                            false
                        }

                        if (dialogCanBeOpenOnEditTextText) {
                            calendar.time =
                                SimpleDateFormat(showDateFormat, Locale.US).parse(receiver.text.toString())
                        }

                        val datePicker = DatePickerDialog(
                            context, R.style.DatePickerTheme, this, calendar
                                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )

                        mLowerLimit?.let {
                            datePicker.datePicker.minDate = it
                        }

                        mUpperLimit?.let {
                            datePicker.datePicker.maxDate = it
                        }

                        datePicker.show()

                        true
                    } else {
                        false
                    }
                }
                else -> false
            }
        }
    }

    private fun wasCalendarDrawableClicked(touchedView: View, event: MotionEvent): Boolean {
        val pos = IntArray(COMPOUND_DRAWABLE_POSITION_ARRAY_SIZE)

        touchedView.getLocationOnScreen(pos)

        val editTextRightPosition = pos[DATE_EDIT_TEXT_RIGHT_COMPOUND_DRAWABLE_POSITION] +
                touchedView.width

        val rightDrawable =
            (touchedView as? TextView)?.compoundDrawables?.get(DRAWABLE_RIGHT_POSITION)?.let { it } ?: return false

        val drawableWidth = rightDrawable.bounds.width()

        val eventRawX = event.rawX

        return eventRawX >= editTextRightPosition - drawableWidth - COMPOUND_DRAWABLE_TOUCH_OFF_SET
    }
}
