package co.condorlabs.customcomponents.fileselector

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import co.condorlabs.customcomponents.R

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

    init {
        setContentView()

        findViewsInLayoutResourceFile()

        attrs?.let { setupAttributeSet(it) }

        mConstraintLayoutTapButton?.setOnClickListener {
            Toast.makeText(context, "Sirve!!", Toast.LENGTH_LONG).show()
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

    private fun setupAttributeSet(atributes: AttributeSet) {
        val attrsArray = context.obtainStyledAttributes(atributes, R.styleable.FileSelectorView)

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
}
