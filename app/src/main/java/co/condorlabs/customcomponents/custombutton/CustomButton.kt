package co.condorlabs.customcomponents.custombutton

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.Button
import co.condorlabs.customcomponents.*

/**
 * @author Oscar Gallon on 2019-04-26.
 */
class CustomButton(context: Context, attrs: AttributeSet) : Button(context, attrs) {

    private var type: ButtonType = BUTTON_DEFAULT_TYPE
    private val customButtonStyleFactory = CustomButtonStyleFactory()
    private var state: ButtonState = ButtonState.Normal
    private var normalStateText: String? = null
    private var loadingStateBackground: Drawable? = null
    private var normalStateBackground: Drawable? = null

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomButton,
            DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )

        type = typedArray.getString(R.styleable.CustomButton_type)
            ?: context.getString(R.string.default_base_hint)

        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        isAllCaps = false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        renderStyle()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        //makeDraw()
    }

    fun makeDraw() {
        when (state) {
            ButtonState.Loading -> {
                println("felo: loading")
                background = loadingStateBackground
                if (normalStateText == null) {
                    normalStateText = text.toString()
                }
                text = EMPTY
            }
            else -> {
                println("felo: normal")
                background = normalStateBackground
                if (normalStateText != null) {
                    text = normalStateText
                }
            }
        }
    }

    fun setType(type: ButtonType) {
        this.type = type
        setup()
    }

    fun getType(): ButtonType = type

    fun changeState(state: ButtonState) {
        this.state = state
        makeDraw()
    }

    private fun setup() {
        renderStyle()
        setFont()
    }

    override fun invalidateDrawable(drawable: Drawable) {
        super.invalidateDrawable(drawable)
        if (state is ButtonState.Loading) {
            //makeDraw()
        }
    }

    private fun renderStyle() {
        customButtonStyleFactory.getCustomColorFromType(type).apply {
            normalStateBackground = getBackground(context)
            background = normalStateBackground
            loadingStateBackground = getProgressDrawable(context, background).apply {
                callback = this@CustomButton
            }
            setTextColor(getTextColor(context))
        }
    }

    private fun setFont() {
        val font = Typeface.createFromAsset(context.assets, OPEN_SANS_SEMI_BOLD)
        typeface = font
    }
}
