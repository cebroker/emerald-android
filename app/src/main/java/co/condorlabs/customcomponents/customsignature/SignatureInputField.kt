package co.condorlabs.customcomponents.customsignature

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.custombutton.CustomButton
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.fileselectorview.FileSelectorValue
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.ValidationResult

class SignatureInputField @JvmOverloads constructor(
    private val currentContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(currentContext, attrs, defStyleAttr),
    FormField<FileSelectorValue?>, OnSignatureDoneListener {

    override var isRequired: Boolean = true
    private var fileSelectorValue: FileSelectorValue? = null
    private var clContent: ConstraintLayout? = null
    private var ivIcon: AppCompatImageView? = null
    private var tvTapAction: AppCompatTextView? = null
    private var tvTitle: AppCompatTextView? = null
    private var tvError: AppCompatTextView? = null
    private var btnSign: CustomButton? = null
    private var valueChangeListener: ValueChangeListener<FileSelectorValue?>? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setup()
    }

    override fun setup() {
        val view = LayoutInflater.from(context).inflate(R.layout.signature_input_view, this, false)
        clContent = view.findViewById(R.id.clContent)
        ivIcon = view.findViewById(R.id.ivICon)
        tvTapAction = view.findViewById(R.id.tvTapAction)
        tvTapAction?.setOnClickListener { openSignatureDialog() }
        tvTitle = view.findViewById(R.id.tvTitle)
        tvError = view.findViewById(R.id.tvError)
        btnSign = view.findViewById(R.id.btnSign)
        btnSign?.setOnClickListener { openSignatureDialog() }

        addView(view)
    }

    private fun openSignatureDialog() {
        SignatureDialog().apply {
            setListener(this@SignatureInputField)
            (this@SignatureInputField.currentContext as? AppCompatActivity)?.supportFragmentManager
                ?.beginTransaction()
                ?.add(this, SignatureDialog::class.java.name)
                ?.commitAllowingStateLoss()
        }
    }

    override fun isValid(): ValidationResult {
        if (isRequired && fileSelectorValue == null) {
            return ValidationResult(false, context.getString(R.string.signature_empty_error))
        }
        return ValidationResult(true, EMPTY)
    }

    override fun showError(message: String) {
        tvError?.text = message
        tvError?.visibility = View.VISIBLE
    }

    override fun clearError() {
        tvError?.visibility = View.GONE
    }

    override fun getValue(): FileSelectorValue? {
        return fileSelectorValue
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, context.getString(R.string.signature_empty_error))
    }

    override fun setValueChangeListener(valueChangeListener: ValueChangeListener<FileSelectorValue?>) {
        this.valueChangeListener = valueChangeListener
    }

    override fun setIsRequired(required: Boolean) {
        isRequired = required
    }

    override fun onSignatureDone(bitmap: Bitmap) {
        fileSelectorValue = FileSelectorValue.BitmapValue(bitmap)
        ivIcon?.setImageDrawable(BitmapDrawable(resources, bitmap))
        clContent?.visibility = View.VISIBLE
        btnSign?.visibility = View.GONE
    }

}
