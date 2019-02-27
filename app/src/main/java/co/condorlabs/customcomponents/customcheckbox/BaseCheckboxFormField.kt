package co.condorlabs.customcomponents.customcheckbox

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.EMPTY

abstract class BaseCheckboxFormField(context: Context, private val mAttrs: AttributeSet) : TextInputLayout(context, mAttrs),
    FormField {


    private var mLabelText = EMPTY
    private val mTVLabel = TextView(context, mAttrs).apply {
        id = R.id.tvLabel
    }

    private val mLayoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setup()
    }

    override fun isValid(): ValidationResult {
        return ValidationResult(true, EMPTY)
    }

    override fun showError(message: String) {
        this.isErrorEnabled = true
        this.error = message
    }

    override fun clearError() {
        this.isErrorEnabled = false
        this.error = EMPTY
    }

    override fun setup() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}