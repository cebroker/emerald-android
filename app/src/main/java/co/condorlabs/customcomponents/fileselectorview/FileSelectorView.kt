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
import co.condorlabs.customcomponents.helper.EMPTY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FileSelectorView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var mConstraintLayoutTapButton: ConstraintLayout? = null
    private var mImageViewIcon: AppCompatImageView? = null
    private var mTextViewTitleTabButton: AppCompatTextView? = null
    private var mTextViewSubtitleTabButton: AppCompatTextView? = null
    private var mTextViewTextTabButton: AppCompatTextView? = null

    private var mOnClickListener: OnClickListener? = null
    private var mDialogTitle: String? = null
    private var mDialogOptionsResourceId: Int? = null

    init {
        setContentView()

        findViewsInLayoutResourceFile()

        attrs?.let { setupAttributeSet(it) }

        mConstraintLayoutTapButton?.setOnClickListener {
            showFileSelectorDialog()
        }
    }

    private fun showFileSelectorDialog() {
        CoroutineScope(Dispatchers.Main + Job()).launch {
            displayMyMultipleChoiceDialog().let {
                mOnClickListener?.onClick(it)
            }
        }
    }

    private fun setContentView() {
        LayoutInflater.from(context).inflate(R.layout.file_selector_view, this, true)
    }

    private fun findViewsInLayoutResourceFile() {
        mConstraintLayoutTapButton = findViewById(R.id.containerLayout)
        mImageViewIcon = findViewById(R.id.iconImageView)
        mTextViewTitleTabButton = findViewById(R.id.titleTextView)
        mTextViewSubtitleTabButton = findViewById(R.id.subtitleTextView)
        mTextViewTextTabButton = findViewById(R.id.textTapTextView)
    }

    private fun setupAttributeSet(attributes: AttributeSet) {
        val attrsArray = context.obtainStyledAttributes(attributes, R.styleable.FileSelectorView)

        if (attrsArray.hasValue(R.styleable.FileSelectorView_dialog_title)) {
            attrsArray.getString(R.styleable.FileSelectorView_dialog_title).let { dialogTitle ->
                mDialogTitle = dialogTitle
            }
        }

        if (attrsArray.hasValue(R.styleable.FileSelectorView_dialog_options)) {
            attrsArray.getResourceId(R.styleable.FileSelectorView_dialog_options, -1).let { optionsArrayResourceId ->
                mDialogOptionsResourceId = optionsArrayResourceId
            }
        }

        if (attrsArray.hasValue(R.styleable.FileSelectorView_src_tap_button)) {
            attrsArray.getResourceId(R.styleable.FileSelectorView_src_tap_button, -1).let { imageResourceId ->
                mImageViewIcon?.setImageResource(imageResourceId)
            }
        }

        if (attrsArray.hasValue(R.styleable.FileSelectorView_tap_button_text)) {
            attrsArray.getString(R.styleable.FileSelectorView_tap_button_text)?.let { text ->
                mTextViewTextTabButton?.setText(text)
            }
        }

        if (attrsArray.hasValue(R.styleable.FileSelectorView_tap_button_title)) {
            attrsArray.getString(R.styleable.FileSelectorView_tap_button_title)?.let { title ->
                mTextViewTitleTabButton?.apply {
                    this.text = title
                    visibility = View.VISIBLE
                }
            }
        }

        if (attrsArray.hasValue(R.styleable.FileSelectorView_tap_button_subtitle)) {
            attrsArray.getString(R.styleable.FileSelectorView_tap_button_subtitle)?.let { subtitle ->
                mTextViewSubtitleTabButton?.apply {
                    this.text = subtitle
                    visibility = View.VISIBLE
                }
            }
        }

        attrsArray.recycle()
    }

    private suspend fun displayMyMultipleChoiceDialog(): Int {
        lateinit var result: Continuation<Int>
        mDialogOptionsResourceId?.let { optionsResourceId ->
            AlertDialog
                    .Builder(context)
                    .setTitle(mDialogTitle ?: EMPTY)
                    .setItems(optionsResourceId) { dialog, which ->
                        dialog.dismiss()
                        result.resume(which)
                    }
                    .create()
                    .show()
        }

        return suspendCoroutine { continuation -> result = continuation }
    }

    fun setOnClickListener(listener: OnClickListener) {
        if (!isClickable) {
            isClickable = true
        }
        mOnClickListener = listener
    }

    /**
     * Interface definition for a callback to be invoked when an option of dialog is clicked.
     */
    interface OnClickListener {
        /**
         * Called when an option of dialog has been clicked.
         *
         * @param optionSelectedId The option position that was clicked.
         */
        fun onClick(optionSelectedId: Int)
    }
}
