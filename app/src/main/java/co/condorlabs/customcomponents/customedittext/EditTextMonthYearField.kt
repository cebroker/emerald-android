package co.condorlabs.customcomponents.customedittext

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.customdatepicker.MonthYearPickerDialog
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.*
import co.condorlabs.customcomponents.helper.masks.MonthYearWatcherMark
import java.text.SimpleDateFormat
import java.util.*

class EditTextMonthYearField(
    private val currentContext: Context, attrs: AttributeSet
) : BaseEditTextFormField(currentContext, attrs), DatePickerDialog.OnDateSetListener {

    private var iconDrawable: Drawable? = null
    private var monthYearWatcherMark: MonthYearWatcherMark? = null
    private var simpleDateFormat: SimpleDateFormat? = null
    private var dateFormat = MONTH_YEAR_FORMAT
    var upperLimit: Calendar? = null

    init {
        currentContext.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EditTextDateField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        ).apply {
            try {
                iconDrawable = getDrawable(R.styleable.EditTextDateField_picker_icon)
            } finally {
                recycle()
            }
        }

        if (mRegex == null) {
            mRegex = MONTH_YEAR_REGEX
        }

        simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    }

    override fun setup() {
        super.setup()
        mEditText?.id = R.id.etMonthYear
        setupPicker()
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, VALIDATE_DATE_ERROR)
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        val receiver = mEditText?.let { it } ?: return
        val dateTextWatcherMask = monthYearWatcherMark?.let { it } ?: return

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        receiver.removeTextChangedListener(dateTextWatcherMask)
        receiver.setText(
            SimpleDateFormat(
                context.getString(R.string.show_month_year_format),
                Locale.US
            ).format(calendar.time)
        )

        mValueChangeListener?.onValueChange(getValue())

        receiver.addTextChangedListener(dateTextWatcherMask)
        receiver.setSelection(receiver.text.length)
    }

    private fun setupPicker() {

        val receiver = mEditText?.let { it } ?: return

        monthYearWatcherMark = MonthYearWatcherMark(receiver).apply {
            receiver.addTextChangedListener(this)
        }

        val drawable = iconDrawable?.let { it } ?: ContextCompat.getDrawable(context, R.drawable.ic_date)?.let { it }
        ?: throw RuntimeException(context.getString(R.string.dateformfield_no_icon_error_message))

        receiver.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            drawable,
            null
        )

        receiver.inputType = InputType.TYPE_CLASS_NUMBER
        receiver.maxEms = DATE_MASK_MAX_EMS

        val showDateFormat = context.getString(R.string.show_month_year_format)

        this.editText?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    if (wasCalendarDrawableClicked(v, event)) {
                        val calendar = Calendar.getInstance()

                        val dialogCanBeOpenOnEditTextText = if (receiver.text?.isEmpty() == false) {
                            val dateTyped = receiver.text?.replace(SLASH.toRegex(), EMPTY)
                            TextUtils.isDigitsOnly(dateTyped)
                        } else {
                            false
                        }

                        if (dialogCanBeOpenOnEditTextText) {
                            calendar.time =
                                SimpleDateFormat(showDateFormat, Locale.US).parse(receiver.text.toString())
                        }

                        MonthYearPickerDialog().apply {
                            year = calendar.get(Calendar.YEAR)
                            month = calendar.get(Calendar.MONTH)
                            setListener(this@EditTextMonthYearField)
                            upperLimit = this@EditTextMonthYearField.upperLimit
                            (currentContext as? AppCompatActivity)?.supportFragmentManager
                                ?.beginTransaction()
                                ?.add(this, MonthYearPickerDialog::class.java.name)
                                ?.commitAllowingStateLoss()
                        }
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

    fun getMonth(): Int {
        val month = editText?.text?.substring(
            MONTH_YEAR_MASK_MONTH_INITIAL_INDEX,
            MONTH_YEAR_MASK_MONTH_FINAL_INDEX
        )?.toInt() ?: ZERO
        return if (month == ZERO) {
            ZERO
        } else {
            month - 1
        }
    }

    fun getYear() =
        editText?.text?.substring(
            MONTH_YEAR_MASK_YEAR_INITIAL_INDEX + 1,
            MONTH_YEAR_MASK_YEAR_FINAL_INDEX + 1
        )?.toInt() ?: ZERO


    override fun isValid(): ValidationResult {
        val result = super.isValid()
        if (result.isValid) {
            upperLimit?.let {
                val upperLimitYear = it.get(Calendar.YEAR)
                val upperLimitMonth = it.get(Calendar.MONTH)
                val typedYear = getYear()
                val typedMonth = getMonth()
                if (typedYear > upperLimitYear
                    || (typedYear == upperLimitYear && typedMonth > upperLimitMonth)) {
                    return ValidationResult(
                        false,
                        String.format(
                            VALIDATE_UPPER_LIMIT_DATE_ERROR,
                            mHint,
                            String.format(MONTH_YEAR_STRING_TO_REPLACE, upperLimitMonth + 1, upperLimitYear)
                        )
                    )
                }
            }

        }
        return result
    }

}
