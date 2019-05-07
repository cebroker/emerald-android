package co.condorlabs.customcomponents.customdatepicker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.NumberPicker
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.helper.*
import java.util.*

class MonthYearPickerDialog: DialogFragment() {

    private var listener: DatePickerDialog.OnDateSetListener? = null
    var year: Int? = null
    var month: Int? = null

    fun setListener(listener: DatePickerDialog.OnDateSetListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val currentDate = Calendar.getInstance()
        val dialog = inflater?.inflate(R.layout.dialog_month_year_picker, null)
        val monthPicker = initializeMonthPicker(dialog, currentDate)
        val yearPicker = initializeYearPicker(dialog, currentDate)

        builder.setView(dialog)
            .setPositiveButton(
                R.string.date_picker_confirm
            ) { _, _ ->
                listener?.onDateSet(
                    null,
                    yearPicker?.value ?: currentDate.get(Calendar.MONTH),
                    monthPicker?.value ?: currentDate.get(Calendar.YEAR),
                    0
                )
            }
            .setNegativeButton(R.string.date_picker_cancel
            ) { _, _ -> this@MonthYearPickerDialog.dialog.cancel() }
        return builder.create()
    }

    private fun initializeMonthPicker(dialog: View?, currentDate: Calendar) =
        dialog?.findViewById<NumberPicker>(R.id.monthPicker)?.apply {
            minValue = MIN_MONTH
            maxValue = MAX_MONTH
            displayedValues = MONTHS
            value = month ?: currentDate.get(Calendar.MONTH)
        }


    private fun initializeYearPicker(dialog: View?, currentDate: Calendar) =
        dialog?.findViewById<NumberPicker>(R.id.yearPicker)?.apply {
            minValue = MIN_YEAR
            maxValue = MAX_YEAR
            value = year ?: currentDate.get(Calendar.YEAR)
        }
}
