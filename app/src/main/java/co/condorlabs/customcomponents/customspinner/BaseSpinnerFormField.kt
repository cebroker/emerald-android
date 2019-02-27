package co.condorlabs.customcomponents.customspinner

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.DEFAULT_STYLE_ATTR
import co.condorlabs.customcomponents.helper.DEFAULT_STYLE_RES
import co.condorlabs.customcomponents.helper.EMPTY
import co.condorlabs.customcomponents.helper.VALIDATE_SPINNER_NO_SELECTION_ERROR

/**
 * @author Oscar Gallon on 2/26/19.
 */
abstract class BaseSpinnerFormField(context: Context, private val mAttrs: AttributeSet) :
    TextInputLayout(context, mAttrs),
    FormField {

    protected var mSpinner: Spinner? = null

    protected val mLayoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    protected val mTVLabel = TextView(context, mAttrs)?.apply {
        id = R.id.tvLabel
    }

    private var mLabelText = EMPTY

    init {
        val typedArray = context.obtainStyledAttributes(
            mAttrs,
            R.styleable.StateSpinnerFormField,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )

        mLabelText = typedArray.getString(R.styleable.StateSpinnerFormField_label)
            ?: context.getString(R.string.default_base_hint)

        typedArray.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setup()
    }

    override fun setup() {
        mTVLabel.text = mLabelText
        addView(mTVLabel, mLayoutParams)
        mSpinner = Spinner(context, mAttrs)
        addView(mSpinner)
    }

    override fun isValid(): ValidationResult {
        mSpinner?.let {
            if (it.selectedItemPosition == AdapterView.INVALID_POSITION) {
                return ValidationResult(false, String.format(VALIDATE_SPINNER_NO_SELECTION_ERROR, mLabelText))
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
}
