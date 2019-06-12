package co.condorlabs.customcomponents.test

import android.graphics.Typeface
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasTextColor
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.SmallTest
import co.condorlabs.customcomponents.OPEN_SANS_REGULAR
import co.condorlabs.customcomponents.OPEN_SANS_SEMI_BOLD
import co.condorlabs.customcomponents.test.util.fontSizeMatcher
import co.condorlabs.customcomponents.test.util.withFontSize
import org.junit.Test

class CustomTextViewTest : MockActivityTest() {

    @SmallTest
    @Test
    fun shouldShowTextTypeTitle() {
        // Given
        MockActivity.layout = R.layout.activity_custom_text_view_title

        // When
        restartActivity()

        // Then
        onView(withId(R.id.tvTitle)).check(matches(hasTextColor(R.color.textColor)))
        onView(withId(R.id.tvTitle)).check(
            matches(
                fontSizeMatcher(
                    ruleActivity.activity.resources.getDimension(co.condorlabs.customcomponents.R.dimen.default_title_size),
                    Typeface.createFromAsset(ruleActivity.activity.assets, OPEN_SANS_SEMI_BOLD)
                )
            )
        )
    }

    @SmallTest
    @Test
    fun shouldShowTextTypeSubtitle() {
        // Given
        MockActivity.layout = R.layout.activity_custom_text_view_subtitle

        // When
        restartActivity()

        // Then
        onView(withId(R.id.tvSubtitle)).check(matches(hasTextColor(R.color.subtitleColor)))
        onView(withId(R.id.tvSubtitle)).check(
            matches(
                fontSizeMatcher(
                    ruleActivity.activity.resources.getDimension(co.condorlabs.customcomponents.R.dimen.default_text_size),
                    Typeface.createFromAsset(ruleActivity.activity.assets, OPEN_SANS_SEMI_BOLD)
                )
            )
        )
    }

    @SmallTest
    @Test
    fun shouldShowTextTypeDefault() {
        // Given
        MockActivity.layout = R.layout.activity_custom_text_view_default_text

        // When
        restartActivity()

        // Then
        onView(withId(R.id.tvDefault)).check(matches(hasTextColor(R.color.subtitleColor)))
        onView(withId(R.id.tvDefault)).check(
            matches(
                withFontSize(14f)
            )
        )
    }
}
