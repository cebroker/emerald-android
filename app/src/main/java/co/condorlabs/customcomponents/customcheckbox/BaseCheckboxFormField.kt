package co.condorlabs.customcomponents.customcheckbox

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.*

abstract class BaseCheckboxFormField(context: Context, private val mAttrs: AttributeSet) : TextInputLayout(context, mAttrs),
        FormField {

    private var mCountOptions = 0
    protected var textOptions: Array<CharSequence>? = null
    private var mRadioGroup: CheckBox? = null
    protected var labelText: String? = EMPTY
    private val mTVLabel = TextView(context, mAttrs).apply {
        id = R.id.tvLabel
    }

    private val mLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    )


    init {
        val typedArray = context.obtainStyledAttributes(
                mAttrs,
                R.styleable.BaseCheckboxFormField,
                DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )
        mCountOptions = typedArray.getInt(R.styleable.BaseCheckboxFormField_count_options, 0)
        textOptions = typedArray.getTextArray(R.styleable.BaseCheckboxFormField_values)
        labelText = typedArray.getString(R.styleable.BaseCheckboxFormField_title)

        typedArray.recycle()
    }

    private fun countIfIsChecked(): Int {
        var count = 0
        (0 until childCount).forEach { i ->
            (getChildAt(i) as? CheckBox)?.let {
                if (!it.isChecked) {
                    count++
                }
            }
        }
        return count
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setup()
    }

    override fun isValid(): ValidationResult {
        when {
            mIsRequired -> {
                if (mCountOptions == countIfIsChecked()) {
                    return ValidationResult(false, String.format(MESSAGE_FORMAT_ERROR, labelText))
                }
            }
        }
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
        labelText?.let {
            mTVLabel.text = it
            addView(mTVLabel, mLayoutParams)
        }
        for (i in 0 until mCountOptions) {
            addView(CheckBox(context).apply {
                id = i
                text = textOptions?.get(i) ?: i.toString()
            }, mLayoutParams)
        }
    }
}