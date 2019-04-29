package co.condorlabs.customcomponents.test

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.widget.TextView
import co.condorlabs.customcomponents.fileselectorview.FileSelectorClickListener
import co.condorlabs.customcomponents.fileselectorview.FileSelectorOption
import co.condorlabs.customcomponents.fileselectorview.FileSelectorField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.test.util.clickWithText
import co.condorlabs.customcomponents.test.util.isTextDisplayed
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FileSelectorFieldTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_file_selector_view_test
    }

    @Test
    fun shouldHaveDefaultText() {
        restartActivity()

        // Given
        val titleView = Espresso.onView(withText("Upload Documentation"))
        val addAction = Espresso.onView(withText("TAP TO ADD"))

        // When

        // Then
        titleView.check(matches(isDisplayed()))
        addAction.check(matches(isDisplayed()))
    }

    @Test
    fun shouldOpenFileSelectorDialog() {
        restartActivity()

        // Given
        val view = onView(withId(R.id.ivICon))

        // When
        runBlocking {
            view.perform(click())
        }

        // Then
        isTextDisplayed("Gallery")
        isTextDisplayed("Photo")
    }

    @Test
    fun shouldReturnPhotoAsOptionClicked() {
        restartActivity()

        // Given
        val view = onView(withId(R.id.ivICon))
        val field = ruleActivity.activity.findViewById<FileSelectorField>(R.id.myCustomLayoutSelector)
        var result: FileSelectorOption? = null
        field.setFileSelectorClickListener(object : FileSelectorClickListener {
            override fun onOptionSelected(fileSelectorOption: FileSelectorOption) {
                result = fileSelectorOption
            }
        })

        // When
        runBlocking {
            view.perform(click())
            clickWithText("Photo")
        }

        // Then
        Assert.assertTrue(result is FileSelectorOption.Photo)
    }

    @Test
    fun shouldReturnGalleryAsOptionClicked() {
        restartActivity()

        // Given
        val view = onView(withId(R.id.ivICon))
        val field = ruleActivity.activity.findViewById<FileSelectorField>(R.id.myCustomLayoutSelector)
        var result: FileSelectorOption? = null
        field.setFileSelectorClickListener(object : FileSelectorClickListener {
            override fun onOptionSelected(fileSelectorOption: FileSelectorOption) {
                result = fileSelectorOption
            }
        })

        // When
        runBlocking {
            view.perform(click())
            clickWithText("Gallery")
        }

        // Then
        Assert.assertTrue(result is FileSelectorOption.Gallery)
    }

    @Test
    fun shouldNotBeValid() {
        restartActivity()

        // Given
        val field = ruleActivity.activity.findViewById<FileSelectorField>(R.id.myCustomLayoutSelector)
        field.setIsRequired(true)

        // When
        val result = field.isValid()

        // Then
        Assert.assertEquals(
                ValidationResult(
                        false,
                        ruleActivity.activity.getString(R.string.file_selector_default_error)
                ), result
        )
    }

    @Test
    fun shouldShowError() {
        restartActivity()

        // Given
        val field = ruleActivity.activity.findViewById<FileSelectorField>(R.id.myCustomLayoutSelector)

        // When
        ruleActivity.activity.runOnUiThread {
            field.showError("This error should be displayed")
        }

        // Then
        Espresso.onView(withText("This error should be displayed")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldClearError() {
        restartActivity()

        // Given
        val field = ruleActivity.activity.findViewById<FileSelectorField>(R.id.myCustomLayoutSelector)
        val errorView = ruleActivity.activity.findViewById<TextView>(R.id.tvError)

        // When
        ruleActivity.activity.runOnUiThread {
            field.showError("This error should be displayed")
            field.clearError()
        }

        // Then
        Assert.assertTrue(errorView.visibility == View.GONE)
    }

    @Test
    fun shouldChangeTitle() {
        MockActivity.layout = R.layout.activity_file_selector_title
        restartActivity()

        // Given
        val view = Espresso.onView(withText("This is the Title"))

        // When

        // Then
        view.check(matches(isDisplayed()))
    }
}
