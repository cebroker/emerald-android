package co.condorlabs.customcomponents.fileselectorview

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import com.squareup.picasso.Picasso
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
    private var clContent: ConstraintLayout? = null
    private var ivIcon: AppCompatImageView? = null
    private var tvTapAction: AppCompatTextView? = null
    private var tvTitle: AppCompatTextView? = null
    private var tvError: AppCompatTextView? = null
    private var iconResourceId: Int? = null
    private var tapButtonText: String? = null
    private var title: String? = null
    private var fileSelectorValue: FileSelectorValue? = null
    private var valueChangeListener: ValueChangeListener<FileSelectorValue?>? = null
    private var dialogTitle: String? = null
    private var fileSelectorClickListener: FileSelectorClickListener? = null
    private var fileSelectorOptionsList: Array<CharSequence>? = null
    private var hasCameraOption = false
    private var hasGalleryOption = false

    init {
        attrs?.let { setupAttributeSet(it) }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setup()
    }

    override fun isValid(): ValidationResult {
        if (isRequired && fileSelectorValue == null) {
            return ValidationResult(false, context.getString(R.string.file_selector_default_error))
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

    override fun setup() {
        val view = LayoutInflater.from(context).inflate(R.layout.file_selector_view, this, false)
        clContent = view.findViewById(R.id.clContent)
        ivIcon = view.findViewById(R.id.ivIcon)
        tvTapAction = view.findViewById(R.id.tvTapAction)
        tvTitle = view.findViewById(R.id.tvTitle)
        tvError = view.findViewById(R.id.tvError)

        iconResourceId?.let {
            ivIcon?.setImageResource(it)
        }

        tapButtonText?.let {
            tvTapAction?.text = it
        }

        title?.let {
            tvTitle?.text = it
        }

        clContent?.setOnClickListener(this)

        addView(view)
    }

    override fun onClick(v: View?) {
        fileSelectorOptionsList?.let {
            if (it.size > ONE) {
                showFileSelectorDialog(it)
            } else {
                when {
                    hasCameraOption -> fileSelectorClickListener?.onOptionSelected(FileSelectorOption.Photo)
                    hasGalleryOption -> fileSelectorClickListener?.onOptionSelected(FileSelectorOption.Gallery)
                    else -> return
                }
            }
        }
    }

    override fun getValue(): FileSelectorValue? {
        return fileSelectorValue
    }

    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, context.getString(R.string.file_selector_default_error))
    }

    override fun setValueChangeListener(valueChangeListener: ValueChangeListener<FileSelectorValue?>) {
        this.valueChangeListener = valueChangeListener
    }

    override fun setIsRequired(required: Boolean) {
        isRequired = required
    }

    private fun showFileSelectorDialog(optionsList: Array<CharSequence>) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            displayMyMultipleChoiceDialog(optionsList).let { optionSelected ->
                fileSelectorClickListener?.onOptionSelected(
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

        if (attrsArray.hasValue(R.styleable.FileSelectorField_fileSelectorOptions)) {
            hasGalleryOption =
                (attrsArray.getInt(
                    R.styleable.FileSelectorField_fileSelectorOptions,
                    -1
                ) and MASK_TO_OBTAIN_FILE_SELECTOR_OPTION_GALLERY) == MASK_TO_OBTAIN_FILE_SELECTOR_OPTION_GALLERY
            hasCameraOption =
                (attrsArray.getInt(
                    R.styleable.FileSelectorField_fileSelectorOptions,
                    -1
                ) and MASK_TO_OBTAIN_FILE_SELECTOR_OPTION_CAMERA) == MASK_TO_OBTAIN_FILE_SELECTOR_OPTION_CAMERA

            fileSelectorOptionsList = if (hasCameraOption && hasGalleryOption) {
                arrayOf(
                    context.getString(R.string.gallery_string),
                    context.getString(R.string.photo_string)
                )
            } else if (hasGalleryOption) {
                arrayOf<CharSequence>(context.getString(R.string.gallery_string))
            } else if (hasCameraOption) {
                arrayOf<CharSequence>(context.getString(R.string.photo_string))
            } else throw FileSelectorViewOptionsNotFound()
        }

        if (attrsArray.hasValue(R.styleable.FileSelectorField_dialog_title)) {
            attrsArray.getString(R.styleable.FileSelectorField_dialog_title).let { dialogTitle ->
                this.dialogTitle = dialogTitle
            }
        }

        if (attrsArray.hasValue(R.styleable.FileSelectorField_src_tap_button)) {
            attrsArray.getResourceId(
                R.styleable.FileSelectorField_src_tap_button,
                NOT_DEFINED_ATTRIBUTE_DEFAULT_VALUE
            ).let { imageResourceId ->
                iconResourceId = imageResourceId
            }
        }

        if (attrsArray.hasValue(R.styleable.FileSelectorField_tap_button_text)) {
            attrsArray.getString(R.styleable.FileSelectorField_tap_button_text)?.let { text ->
                tapButtonText = text
            }
        }

        if (attrsArray.hasValue(R.styleable.FileSelectorField_tap_button_title)) {
            attrsArray.getString(R.styleable.FileSelectorField_tap_button_title)?.let { title ->
                this.title = title
            }
        }

        attrsArray.recycle()
    }

    private suspend fun displayMyMultipleChoiceDialog(optionList: Array<CharSequence>): Int {
        lateinit var result: Continuation<Int>

        AlertDialog
            .Builder(context)
            .setTitle(dialogTitle ?: EMPTY)
            .setItems(optionList) { dialog, which ->
                dialog.dismiss()
                result.resume(which)
            }
            .create()
            .show()

        return suspendCoroutine { continuation -> result = continuation }
    }

    fun setFileValue(fileSelectorValue: FileSelectorValue) {
        this.fileSelectorValue = fileSelectorValue

        ivIcon?.let { view ->
            when (fileSelectorValue) {
                is FileSelectorValue.PathValue -> Picasso.get().load(fileSelectorValue.path).into(view)
                is FileSelectorValue.DrawableValue -> view.setImageDrawable(fileSelectorValue.drawable)
                is FileSelectorValue.BitmapValue -> view.setImageBitmap(fileSelectorValue.bitmap)
            }
        }
    }

    fun setFileSelectorClickListener(fileSelectorClickListener: FileSelectorClickListener) {
        this.fileSelectorClickListener = fileSelectorClickListener
    }

    fun setEnable(isEnable: Boolean) {
        clContent?.isEnabled = isEnable
        ivIcon?.isEnabled = isEnable
        tvTitle?.isEnabled = isEnable
        tvTapAction?.isEnabled = isEnable

        if (isEnable) {
            setEnableColors()
        } else {
            setDisableColors()
        }
    }

    private fun setDisableColors() {
        ivIcon?.setColorFilter(context.resources.getColor(R.color.emerald_disable_file_selector))
        tvTitle?.setTextColor(context.resources.getColor(R.color.emerald_disable_file_selector))
        tvTapAction?.setTextColor(context.resources.getColor(R.color.emerald_disable_file_selector))
        clContent?.background = context.getDrawable(R.drawable.ripple_disable_background)
    }

    private fun setEnableColors() {
        ivIcon?.setColorFilter(context.resources.getColor(R.color.emerald_enable_file_selector_icon))
        tvTitle?.setTextColor(context.resources.getColor(R.color.text_tap_color))
        tvTapAction?.setTextColor(context.resources.getColor(R.color.text_tap_color))
        clContent?.background = context.getDrawable(R.drawable.ripple)
    }
}
