package co.condorlabs.customcomponents.customedittext

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.customdatepicker.MonthYearPickerDialog
import co.condorlabs.customcomponents.formfield.ValidationResult
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

        if (_regex == null) {
            _regex = MONTH_YEAR_REGEX
        }

        simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    }

    override fun setup() {
        super.setup()
        editText?.id = R.id.etMonthYear
        setupPicker()
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, VALIDATE_DATE_ERROR)
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        val receiver = editText?.let { it } ?: return
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

        _valueChangeListener?.onValueChange(getValue())

        receiver.addTextChangedListener(dateTextWatcherMask)
        receiver.setSelection(receiver.text.length)
    }

    private fun setupPicker() {

        val receiver = editText?.let { it } ?: return

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

                        val datePicker = MonthYearPickerDialog().apply {
                            year = calendar.get(Calendar.YEAR)
                            month = calendar.get(Calendar.MONTH)
                        }

                        datePicker.setListener(this)

                        val manager =
                            (currentContext as? AppCompatActivity)?.supportFragmentManager ?: return@setOnTouchListener true
                        datePicker.show(
                            manager,
                            MONTH_YEAR_PICKER_DIALOG_TAG
                        )

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
