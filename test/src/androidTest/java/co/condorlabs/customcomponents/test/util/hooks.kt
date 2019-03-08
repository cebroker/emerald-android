package co.condorlabs.customcomponents.test.util

import android.support.test.espresso.AmbiguousViewMatcherException
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import junit.framework.Assert

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