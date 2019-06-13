package co.condorlabs.customcomponents.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasTextColor
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.SmallTest
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.customtextview.CustomTextView
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
                withFontSize(16f)
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
                withFontSize(14f)
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
        onView(withId(R.id.tvDefault)).check(matches(hasTextColor(R.color.labelColor)))
        onView(withId(R.id.tvDefault)).check(
            matches(
                withFontSize(14f)
            )
        )
    }

    @SmallTest
    @Test
    fun shouldShowTextTypeSectionTitle() {
        // Given
        MockActivity.layout = R.layout.activity_custom_text_view_section_title

        // When
        restartActivity()

        // Then
        onView(withId(R.id.tvDefault)).check(matches(hasTextColor(R.color.textColor)))
        onView(withId(R.id.tvDefault)).check(
            matches(
                withFontSize(14f)
            )
        )
    }

    @SmallTest
    @Test
    fun shouldShowTextTypeTitleProgrammatically() {
        // Given
        MockActivity.layout = R.layout.activity_custom_text_view_default_text
        restartActivity()
        val formField = ruleActivity.activity.findViewById<CustomTextView>(R.id.tvDefault)

        // When
        ruleActivity.runOnUiThread {
            formField.setCustomTextViewType(TITLE_TYPE)
        }

        // Then
        onView(withId(R.id.tvDefault)).check(matches(hasTextColor(R.color.textColor)))
        onView(withId(R.id.tvDefault)).check(
            matches(
                withFontSize(16f)
            )
        )
    }

    @SmallTest
    @Test
    fun shouldShowTextTypeSubtitleProgrammatically() {
        // Given
        MockActivity.layout = R.layout.activity_custom_text_view_default_text
        restartActivity()
        val formField = ruleActivity.activity.findViewById<CustomTextView>(R.id.tvDefault)

        // When
        ruleActivity.runOnUiThread {
            formField.setCustomTextViewType(SUBTITLE_TYPE)
        }

        // Then
        onView(withId(R.id.tvDefault)).check(matches(hasTextColor(R.color.subtitleColor)))
        onView(withId(R.id.tvDefault)).check(
            matches(
                withFontSize(14f)
            )
        )
    }

    @SmallTest
    @Test
    fun shouldShowTextTypeBodyProgrammatically() {
        // Given
        MockActivity.layout = R.layout.activity_custom_text_view_default_text
        restartActivity()
        val formField = ruleActivity.activity.findViewById<CustomTextView>(R.id.tvDefault)

        // When
        ruleActivity.runOnUiThread {
            formField.setCustomTextViewType(BODY_TYPE)
        }

        // Then
        onView(withId(R.id.tvDefault)).check(matches(hasTextColor(R.color.labelColor)))
        onView(withId(R.id.tvDefault)).check(
            matches(
                withFontSize(14f)
            )
        )
    }

    @SmallTest
    @Test
    fun shouldShowTextTypeSectionTitleProgrammatically() {
        // Given
        MockActivity.layout = R.layout.activity_custom_text_view_default_text
        restartActivity()
        val formField = ruleActivity.activity.findViewById<CustomTextView>(R.id.tvDefault)

        // When
        ruleActivity.runOnUiThread {
            formField.setCustomTextViewType(SECTION_TITLE_TYPE)
        }

        // Then
        onView(withId(R.id.tvDefault)).check(matches(hasTextColor(R.color.textColor)))
        onView(withId(R.id.tvDefault)).check(
            matches(
                withFontSize(14f)
            )
        )
    }
}
