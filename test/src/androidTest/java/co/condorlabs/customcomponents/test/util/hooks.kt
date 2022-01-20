package co.condorlabs.customcomponents.test.util

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.test.espresso.AmbiguousViewMatcherException
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.customedittext.BaseEditTextFormField
import co.condorlabs.customcomponents.customradiogroup.RadioGroupFormField
import co.condorlabs.customcomponents.customspinner.BaseSpinnerFormField
import co.condorlabs.customcomponents.customtextview.CustomTextView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import kotlin.math.roundToInt

fun isTextDisplayed(text: String?) {
    var isDisplayed = true
    Espresso.onView(ViewMatchers.withSubstring(text))
        .withFailureHandler { error, _ ->
            isDisplayed = error is AmbiguousViewMatcherException
        }
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    assertTrue(isDisplayed)
}

fun isSpinnerEnable(): Matcher<View> {
    return object : BoundedMatcher<View, BaseSpinnerFormField>(BaseSpinnerFormField::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("is disable")
        }

        override fun matchesSafely(item: BaseSpinnerFormField): Boolean {
            return item.textInputLayout!!.isEnabled
        }
    }
}

fun isTextNotDisplayed(text: String?) {
    var isDisplayed = false
    Espresso.onView(ViewMatchers.withSubstring(text))
        .withFailureHandler { error, _ ->
            isDisplayed = error is AmbiguousViewMatcherException
        }
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    assertFalse(isDisplayed)
}

fun withTextColor(expectedId: Int): Matcher<View> {
    return object : BoundedMatcher<View, TextView>(TextView::class.java) {

        override fun matchesSafely(textView: TextView): Boolean {
            return expectedId == textView.currentTextColor
        }

        override fun describeTo(description: Description) {
            description.appendText(WITH_TEXT_COLOR_DESCRIPTION)
            description.appendValue(expectedId)
        }
    }
}

fun clickWithText(text: String) {
    Espresso.onView(ViewMatchers.withSubstring(text))
        .perform(ViewActions.click())
}

fun isTextInLines(lines: Int): TypeSafeMatcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun matchesSafely(item: View): Boolean {
            return (item as TextView).lineCount == lines
        }

        override fun describeTo(description: Description) {
            description.appendText(WITH_TEXT_IN_LINES_DESCRIPTION)
            description.appendValue(lines)
        }
    }
}

fun BaseEditTextFormField.text(): String {
    return this.textInputLayout?.editText?.text.toString()
}

fun clickDrawable(): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return CLICK_DRAWABLE_DESCRIPTION
        }

        override fun getConstraints(): Matcher<View> {
            return allOf(
                isAssignableFrom(TextView::class.java),
                object : BoundedMatcher<View, TextView>(TextView::class.java) {
                    override fun matchesSafely(aTextView: TextView): Boolean {
                        if (aTextView.requestFocusFromTouch())
                            for (drawableItem in aTextView.compoundDrawables)
                                if (drawableItem != null)
                                    return true

                        return false
                    }

                    override fun describeTo(description: Description) {
                        description.appendText(CLICK_DRAWABLE_DESCRIPTION_APPEND)
                    }
                })
        }

        override fun perform(uiController: UiController?, view: View?) {
            val aTextView = view as TextView
            if (aTextView != null && aTextView.requestFocusFromTouch()) {
                val drawables = aTextView.compoundDrawables

                val tvLocation = Rect()
                aTextView.getHitRect(tvLocation)

                val textViewBounds = arrayOfNulls<Point>(TEXT_VIEW_BOUNDS_SIDES)
                textViewBounds[0] = Point(tvLocation.left, tvLocation.centerY())
                textViewBounds[1] = Point(tvLocation.centerX(), tvLocation.top)
                textViewBounds[2] = Point(tvLocation.right, tvLocation.centerY())
                textViewBounds[3] = Point(tvLocation.centerX(), tvLocation.bottom)

                for (location in DRAWABLE_START_INDEX..DRAWABLE_END_INDEX)
                    if (drawables[location] != null) {
                        val bounds = drawables[location].bounds
                        textViewBounds[location]?.offset(
                            bounds.width() / 2,
                            bounds.height() / 2
                        )
                        if (aTextView.dispatchTouchEvent(
                                MotionEvent.obtain(
                                    android.os.SystemClock.uptimeMillis(),
                                    android.os.SystemClock.uptimeMillis(),
                                    MotionEvent.ACTION_DOWN,
                                    textViewBounds[location]?.x?.toFloat() ?: ZERO_FLOAT,
                                    textViewBounds[location]?.y?.toFloat() ?: ZERO_FLOAT,
                                    ZERO
                                )
                            )
                        )
                            aTextView.dispatchTouchEvent(
                                MotionEvent.obtain(
                                    android.os.SystemClock.uptimeMillis(),
                                    android.os.SystemClock.uptimeMillis(),
                                    MotionEvent.ACTION_UP,
                                    textViewBounds[location]?.x?.toFloat() ?: ZERO_FLOAT,
                                    textViewBounds[location]?.y?.toFloat() ?: ZERO_FLOAT,
                                    ZERO
                                )
                            )
                    }
            }
        }
    }
}

fun setNumberPickerValue(value: Int): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return NUMBER_PICKER_VALUE_SETTER_DESCRIPTION
        }

        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(NumberPicker::class.java)
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as NumberPicker).value = value
        }
    }
}

fun withFontSize(expectedSize: Float): Matcher<View> {
    return object : BoundedMatcher<View, CustomTextView>(CustomTextView::class.java) {

        public override fun matchesSafely(target: CustomTextView): Boolean {
            val pixels = target.textSize
            val actualSize = pixels / target.resources.displayMetrics.scaledDensity
            return actualSize.compareTo(expectedSize) == 0
        }

        override fun describeTo(description: Description) {
            description.appendText(WITH_FONT_SIZE_DESCRIPTION)
            description.appendValue(expectedSize)
        }
    }
}

fun withTintColorInRadioButtons(expectedColor: Int): Matcher<View> {
    return object : BoundedMatcher<View, RadioGroupFormField>(RadioGroupFormField::class.java) {

        public override fun matchesSafely(formField: RadioGroupFormField): Boolean {
            val viewGroup = (formField as? ViewGroup) ?: return false
            for (index in ZERO until viewGroup.childCount) {
                with(getRadioButtonAtPosition(viewGroup, index)) {
                    val actualColor = buttonTintList.getColorForState(
                        intArrayOf(android.R.attr.state_checked),
                        ZERO
                    )

                    if (expectedColor != actualColor) {
                        return false
                    }
                }
            }
            return true
        }

        override fun describeTo(description: Description) {
            description.appendText(TINT_COLOR_IN_RADIO_BUTTON_DESCRIPTION)
            description.appendValue(expectedColor)
        }
    }
}

fun getRadioButtonAtPosition(
    parentView: ViewGroup,
    position: Int
): RadioButton =
    (parentView.getChildAt(RADIO_GROUP_POSITION) as RadioGroup).getChildAt(position) as RadioButton

fun Activity.getRootView(): View {
    return findViewById(android.R.id.content)
}

fun Context.convertDpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this.resources.displayMetrics
    )
}

fun Activity.isKeyboardOpen(): Boolean {
    val visibleBounds = Rect()
    this.getRootView().getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = getRootView().height - visibleBounds.height()
    val marginOfError = this.convertDpToPx(CALCULATE_KEYBOARD_DISPLAYED_MARGIN_ERROR).roundToInt()
    return heightDiff > marginOfError
}

fun Activity.isKeyboardClosed(): Boolean {
    return !this.isKeyboardOpen()
}
