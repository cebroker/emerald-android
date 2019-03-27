package co.condorlabs.customcomponents.fileselectorview

import android.app.AlertDialog
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.EMPTY
import co.condorlabs.customcomponents.helper.FILE_SELECTOR_GALLERY_OPTION_INDEX
import co.condorlabs.customcomponents.helper.NOT_DEFINED_ATTRIBUTE_DEFAULT_VALUE
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.file_selector_view.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FileSelectorField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    FormField<FileSelectorValue?>, View.OnClickListener {

    override var isRequired: Boolean = false

    private var mCLContent: ConstraintLayout? = null
    private var mIVIcon: AppCompatImageView? = null
    private var mTVTapAction: AppCompatTextView? = null
    private var mTVTitle: AppCompatTextView? = null
    private var mTVError: AppCompatTextView? = null

    private var mIconResourceId: Int? = null
    private var mTapButtonText: String? = null
    private var mTitle: String? = null

    private var mFileSelectorValue: FileSelectorValue? = null
    private var mValueChangeListener: ValueChangeListener<FileSelectorValue?>? = null
    private var mDialogTitle: String? = null
    private var mFileSelectorClickListener: FileSelectorClickListener? = null

    init {
        attrs?.let { setupAttributeSet(it) }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setup()
    }

    override fun isValid(): ValidationResult {
        if (isRequired && mFileSelectorValue == null) {
            return ValidationResult(false, context.getString(R.string.file_selector_default_error))
        }

        return ValidationResult(true, EMPTY)
    }

    override fun showError(message: String) {
        mTVError?.text = message
        mTVError?.visibility = View.VISIBLE
    }

    override fun clearError() {
        mTVError?.visibility = View.GONE
    }

    override fun setup() {
        val view = LayoutInflater.from(context).inflate(R.layout.file_selector_view, this, false)
        mCLContent = view.findViewById(R.id.clContent)
        mIVIcon = view.findViewById(R.id.ivICon)
        mTVTapAction = view.findViewById(R.id.tvTapAction)
        mTVTitle = view.findViewById(R.id.tvTitle)
        mTVError = view.findViewById(R.id.tvError)

        mIconResourceId?.let {
            mIVIcon?.setImageResource(it)
        }

        mTapButtonText?.let {
            mTVTapAction?.text = it
        }

        mTitle?.let {
            mTVTitle?.text = it
        }

        view.setOnClickListener(this)
        mIVIcon?.setOnClickListener(this)
        clContent?.setOnClickListener(this)
        setOnClickListener(this)

        addView(view)
    }

    override fun onClick(v: View?) {
        showFileSelectorDialog()
    }

    override fun getValue(): FileSelectorValue? {
        return mFileSelectorValue
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, context.getString(R.string.file_selector_default_error))
    }

    override fun setValueChangeListener(valueChangeListener: ValueChangeListener<FileSelectorValue?>) {
        mValueChangeListener = valueChangeListener
    }

    override fun setIsRequired(required: Boolean) {
        isRequired = required
    }

    fun setFileValue(fileSelectorValue: FileSelectorValue) {
        mFileSelectorValue = fileSelectorValue

        mIVIcon?.let { view ->
            when (fileSelectorValue) {
                is FileSelectorValue.PathValue -> Picasso.get().load(fileSelectorValue.path).into(view)
                is FileSelectorValue.DrawableValue -> view.setImageDrawable(fileSelectorValue.drawable)
                is FileSelectorValue.BitmapValue -> view.setImageBitmap(fileSelectorValue.bitmap)
            }
        }
    }

    fun setFileSelectorClickListener(fileSelectorClickListener: FileSelectorClickListener) {
        mFileSelectorClickListener = fileSelectorClickListener
    }

    private fun showFileSelectorDialog() {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            displayMyMultipleChoiceDialog().let { optionSelected ->
                mFileSelectorClickListener?.onOptionSelected(
                    when (optionSelected) {
                        FILE_SELECTOR_GALLERY_OPTION_INDEX -> FileSelectorOption.Gallery
                        else -> FileSelectorOption.Photo
                    }
                )
            }
        }
    }

    private fun setupAttributeSet(attributes: AttributeSet) {
        val attrsArray = context.obtainStyledAttributes(attributes, R.styleable.FileSelectorField)

        if (attrsArray.hasValue(R.styleable.FileSelectorField_dialog_title)) {
            attrsArray.getString(R.styleable.FileSelectorField_dialog_title).let { dialogTitle ->
                mDialogTitle = dialogTitle
            }
        }

        if (attrsArray.hasValue(R.styleable.FileSelectorField_src_tap_button)) {
            attrsArray.getResourceId(R.styleable.FileSelectorField_src_tap_button, NOT_DEFINED_ATTRIBUTE_DEFAULT_VALUE)
                .let { imageResourceId ->
                    mIconResourceId = imageResourceId
                }
        }

        if (attrsArray.hasValue(R.styleable.FileSelectorField_tap_button_text)) {
            attrsArray.getString(R.styleable.FileSelectorField_tap_button_text)?.let { text ->
                mTapButtonText = text
            }
        }

        if (attrsArray.hasValue(R.styleable.FileSelectorField_tap_button_title)) {
            attrsArray.getString(R.styleable.FileSelectorField_tap_button_title)?.let { title ->
                mTitle = title
            }
        }

        attrsArray.recycle()
    }

    private suspend fun displayMyMultipleChoiceDialog(): Int {
        lateinit var result: Continuation<Int>

        AlertDialog
            .Builder(context)
            .setTitle(mDialogTitle ?: EMPTY)
            .setItems(
                arrayOf<CharSequence>(
                    context.getString(R.string.gallery_string),
                    context.getString(R.string.photo_string)
                )
            ) { dialog, which ->
                dialog.dismiss()
                result.resume(which)
            }
            .create()
            .show()

        return suspendCoroutine { continuation -> result = continuation }
    }
}
