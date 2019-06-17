package co.condorlabs.customcomponents.customcollapsibleview

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import co.condorlabs.customcomponents.COLLAPSIBLE_CARD_VIEW_END_MARGIN
import co.condorlabs.customcomponents.COLLAPSIBLE_CARD_VIEW_START_MARGIN
import co.condorlabs.customcomponents.COLLAPSIBLE_INDICATOR_ROTATION
import co.condorlabs.customcomponents.COLLAPSIBLE_LINE_SEPARATOR_HEIGHT
import co.condorlabs.customcomponents.COLLAPSIBLE_LINE_SEPARATOR_START_MARGIN
import co.condorlabs.customcomponents.DEFAULT_COLLAPSIBLE_ANIMATION_TIME
import co.condorlabs.customcomponents.DEFAULT_COLLAPSIBLE_ICON_HEIGHT
import co.condorlabs.customcomponents.DEFAULT_COLLAPSIBLE_ICON_WIDTH
import co.condorlabs.customcomponents.DEFAULT_COLLAPSIBLE_INDICATOR_MARGIN_END
import co.condorlabs.customcomponents.DEFAULT_COLLAPSIBLE_MARGIN
import co.condorlabs.customcomponents.DEFAULT_COLLAPSIBLE_MARGIN_FOOTER_TEXT
import co.condorlabs.customcomponents.DEFAULT_COLLAPSIBLE_PADDING
import co.condorlabs.customcomponents.DEFAULT_COLLAPSIBLE_SUBTITLE_MARGIN_BOTTOM
import co.condorlabs.customcomponents.DEFAULT_COLLAPSIBLE_SUBTITLE_TOP_MARGIN
import co.condorlabs.customcomponents.DEFAULT_COLLAPSIBLE_TITLE_MARGIN_TOP
import co.condorlabs.customcomponents.DEFAULT_COLLAPSIBLE_TITLE_TEXT_SIZE
import co.condorlabs.customcomponents.DEFAULT_STYLE_ATTR
import co.condorlabs.customcomponents.DEFAULT_STYLE_RES
import co.condorlabs.customcomponents.ONE_HUNDRED_FLOAT
import co.condorlabs.customcomponents.OPEN_SANS_SEMI_BOLD
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.ZERO
import co.condorlabs.customcomponents.ZERO_FLOAT

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
    private var elementsAreCollapsed = false
    private var collapseListener: OnCollapseListener? = null
    private var textHiddenState: String? = null
    private var textShowState: String? = null
    private var iconImageView = AppCompatImageView(context).apply { id = R.id.iconImageId }
    private var sectionTitleTextView = AppCompatTextView(context).apply { id = R.id.titleId }
    private var sectionSubtitleTextView = AppCompatTextView(context).apply { id = R.id.subtitleId }
    private var sectionCardView = CardView(context).apply { id = R.id.cardId }
    private var sectionConstraintLayout = ConstraintLayout(context)
    private var lineSeparatorView = View(context).apply { id = R.id.lineSeparatorId }
    private var containerFrameLayout = FrameLayout(context).apply { id = R.id.frameLayoutId }
    private var footerConstraintLayout = ConstraintLayout(context).apply { id = R.id.footerId }
    private var footerTextTextView = AppCompatTextView(context).apply { id = R.id.footerTextViewId }
    private var footerIndicatorImageView =
        AppCompatImageView(context).apply { id = R.id.footerImageViewId }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val typedArray =
                context.obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackground))
            val selectableItemBackground = typedArray.getResourceId(ZERO, ZERO)
            typedArray.recycle()

            sectionConstraintLayout.foreground = context.getDrawable(selectableItemBackground)
        }

        isClickable = true
        isFocusable = true
        setPadding(ZERO, ZERO, ZERO, ZERO)
        addView(sectionCardView)

        sectionCardView.apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            radius = ZERO_FLOAT
            addView(sectionConstraintLayout)
        }

        sectionConstraintLayout.apply {
            addView(iconImageView)
            addView(sectionTitleTextView)
            addView(sectionSubtitleTextView)
            addView(lineSeparatorView)
            addView(containerFrameLayout)
            addView(footerConstraintLayout)
        }

        iconImageView.apply {
            layoutParams = LayoutParams(
                DEFAULT_COLLAPSIBLE_ICON_WIDTH,
                DEFAULT_COLLAPSIBLE_ICON_HEIGHT
            )
            scaleType = ImageView.ScaleType.CENTER
        }

        sectionTitleTextView.apply {
            layoutParams = LayoutParams(
                MATCH_CONSTRAINT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            typeface = Typeface.createFromAsset(context.assets, OPEN_SANS_SEMI_BOLD)
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
                COLLAPSIBLE_LINE_SEPARATOR_HEIGHT
            )
            setBackgroundResource(R.color.linear_separator)
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
            setTextColor(ContextCompat.getColor(context, R.color.primaryColor))
            typeface = Typeface.createFromAsset(context.assets, OPEN_SANS_SEMI_BOLD)
        }

        footerIndicatorImageView.apply {
            setImageResource(R.drawable.ic_arrow_down)
            rotation = COLLAPSIBLE_INDICATOR_ROTATION
        }

        ConstraintSet().apply {
            clone(footerConstraintLayout)
            with(footerTextTextView) {
                connect(
                    id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM,
                    DEFAULT_COLLAPSIBLE_MARGIN_FOOTER_TEXT
                )
                connect(id, ConstraintSet.END, footerIndicatorImageView.id, ConstraintSet.START)
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(
                    id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP,
                    DEFAULT_COLLAPSIBLE_MARGIN_FOOTER_TEXT
                )
            }
            with(footerIndicatorImageView) {
                connect(
                    id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM
                )
                connect(
                    id,
                    ConstraintSet.END,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.END,
                    DEFAULT_COLLAPSIBLE_INDICATOR_MARGIN_END
                )
                connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            }
            applyTo(footerConstraintLayout)
        }

        ConstraintSet().apply {
            clone(sectionConstraintLayout)
            with(iconImageView) {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.TOP, sectionTitleTextView.id, ConstraintSet.TOP)
                connect(id, ConstraintSet.BOTTOM, sectionTitleTextView.id, ConstraintSet.BOTTOM)
            }
            with(sectionTitleTextView) {
                connect(
                    id,
                    ConstraintSet.START,
                    iconImageView.id,
                    ConstraintSet.END,
                    DEFAULT_COLLAPSIBLE_MARGIN
                )
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(
                    id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP,
                    DEFAULT_COLLAPSIBLE_TITLE_MARGIN_TOP
                )
            }
            with(sectionSubtitleTextView) {
                connect(
                    id,
                    ConstraintSet.BOTTOM,
                    lineSeparatorView.id,
                    ConstraintSet.TOP,
                    DEFAULT_COLLAPSIBLE_SUBTITLE_MARGIN_BOTTOM
                )
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.START, sectionTitleTextView.id, ConstraintSet.START)
                connect(
                    id,
                    ConstraintSet.TOP,
                    sectionTitleTextView.id,
                    ConstraintSet.BOTTOM,
                    DEFAULT_COLLAPSIBLE_SUBTITLE_TOP_MARGIN
                )
            }
            with(lineSeparatorView) {
                connect(
                    id,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
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
                connect(id, ConstraintSet.START, sectionTitleTextView.id, ConstraintSet.START)
                connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }
            applyTo(sectionConstraintLayout)
        }

        ConstraintSet().apply {
            clone(this@CollapsibleView)

            with(sectionCardView) {
                connect(
                    id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM
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
                    setContent(it)
                }
        }
    }

    private fun setupAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CollapsibleView,
            DEFAULT_STYLE_ATTR,
            DEFAULT_STYLE_RES
        )

        val padding =
            typedArray.getDimension(R.styleable.CollapsibleView_android_padding, ZERO_FLOAT)
        if (padding == ZERO_FLOAT) {
            val paddingStart = typedArray.getDimension(
                R.styleable.CollapsibleView_android_paddingStart,
                ZERO_FLOAT
            )
            val paddingTop =
                typedArray.getDimension(R.styleable.CollapsibleView_android_paddingTop, ZERO_FLOAT)
            val paddingEnd =
                typedArray.getDimension(R.styleable.CollapsibleView_android_paddingEnd, ZERO_FLOAT)
            val paddingBottom = typedArray.getDimension(
                R.styleable.CollapsibleView_android_paddingBottom,
                ZERO_FLOAT
            )

            sectionConstraintLayout.setPadding(
                paddingStart.toInt(),
                paddingTop.toInt(),
                paddingEnd.toInt(),
                paddingBottom.toInt()
            )
        } else {
            sectionConstraintLayout.setPadding(
                padding.toInt(),
                padding.toInt(),
                padding.toInt(),
                padding.toInt()
            )
        }

        setImage(typedArray.getResourceId(R.styleable.CollapsibleView_collapsibleIcon, NO_ID))
        sectionCardView.useCompatPadding =
            typedArray.getBoolean(R.styleable.CollapsibleView_useAppCompactPadding, true)
        collapsibleViewId =
            typedArray.getResourceId(R.styleable.CollapsibleView_collapsibleContent, NO_ID)
        elementsAreCollapsed =
            typedArray.getBoolean(R.styleable.CollapsibleView_startCollapsed, false)
        textHiddenState =
            typedArray.getString(R.styleable.CollapsibleView_collapsibleHiddenFooterText)
                ?: resources.getString(R.string.hide)
        textShowState = typedArray.getString(R.styleable.CollapsibleView_collapsibleShowFooterText)
            ?: resources.getString(R.string.show)
        setTitle(typedArray.getString(R.styleable.CollapsibleView_collapsibleTitle))
        setSubtitle(typedArray.getString(R.styleable.CollapsibleView_collapsibleSubtitle))
        setFooterTextColor(
            typedArray.getColor(
                R.styleable.CollapsibleView_collapsibleFooterTextColor,
                NO_ID
            )
        )
        setImageTint(typedArray.getColor(R.styleable.CollapsibleView_imageTintColor, NO_ID))
        seeArrowIndicator(
            typedArray.getBoolean(
                R.styleable.CollapsibleView_seeArrowIndicator,
                false
            )
        )

        typedArray.recycle()
    }

    private fun collapseElements(requirementsHeight: Int) {
        if (elementsAreCollapsed) {
            collapseElements(ZERO, requirementsHeight, false)
        } else {
            collapseElements(requirementsHeight, ZERO, true)
        }

        elementsAreCollapsed = !elementsAreCollapsed

        callOnCollapse(elementsAreCollapsed)
    }

    private fun collapseElements(from: Int, to: Int, isCollapsed: Boolean) {
        updateFooterText(isCollapsed)

        val animator =
            ValueAnimator.ofInt(from, to).setDuration(DEFAULT_COLLAPSIBLE_ANIMATION_TIME)?.apply {
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

    private fun callOnCollapse(isCollapsed: Boolean): Boolean {
        return collapseListener?.let { it.onCollapse(isCollapsed); true } ?: false
    }

    fun setImage(imageIconResourceId: Int) {
        if (imageIconResourceId != NO_ID) {
            val imageDrawableResource = AppCompatResources.getDrawable(context, imageIconResourceId)
            iconImageView.setImageDrawable(imageDrawableResource)
            iconImageView.visibility = View.VISIBLE
        } else {
            iconImageView.visibility = View.GONE
        }
    }

    fun setImageTint(colorTint: Int) {
        if (colorTint != NO_ID) {
            ImageViewCompat.setImageTintList(
                iconImageView,
                AppCompatResources.getColorStateList(context, colorTint)
            )
        }
    }

    fun setContent(collapsibleContent: View) {
        containerFrameLayout.addView(collapsibleContent)

        val displayMetrics = DisplayMetrics()
        (context as? AppCompatActivity)?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            ?.let {
                containerFrameLayout.measure(
                    displayMetrics.widthPixels,
                    displayMetrics.heightPixels
                )
                requirementsHeight = containerFrameLayout.measuredHeight
            }

        if (elementsAreCollapsed) {
            updateContainerViewHeight()
            rotateRowIndicator()
            callOnCollapse(elementsAreCollapsed)
        }

        updateFooterText(elementsAreCollapsed)

        setOnClickListener { collapseElements(requirementsHeight) }
    }

    fun seeArrowIndicator(isVisible: Boolean) {
        footerIndicatorImageView.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    fun getContent(): View? {
        return if (containerFrameLayout.childCount > ZERO) {
            containerFrameLayout.getChildAt(ZERO)
        } else {
            null
        }
    }

    fun setTitle(sectionTitle: String?) {
        sectionTitleTextView.text = sectionTitle
    }

    fun getTitle(): String? {
        return sectionTitleTextView.text?.toString()
    }

    fun setSubtitle(sectionSubtitle: String?) {
        sectionSubtitleTextView.text = sectionSubtitle
    }

    fun getSubtitle(): String? {
        return sectionSubtitleTextView.text?.toString()
    }

    fun setHiddenFooterText(hiddenFooterText: String?) {
        textHiddenState = hiddenFooterText
        updateFooterText(elementsAreCollapsed)
    }

    fun getHiddenFooterText(): String? {
        return textHiddenState
    }

    fun setShowFooterText(showFooterText: String?) {
        textShowState = showFooterText
        updateFooterText(elementsAreCollapsed)
    }

    fun getShowFooterText(): String? {
        return textShowState
    }

    fun setFooterTextColor(footerTextColor: Int?) {
        if (footerTextColor != null && footerTextColor != NO_ID) {
            footerTextTextView.setTextColor(footerTextColor)
        }
    }

    fun setOnCollapseListener(collapseListener: OnCollapseListener?) {
        this.collapseListener = collapseListener
    }

    fun startCollapsed() {
        elementsAreCollapsed = true
    }

    fun collapse() {
        if (!elementsAreCollapsed) {
            collapseElements(requirementsHeight)
        }
    }

    interface OnCollapseListener {

        fun onCollapse(isCollapsed: Boolean)
    }
}
