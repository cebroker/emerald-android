package co.condorlabs.customcomponents.test.util

import androidx.test.espresso.AmbiguousViewMatcherException
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import android.view.View
import android.widget.TextView
import co.condorlabs.customcomponents.customedittext.BaseEditTextFormField
import junit.framework.Assert
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import androidx.test.espresso.matcher.BoundedMatcher
import android.view.MotionEvent
import android.graphics.Point
import android.graphics.Rect
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matchers.allOf
import android.widget.NumberPicker
import co.condorlabs.customcomponents.CLICK_DRAWABLE_DESCRIPTION
import co.condorlabs.customcomponents.NUMBER_PICKER_VALUE_SETTER_DESCRIPTION
import co.condorlabs.customcomponents.CLICK_DRAWABLE_DESCRIPTION_APPEND

fun isTextDisplayed(resourceId: Int) {
    var isDisplayed = true
    Espresso.onView(ViewMatchers.withText(resourceId))
        .withFailureHandler { error, _ ->
            isDisplayed = error is AmbiguousViewMatcherException
        }
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    Assert.assertTrue(isDisplayed)
}

fun isTextDisplayed(text: String?) {
    var isDisplayed = true
    Espresso.onView(ViewMatchers.withSubstring(text))
        .withFailureHandler { error, _ ->
            isDisplayed = error is AmbiguousViewMatcherException
        }
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    Assert.assertTrue(isDisplayed)
}

fun clickWithId(id: Int) {
    Espresso.onView(ViewMatchers.withId(id))
        .perform(ViewActions.click())
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
            description.appendText("isTextInLines")
        }
    }
}

fun BaseEditTextFormField.text(): String {
    return this.editText?.text.toString()
}

fun clickDrawable(): ViewAction {
    return object: ViewAction {
        override fun getDescription(): String {
            return CLICK_DRAWABLE_DESCRIPTION
        }

        override fun getConstraints(): Matcher<View> {
            return allOf(
                isAssignableFrom(TextView::class.java),
                object : BoundedMatcher<View, TextView>(TextView::class.java) {
                    override fun matchesSafely(tv: TextView): Boolean {
                        if (tv.requestFocusFromTouch())
                            for (d in tv.compoundDrawables)
                                if (d != null)
                                    return true

                        return false
                    }

                    override fun describeTo(description: Description) {
                        description.appendText(CLICK_DRAWABLE_DESCRIPTION_APPEND)
                    }
                })
        }

        override fun perform(uiController: UiController?, view: View?) {
            val tv = view as TextView
            if (tv != null && tv.requestFocusFromTouch())
            {
                val drawables = tv.compoundDrawables

                val tvLocation = Rect()
                tv.getHitRect(tvLocation)

                val tvBounds = arrayOfNulls<Point>(4)
                tvBounds[0] = Point(tvLocation.left, tvLocation.centerY())
                tvBounds[1] = Point(tvLocation.centerX(), tvLocation.top)
                tvBounds[2] = Point(tvLocation.right, tvLocation.centerY())
                tvBounds[3] = Point(tvLocation.centerX(), tvLocation.bottom)

                for (location in 0..3)
                    if (drawables[location] != null) {
                        val bounds = drawables[location].bounds
                        tvBounds[location]?.offset(
                            bounds.width() / 2,
                            bounds.height() / 2
                        )
                        if (tv.dispatchTouchEvent(
                                MotionEvent.obtain(
                                    android.os.SystemClock.uptimeMillis(),
                                    android.os.SystemClock.uptimeMillis(),
                                    MotionEvent.ACTION_DOWN,
                                    tvBounds[location]?.x?.toFloat() ?: 0.0F,
                                    tvBounds[location]?.y?.toFloat() ?: 0.0F,
                                    0
                                )
                            )
                        )
                            tv.dispatchTouchEvent(
                                MotionEvent.obtain(
                                    android.os.SystemClock.uptimeMillis(),
                                    android.os.SystemClock.uptimeMillis(),
                                    MotionEvent.ACTION_UP,
                                    tvBounds[location]?.x?.toFloat() ?: 0.0F,
                                    tvBounds[location]?.y?.toFloat() ?: 0.0F,
                                    0
                                )
                            )
                    }
            }
        }
    }
}

fun setNumberPickerValue(value: Int): ViewAction {
    return object: ViewAction {
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
