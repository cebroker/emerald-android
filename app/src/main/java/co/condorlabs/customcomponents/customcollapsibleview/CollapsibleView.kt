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
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
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

    private var contentViewId: Int = NO_ID
    private var contentHeight = ZERO
    private var isContentCollapsed = true
    private var onCollapseListener: OnCollapseListener? = null
    private var hideActionLabel: String? = null
    private var showActionLabel: String? = null
    private var ivCollapsible = AppCompatImageView(context).apply { id = R.id.imageId }
    private var tvTitle = AppCompatTextView(context).apply { id = R.id.titleId }
    private var tvSubtitle = AppCompatTextView(context).apply { id = R.id.subtitleId }
    private var cvContainer = CardView(context).apply { id = R.id.cardId }
    private var clCardViewContainer = ConstraintLayout(context)
    private var vLineSeparator = View(context).apply { id = R.id.lineSeparatorId }
    private var flContent = FrameLayout(context).apply { id = R.id.frameLayoutId }
    private var clAction = ConstraintLayout(context).apply { id = R.id.actionContainerId }
    private var tvActionLabel = AppCompatTextView(context).apply { id = R.id.actionLabelViewId }
    private var ivActionIndicator =
        AppCompatImageView(context).apply { id = R.id.actionImageViewId }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val typedArray =
                context.obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackground))
            val selectableItemBackground = typedArray.getResourceId(ZERO, ZERO)
            typedArray.recycle()

            clCardViewContainer.foreground = context.getDrawable(selectableItemBackground)
        }

        isClickable = true
        isFocusable = true
        setPadding(ZERO, ZERO, ZERO, ZERO)
        addView(cvContainer)

        cvContainer.apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            radius = ZERO_FLOAT
            addView(clCardViewContainer)
        }

        clCardViewContainer.apply {
            addView(ivCollapsible)
            addView(tvTitle)
            addView(tvSubtitle)
            addView(vLineSeparator)
            addView(flContent)
            addView(clAction)
        }

        ivCollapsible.apply {
            layoutParams = LayoutParams(
                DEFAULT_COLLAPSIBLE_IMAGE_WIDTH,
                DEFAULT_COLLAPSIBLE_IMAGE_HEIGHT
            )
            scaleType = ImageView.ScaleType.CENTER
        }

        tvTitle.apply {
            layoutParams = LayoutParams(
                MATCH_CONSTRAINT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            typeface = Typeface.createFromAsset(context.assets, OPEN_SANS_SEMI_BOLD)
            setTextColor(Color.BLACK)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_COLLAPSIBLE_TITLE_TEXT_SIZE)
        }

        tvSubtitle.apply {
            layoutParams = LayoutParams(
                MATCH_CONSTRAINT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        vLineSeparator.apply {
            layoutParams = LayoutParams(
                MATCH_CONSTRAINT,
                COLLAPSIBLE_LINE_SEPARATOR_HEIGHT
            )
            setBackgroundResource(R.color.linear_separator)
        }

        flContent.apply {
            layoutParams = LayoutParams(
                MATCH_CONSTRAINT,
                LayoutParams.WRAP_CONTENT
            )
        }

        clAction.apply {
            layoutParams = LayoutParams(
                MATCH_CONSTRAINT,
                LayoutParams.WRAP_CONTENT
            )
            setPadding(
                DEFAULT_COLLAPSIBLE_PADDING, DEFAULT_COLLAPSIBLE_PADDING,
                DEFAULT_COLLAPSIBLE_PADDING, DEFAULT_COLLAPSIBLE_PADDING
            )
            addView(tvActionLabel)
            addView(ivActionIndicator)
        }

        tvActionLabel.apply {

            layoutParams = LayoutParams(
                MATCH_CONSTRAINT,
                LayoutParams.WRAP_CONTENT
            )
            setTextColor(ContextCompat.getColor(context, R.color.primaryColor))
            typeface = Typeface.createFromAsset(context.assets, OPEN_SANS_SEMI_BOLD)
        }

        ivActionIndicator.apply {
            setImageResource(R.drawable.ic_arrow_down)
            rotation = COLLAPSIBLE_INDICATOR_ROTATION
        }

        ConstraintSet().apply {
            clone(clAction)
            with(tvActionLabel) {
                connect(
                    id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM,
                    DEFAULT_COLLAPSIBLE_MARGIN_ACTION_TEXT
                )
                connect(id, ConstraintSet.END, ivActionIndicator.id, ConstraintSet.START)
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(
                    id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP,
                    DEFAULT_COLLAPSIBLE_MARGIN_ACTION_TEXT
                )
            }
            with(ivActionIndicator) {
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
            applyTo(clAction)
        }

        ConstraintSet().apply {
            clone(clCardViewContainer)
            with(ivCollapsible) {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.TOP, tvTitle.id, ConstraintSet.TOP)
                connect(id, ConstraintSet.BOTTOM, tvTitle.id, ConstraintSet.BOTTOM)
            }
            with(tvTitle) {
                connect(
                    id,
                    ConstraintSet.START,
                    ivCollapsible.id,
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
            with(tvSubtitle) {
                connect(
                    id,
                    ConstraintSet.BOTTOM,
                    vLineSeparator.id,
                    ConstraintSet.TOP,
                    DEFAULT_COLLAPSIBLE_SUBTITLE_MARGIN_BOTTOM
                )
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.START, tvTitle.id, ConstraintSet.START)
                connect(
                    id,
                    ConstraintSet.TOP,
                    tvTitle.id,
                    ConstraintSet.BOTTOM,
                    DEFAULT_COLLAPSIBLE_SUBTITLE_TOP_MARGIN
                )
            }
            with(vLineSeparator) {
                connect(
                    id,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START,
                    COLLAPSIBLE_LINE_SEPARATOR_START_MARGIN
                )
                connect(id, ConstraintSet.BOTTOM, flContent.id, ConstraintSet.TOP)
            }
            with(flContent) {
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.BOTTOM, clAction.id, ConstraintSet.TOP)
            }
            with(clAction) {
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.START, tvTitle.id, ConstraintSet.START)
                connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }
            applyTo(clCardViewContainer)
        }

        ConstraintSet().apply {
            clone(this@CollapsibleView)

            with(cvContainer) {
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

        if (contentViewId != NO_ID) {
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater)
                ?.inflate(contentViewId, null, false)?.let {
                    setContent(it)
                }
        }
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        clCardViewContainer.setPadding(
            getMinimumCollapsibleContentPadding(left),
            getMinimumCollapsibleContentPadding(top),
            getMinimumCollapsibleContentPadding(right),
            getMinimumCollapsibleContentPadding(bottom)
        )
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
            ).toInt()
            val paddingTop =
                typedArray.getDimension(R.styleable.CollapsibleView_android_paddingTop, ZERO_FLOAT)
                    .toInt()
            val paddingEnd =
                typedArray.getDimension(R.styleable.CollapsibleView_android_paddingEnd, ZERO_FLOAT)
                    .toInt()
            val paddingBottom = typedArray.getDimension(
                R.styleable.CollapsibleView_android_paddingBottom,
                ZERO_FLOAT
            ).toInt()

            setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
        } else {
            padding.toInt().let {
                setPadding(it, it, it, it)
            }
        }
        setImage(typedArray.getResourceId(R.styleable.CollapsibleView_image, NO_ID))
        cvContainer.useCompatPadding =
            typedArray.getBoolean(R.styleable.CollapsibleView_useAppCompactPadding, true)
        contentViewId =
            typedArray.getResourceId(R.styleable.CollapsibleView_content, NO_ID)
        isContentCollapsed =
            typedArray.getBoolean(R.styleable.CollapsibleView_isCollapsed, true)
        hideActionLabel =
            typedArray.getString(R.styleable.CollapsibleView_hideActionLabel)
                ?: resources.getString(R.string.hide)
        showActionLabel = typedArray.getString(R.styleable.CollapsibleView_showActionLabel)
            ?: resources.getString(R.string.show)
        setTitle(typedArray.getString(R.styleable.CollapsibleView_collapsibleTitle))
        setSubtitle(typedArray.getString(R.styleable.CollapsibleView_subtitle))
        setActionLabelColor(
            typedArray.getColor(
                R.styleable.CollapsibleView_actionLabelColor,
                NO_ID
            )
        )
        setImageTint(typedArray.getColor(R.styleable.CollapsibleView_imageTintColor, NO_ID))

        setActionIndicatorVisibility(
            if (typedArray.getBoolean(R.styleable.CollapsibleView_visibleIndicatorArrow, false)) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        )

        typedArray.recycle()
    }

    private fun collapseContent(requirementsHeight: Int) {
        if (isContentCollapsed) {
            collapseContent(ZERO, requirementsHeight, false)
        } else {
            collapseContent(requirementsHeight, ZERO, true)
        }

        isContentCollapsed = !isContentCollapsed

        onCollapseListener?.onCollapse(isContentCollapsed)
    }

    private fun collapseContent(from: Int, to: Int, isCollapsed: Boolean) {
        updateActionLabel(isCollapsed)

        val animator =
            ValueAnimator.ofInt(from, to).setDuration(DEFAULT_COLLAPSIBLE_ANIMATION_TIME)?.apply {
                addUpdateListener {
                    updateContentHeight(it.animatedValue as Int)
                    rotateIndicatorArrow(
                        getRotationIndicatorArrowValue(
                            it.animatedValue as Int,
                            if (from > to) {
                                from
                            } else {
                                to
                            }
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

    private fun updateContentHeight(height: Int = ZERO) {
        flContent.apply {
            layoutParams?.height = height
            requestLayout()
        }
    }

    private fun rotateIndicatorArrow(rotation: Float = ZERO_FLOAT) {
        ivActionIndicator.rotation = -rotation
    }

    private fun getRotationIndicatorArrowValue(currentValue: Int, limit: Int): Float {
        return (COLLAPSIBLE_INDICATOR_ROTATION * ((currentValue * ONE_HUNDRED_FLOAT) / limit)) / ONE_HUNDRED_FLOAT
    }

    private fun updateActionLabel(isCollapsed: Boolean) {
        tvActionLabel.text = if (isCollapsed) {
            showActionLabel
        } else {
            hideActionLabel
        }
    }

    private fun getMinimumCollapsibleContentPadding(padding: Int): Int {
        return if (padding < MIN_DEFAULT_PADDING) MIN_DEFAULT_PADDING else padding
    }

    fun isContentCollapsed() = isContentCollapsed

    fun setImage(imageResourceId: Int) {
        if (imageResourceId != NO_ID) {
            val imageDrawableResource = AppCompatResources.getDrawable(context, imageResourceId)
            ivCollapsible.setImageDrawable(imageDrawableResource)
            ivCollapsible.visibility = View.VISIBLE
        } else {
            ivCollapsible.visibility = View.GONE
        }
    }

    fun setImageTint(colorTint: Int) {
        if (colorTint != NO_ID) {
            ImageViewCompat.setImageTintList(
                ivCollapsible,
                AppCompatResources.getColorStateList(context, colorTint)
            )
        }
    }

    fun setContent(collapsibleContent: View) {
        flContent.removeAllViews()
        flContent.addView(collapsibleContent)

        flContent.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (flContent.measuredHeight > contentHeight) {
                        contentHeight = flContent.measuredHeight
                    }

                    if (isContentCollapsed) {
                        updateContentHeight()
                        rotateIndicatorArrow()
                        onCollapseListener?.onCollapse(isContentCollapsed)
                    }

                    updateActionLabel(isContentCollapsed)

                    setOnClickListener { collapseContent(contentHeight) }

                    flContent.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
    }

    fun setActionIndicatorVisibility(visibility: Int) {
        ivActionIndicator.visibility = visibility
    }

    fun getContent(): View? {
        return if (flContent.childCount > ZERO) {
            flContent.getChildAt(ZERO)
        } else {
            null
        }
    }

    fun setTitle(title: String?) {
        tvTitle.text = title
    }

    fun getTitle(): String? {
        return tvTitle.text?.toString()
    }

    fun setSubtitle(subtitle: String?) {
        tvSubtitle.text = subtitle
    }

    fun getSubtitle(): String? {
        return tvSubtitle.text?.toString()
    }

    fun setHideActionLabel(actionLabel: String?) {
        hideActionLabel = actionLabel
        updateActionLabel(isContentCollapsed)
    }

    fun getHideActionLabel(): String? {
        return hideActionLabel
    }

    fun setShowActionLabel(actionLabel: String?) {
        showActionLabel = actionLabel
        updateActionLabel(isContentCollapsed)
    }

    fun getShowActionLabel(): String? {
        return showActionLabel
    }

    fun setActionLabelColor(actionLabelColor: Int?) {
        if (actionLabelColor != null && actionLabelColor != NO_ID) {
            tvActionLabel.setTextColor(actionLabelColor)
        }
    }

    fun setOnCollapseListener(collapseListener: OnCollapseListener?) {
        this.onCollapseListener = collapseListener
    }

    fun startExpanded() {
        isContentCollapsed = false
    }

    fun collapse() {
        collapseContent(contentHeight)
    }
}
