package co.condorlabs.customcomponents.customcollapsibleview

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_WRAP
import androidx.constraintlayout.widget.ConstraintSet
import co.condorlabs.customcomponents.*

/**
 * @author Alexis Duque on 2019-05-29.
 * @company Condor Labs
 * @email eduque@condorlabs.io
 */
class CollapsibleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = ZERO
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var collapsibleViewId: Int = NO_ID
    private var requirementsHeight = ZERO
    private var elementsAreCollapsing = false
    private var collapseListener: OnCollapseListener? = null
    private var textHiddenState: String? = null
    private var textShowState: String? = null
    private var sectionTagTextView = AppCompatTextView(context).apply { id = R.id.tagId }
    private var sectionTitleTextView = AppCompatTextView(context).apply { id = R.id.titleId }
    private var sectionSubtitleTextView = AppCompatTextView(context).apply { id = R.id.subtitleId }
    private var sectionCardView = CardView(context).apply { id = R.id.cardId }
    private var sectionConstraintLayout = ConstraintLayout(context)
    private var lineSeparatorView = View(context).apply { id = R.id.lineSeparatorId }
    private var containerFrameLayout = FrameLayout(context).apply { id = R.id.frameLayoutId }
    private var footerConstraintLayout = ConstraintLayout(context).apply { id = R.id.footerId }
    private var footerTextTextView = AppCompatTextView(context).apply { id = R.id.footerTextViewId }
    private var footerIndicatorImageView = AppCompatImageView(context).apply { id = R.id.footerImageViewId }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val typedArray = context.obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackground))
            val selectableItemBackground = typedArray.getResourceId(ZERO, ZERO)
            typedArray.recycle()

            this.foreground = context.getDrawable(selectableItemBackground)
        }

        isClickable = true
        isFocusable = true
        setPadding(ZERO, DEFAULT_COLLAPSIBLE_PADDING, ZERO, DEFAULT_COLLAPSIBLE_PADDING)
        addView(sectionTagTextView)
        addView(sectionCardView)

        sectionTagTextView.apply {
            layoutParams = LayoutParams(
                MATCH_CONSTRAINT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            typeface = Typeface.DEFAULT_BOLD
        }

        sectionCardView.apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                useCompatPadding = true
            }
            radius = ZERO_FLOAT
            addView(sectionConstraintLayout)
        }

        sectionConstraintLayout.apply {
            setPadding(COLLAPSIBLE_PADDING_START, DEFAULT_COLLAPSIBLE_PADDING, COLLAPSIBLE_PADDING_END, ZERO)
            addView(sectionTitleTextView)
            addView(sectionSubtitleTextView)
            addView(lineSeparatorView)
            addView(containerFrameLayout)
            addView(footerConstraintLayout)
        }

        sectionTitleTextView.apply {
            layoutParams = LayoutParams(
                MATCH_CONSTRAINT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.BLACK)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_COLLAPSIBLE_TITLE_TEXT_SIZE)
        }

        sectionSubtitleTextView.apply {
            layoutParams = LayoutParams(
                MATCH_CONSTRAINT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        lineSeparatorView.apply {
            layoutParams = LayoutParams(
                MATCH_CONSTRAINT,
                MATCH_CONSTRAINT_WRAP
            )
            setBackgroundResource(R.color.background_line_separator_view)
        }

        containerFrameLayout.apply {
            layoutParams = LayoutParams(
                MATCH_CONSTRAINT,
                LayoutParams.WRAP_CONTENT
            )
        }

        footerConstraintLayout.apply {
            layoutParams = LayoutParams(
                MATCH_CONSTRAINT,
                LayoutParams.WRAP_CONTENT
            )
            setPadding(
                DEFAULT_COLLAPSIBLE_PADDING, DEFAULT_COLLAPSIBLE_PADDING,
                DEFAULT_COLLAPSIBLE_PADDING, DEFAULT_COLLAPSIBLE_PADDING
            )
            addView(footerTextTextView)
            addView(footerIndicatorImageView)
        }

        footerTextTextView.apply {
            layoutParams = LayoutParams(
                MATCH_CONSTRAINT,
                LayoutParams.WRAP_CONTENT
            )
            setTextColor(resources.getColor(android.R.color.holo_blue_light))
        }

        footerIndicatorImageView.apply {
            setImageResource(R.drawable.ic_arrow_down)
            rotation = COLLAPSIBLE_INDICATOR_ROTATION
        }

        ConstraintSet().apply {
            clone(footerConstraintLayout)
            with(footerTextTextView) {
                connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                connect(id, ConstraintSet.END, footerIndicatorImageView.id, ConstraintSet.START)
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            }
            with(footerIndicatorImageView) {
                connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            }
            applyTo(footerConstraintLayout)
        }

        ConstraintSet().apply {
            clone(sectionConstraintLayout)
            with(sectionTitleTextView) {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            }
            with(sectionSubtitleTextView) {
                connect(id, ConstraintSet.BOTTOM, lineSeparatorView.id, ConstraintSet.TOP, DEFAULT_COLLAPSIBLE_MARGIN)
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.START, sectionTitleTextView.id, ConstraintSet.START)
                connect(id, ConstraintSet.TOP, sectionTitleTextView.id, ConstraintSet.BOTTOM)
            }
            with(lineSeparatorView) {
                connect(
                    id,
                    ConstraintSet.START,
                    sectionSubtitleTextView.id,
                    ConstraintSet.START,
                    COLLAPSIBLE_LINE_SEPARATOR_START_MARGIN
                )
                connect(id, ConstraintSet.BOTTOM, containerFrameLayout.id, ConstraintSet.TOP)
            }
            with(containerFrameLayout) {
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.BOTTOM, footerConstraintLayout.id, ConstraintSet.TOP)
            }
            with(footerConstraintLayout) {
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }
            applyTo(sectionConstraintLayout)
        }

        ConstraintSet().apply {
            clone(this@CollapsibleView)
            with(sectionTagTextView) {
                connect(
                    id,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START,
                    COLLAPSIBLE_TAG_MARGIN
                )
                connect(id, ConstraintSet.END, this@CollapsibleView.id, ConstraintSet.END, COLLAPSIBLE_TAG_MARGIN)
                connect(id, ConstraintSet.TOP, this@CollapsibleView.id, ConstraintSet.TOP)
            }
            with(sectionCardView) {
                connect(
                    id,
                    ConstraintSet.TOP,
                    sectionTagTextView.id,
                    ConstraintSet.BOTTOM,
                    COLLAPSIBLE_CARD_VIEW_TOP_MARGIN
                )
                connect(id, ConstraintSet.BOTTOM, this@CollapsibleView.id, ConstraintSet.BOTTOM)
                connect(
                    id,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START,
                    COLLAPSIBLE_CARD_VIEW_START_MARGIN
                )
                connect(
                    id,
                    ConstraintSet.END,
                    this@CollapsibleView.id,
                    ConstraintSet.END,
                    COLLAPSIBLE_CARD_VIEW_END_MARGIN
                )
            }
            applyTo(this@CollapsibleView)
        }

        attrs?.let { setupAttrs(it) }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (collapsibleViewId != NO_ID) {
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater)
                ?.inflate(collapsibleViewId, null, false)?.let {
                    setCollapsibleContent(it)
                }

            (context as? AppCompatActivity)?.windowManager?.defaultDisplay?.let { display ->
                containerFrameLayout.measure(display.width, display.height)
                requirementsHeight = containerFrameLayout.measuredHeight
            }

            if (elementsAreCollapsing) {
                updateContainerViewHeight()
                rotateRowIndicator()
                callOnCollapse(elementsAreCollapsing)
            }

            updateFooterText(elementsAreCollapsing)

            setOnClickListener { collapseElements(requirementsHeight) }
        }
    }

    private fun setupAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CollapsibleView,
            DEFAULT_STYLE_ATTR,
            DEFAULT_STYLE_RES
        )

        collapsibleViewId = typedArray.getResourceId(R.styleable.CollapsibleView_collapsibleContent, NO_ID)
        elementsAreCollapsing = typedArray.getBoolean(R.styleable.CollapsibleView_startCollapsed, false)
        textHiddenState = typedArray.getString(R.styleable.CollapsibleView_collapsibleHiddenFooterText)
            ?: resources.getString(R.string.hide)
        textShowState = typedArray.getString(R.styleable.CollapsibleView_collapsibleShowFooterText)
            ?: resources.getString(R.string.show)
        setSectionTag(typedArray.getString(R.styleable.CollapsibleView_collapsibleTag))
        setSectionTitle(typedArray.getString(R.styleable.CollapsibleView_collapsibleTitle))
        setSectionSubtitle(typedArray.getString(R.styleable.CollapsibleView_collapsibleSubtitle))
        setSectionFooterTextColor(typedArray.getColor(R.styleable.CollapsibleView_collapsibleFooterTextColor, NO_ID))
        setSectionTagColor(typedArray.getColor(R.styleable.CollapsibleView_collapsibleTagColor, NO_ID))

        typedArray.recycle()
    }

    private fun collapseElements(requirementsHeight: Int) {
        if (elementsAreCollapsing) {
            collapseElements(ZERO, requirementsHeight, false)
        } else {
            collapseElements(requirementsHeight, ZERO, true)
        }

        elementsAreCollapsing = !elementsAreCollapsing

        callOnCollapse(elementsAreCollapsing)
    }

    private fun collapseElements(from: Int, to: Int, isCollapsed: Boolean) {
        updateFooterText(isCollapsed)

        val animator = ValueAnimator.ofInt(from, to).setDuration(DEFAULT_COLLAPSIBLE_ANIMATION_TIME)?.apply {
            addUpdateListener {
                updateContainerViewHeight(it.animatedValue as Int)
                rotateRowIndicator(
                    rotationValue(
                        it.animatedValue as Int,
                        if (from > to) from else to
                    )
                )
            }
        } ?: return

        startValueAnimator(animator)
    }

    private fun startValueAnimator(valueAnimator: ValueAnimator) {
        AnimatorSet().apply {
            play(valueAnimator)
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun updateContainerViewHeight(height: Int = ZERO) {
        containerFrameLayout.apply {
            layoutParams?.height = height
            requestLayout()
        }
    }

    private fun rotateRowIndicator(rotation: Float = ZERO_FLOAT) {
        footerIndicatorImageView.rotation = -rotation
    }

    private fun rotationValue(currentValue: Int, limit: Int): Float {
        return (COLLAPSIBLE_INDICATOR_ROTATION * ((currentValue * ONE_HUNDRED_FLOAT) / limit)) / ONE_HUNDRED_FLOAT
    }

    private fun updateFooterText(isCollapsed: Boolean) {
        footerTextTextView.text = if (isCollapsed) textShowState else textHiddenState
    }

    fun setCollapsibleContent(collapsibleContent: View) {
        containerFrameLayout.addView(collapsibleContent)
    }

    fun getCollapsibleContent(): View? {
        return if (containerFrameLayout.childCount > ZERO) {
            containerFrameLayout.getChildAt(ZERO)
        } else {
            null
        }
    }

    fun setSectionTag(sectionTag: String?) {
        sectionTagTextView.text = sectionTag
    }

    fun setSectionTitle(sectionTitle: String?) {
        sectionTitleTextView.text = sectionTitle
    }

    fun setSectionSubtitle(sectionSubtitle: String?) {
        sectionSubtitleTextView.text = sectionSubtitle
    }

    fun setSectionHiddenFooterText(hiddenFooterText: String?) {
        textHiddenState = hiddenFooterText
    }

    fun setSectionShowFooterText(showFooterText: String?) {
        textShowState = showFooterText
    }

    fun setStartCollapsed() {
        elementsAreCollapsing = true
    }

    fun setSectionFooterTextColor(footerTextColor: Int?) {
        if (footerTextColor != null && footerTextColor != NO_ID) {
            footerTextTextView.setTextColor(footerTextColor)
        }
    }

    fun setSectionTagColor(footerTagColor: Int?) {
        if (footerTagColor != null && footerTagColor != NO_ID) {
            sectionTagTextView.setTextColor(footerTagColor)
        }
    }

    fun setOnCollapseListener(collapseListener: OnCollapseListener?) {
        this.collapseListener = collapseListener
    }

    fun callOnCollapse(isCollapsed: Boolean): Boolean {
        return collapseListener?.let { it.onCollapse(isCollapsed); true } ?: false
    }

    /**
     * Interface definition for a callback to be invoked when a collapsible view is collapsed.
     */
    interface OnCollapseListener {
        /**
         * Called when a collapsible view has been collapsed.
         *
         * @param isCollapsed The Boolean that indicates if the view is collapsed.
         */
        fun onCollapse(isCollapsed: Boolean)
    }
}
