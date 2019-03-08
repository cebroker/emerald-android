package co.condorlabs.customcomponents.test


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class eye {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MockActivity::class.java)

    @Test
    fun eye() {
        val checkableImageButton = onView(
            allOf(
                withId(R.id.text_input_password_toggle), withContentDescription("Show password"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tlPassword),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        checkableImageButton.perform(click())

        val editText = onView(
            allOf(
                withId(R.id.etPassword),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tlPassword),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        editText.perform(replaceText("1234"), closeSoftKeyboard())

        val editText2 = onView(
            allOf(
                withId(R.id.etPassword), withText("1234"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tlPassword),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        editText2.perform(click())

        val editText3 = onView(
            allOf(
                withId(R.id.etPassword), withText("1234"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tlPassword),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        editText3.perform(replaceText("123412"))

        val editText4 = onView(
            allOf(
                withId(R.id.etPassword), withText("123412"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tlPassword),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        editText4.perform(closeSoftKeyboard())

        val checkableImageButton2 = onView(
            allOf(
                withId(R.id.text_input_password_toggle), withContentDescription("Show password"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tlPassword),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        checkableImageButton2.perform(click())

        val checkableImageButton3 = onView(
            allOf(
                withId(R.id.text_input_password_toggle), withContentDescription("Show password"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tlPassword),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        checkableImageButton3.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
