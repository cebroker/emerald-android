package co.condorlabs.customcomponents.fileselectorview

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
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
    private var fileSelectorOptionsList: ArrayList<CharSequence>? = null
    private var hasCameraOption = false
    private var hasGalleryOption = false
    private var hasFileOption = false

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
        ivIcon?.tag = R.drawable.ic_cloud_upload_file
        tvTapAction = view.findViewById(R.id.tvTapAction)
        tvTitle = view.findViewById(R.id.tvTitle)
        tvError = view.findViewById(R.id.tvError)

        iconResourceId?.let {
            ivIcon?.setImageResource(it)
            ivIcon?.setTag(it)
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
                    hasFileOption -> fileSelectorClickListener?.onOptionSelected(FileSelectorOption.File)
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

    private fun showFileSelectorDialog(optionsList: ArrayList<CharSequence>) {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            displayMyMultipleChoiceDialog(optionsList).let { optionSelected ->
                fileSelectorClickListener?.onOptionSelected(
                    when (optionsList[optionSelected]) {
                        FILE_SELECTOR_OPTION_PHOTO -> FileSelectorOption.Photo
                        FILE_SELECTOR_OPTION_GALLERY -> FileSelectorOption.Gallery
                        FILE_SELECTOR_OPTION_FILE -> FileSelectorOption.File
                        else -> FileSelectorOption.File
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
            hasFileOption =
                (attrsArray.getInt(
                    R.styleable.FileSelectorField_fileSelectorOptions,
                    -1
                ) and MASK_TO_OBTAIN_FILE_SELECTOR_OPTION_FILE) == MASK_TO_OBTAIN_FILE_SELECTOR_OPTION_FILE

            fileSelectorOptionsList = arrayListOf()
            if (hasCameraOption) {
                fileSelectorOptionsList?.add(FILE_SELECTOR_OPTION_PHOTO)
            }
            if (hasGalleryOption) {
                fileSelectorOptionsList?.add(FILE_SELECTOR_OPTION_GALLERY)
            }
            if (hasFileOption) {
                fileSelectorOptionsList?.add(FILE_SELECTOR_OPTION_FILE)
            }
            if (fileSelectorOptionsList?.isEmpty() == true) {
                throw FileSelectorViewOptionsNotFound()
            }
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

    private suspend fun displayMyMultipleChoiceDialog(optionList: ArrayList<CharSequence>): Int {
        lateinit var result: Continuation<Int>

        AlertDialog
            .Builder(context)
            .setTitle(dialogTitle ?: EMPTY)
            .setItems(optionList.toTypedArray()) { dialog, which ->
                dialog.dismiss()
                result.resume(which)
            }
            .create()
            .show()

        return suspendCoroutine { continuation -> result = continuation }
    }

    fun setFileValue(fileSelectorValue: FileSelectorValue) {
        this.fileSelectorValue = fileSelectorValue
        ivIcon?.tag = TAG_IMAGE_VIEW_FILE_SELECTOR_VALUE
        ivIcon?.let { view ->
            when (fileSelectorValue) {
                is FileSelectorValue.PathValue -> Picasso.get().load(fileSelectorValue.path).into(view)
                is FileSelectorValue.DrawableValue -> view.setImageDrawable(fileSelectorValue.drawable)
                is FileSelectorValue.BitmapValue -> view.setImageBitmap(fileSelectorValue.bitmap)
                is FileSelectorValue.FileValue -> setFileIconInView(view, fileSelectorValue)
            }
        }
    }

    private fun setFileIconInView(view: AppCompatImageView, fileValue: FileSelectorValue.FileValue) {
        val extension = with(fileValue.filepath) {
            val index = lastIndexOf(DOT_STRING) + FILE_AFTER_DOT_INDEX
            if (index < length) {
                substring(index)
            } else {
                EMPTY
            }
        }
        when (extension) {
            EXTENSION_PDF -> view.setImageResource(R.drawable.ic_file_pdf)
            EXTENSION_DOC, EXTENSION_DOCX -> view.setImageResource(R.drawable.ic_file_doc)
            EXTENSION_JPEG, EXTENSION_JPG, EXTENSION_PNG -> {
                val bitmap = BitmapFactory.decodeFile(fileValue.filepath)
                view.setImageBitmap(bitmap)
                view.requestLayout()
            }
            else -> view.setImageResource(R.drawable.ic_file_base)
        }
        if (fileValue.filename != null) {
            tvFilename?.text = fileValue.filename
            tvFilename?.visibility = View.VISIBLE
        } else {
            tvFilename?.visibility = View.GONE
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
        if (ivIcon?.tag == R.drawable.ic_cloud_upload_file) {
            ivIcon?.tag = R.drawable.ic_cloud_upload_file_disabled
            ivIcon?.setImageResource(R.drawable.ic_cloud_upload_file_disabled)
        }
        tvTitle?.setTextColor(context.resources.getColor(R.color.emerald_disable_file_selector))
        tvTapAction?.setTextColor(context.resources.getColor(R.color.emerald_disable_file_selector))
        clContent?.background = context.getDrawable(R.drawable.ripple_disable_background)
    }

    private fun setEnableColors() {
        if (ivIcon?.tag == R.drawable.ic_cloud_upload_file_disabled) {
            ivIcon?.tag = R.drawable.ic_cloud_upload_file
            ivIcon?.setImageResource(R.drawable.ic_cloud_upload_file)
        }
        tvTitle?.setTextColor(context.resources.getColor(R.color.primaryColor))
        tvTapAction?.setTextColor(context.resources.getColor(R.color.primaryColor))
        clContent?.background = context.getDrawable(R.drawable.ripple)
    }
}
