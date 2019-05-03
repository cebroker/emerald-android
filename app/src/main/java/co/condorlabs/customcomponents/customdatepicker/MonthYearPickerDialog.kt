package co.condorlabs.customcomponents.customdatepicker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
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
        val cal = Calendar.getInstance()
        val dialog = inflater?.inflate(R.layout.dialog_month_year_picker, null)
        val monthPicker = dialog?.findViewById(R.id.monthPicker) as NumberPicker
        val yearPicker = dialog?.findViewById(R.id.yearPicker) as NumberPicker

        monthPicker.minValue = MIN_MONTH
        monthPicker.maxValue = MAX_MONTH
        monthPicker.displayedValues = MONTHS
        month.let {
            if(it != null) {
                monthPicker.value = it
            } else {
                monthPicker.value = cal.get(Calendar.MONTH)
            }
        }

        yearPicker.minValue = MIN_YEAR
        yearPicker.maxValue = MAX_YEAR
        year.let {
            if(it != null){
                yearPicker.value = it
            } else{
                yearPicker.value = cal.get(Calendar.YEAR)
            }
        }

        builder.setView(dialog)
            .setPositiveButton(
                R.string.date_picker_confirm
            ) { _, _ ->
                listener?.onDateSet(
                    null,
                    yearPicker.value,
                    monthPicker.value,
                    0
                )
            }
            .setNegativeButton(R.string.date_picker_cancel
            ) { _, _ -> this@MonthYearPickerDialog.dialog.cancel() }
        return builder.create()
    }
}