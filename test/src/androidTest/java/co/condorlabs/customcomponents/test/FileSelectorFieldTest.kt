package co.condorlabs.customcomponents.test

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import co.condorlabs.customcomponents.fileselectorview.FileSelectorClickListener
import co.condorlabs.customcomponents.fileselectorview.FileSelectorField
import co.condorlabs.customcomponents.fileselectorview.FileSelectorOption
import co.condorlabs.customcomponents.fileselectorview.FileSelectorValue
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.test.util.clickWithText
import co.condorlabs.customcomponents.test.util.isTextDisplayed
import co.condorlabs.customcomponents.test.util.isTextNotDisplayed
import co.condorlabs.customcomponents.test.util.withDrawable
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.`is`
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

    @SmallTest
    @Test
    fun shouldHaveDefaultText() {
        restartActivity()

        // Given
        val titleView = onView(withText("Upload Documentation"))
        val addAction = onView(withText("TAP TO ADD"))

        // When

        // Then
        titleView.check(matches(isDisplayed()))
        addAction.check(matches(isDisplayed()))
    }

    @SmallTest
    @Test
    fun shouldOpenFileSelectorDialog() {
        restartActivity()

        // Given
        val view = onView(withId(R.id.ivIcon))

        // When
        runBlocking {
            view.perform(click())
        }

        // Then
        isTextDisplayed("Gallery")
        isTextDisplayed("Photo")
    }

    @SmallTest
    @Test
    fun shouldReturnPhotoAsOptionClicked() {
        restartActivity()

        // Given
        val view = onView(withId(R.id.ivIcon))
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

    @SmallTest
    @Test
    fun shouldReturnGalleryAsOptionClicked() {
        restartActivity()

        // Given
        val view = onView(withId(R.id.ivIcon))
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

    @SmallTest
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

    @SmallTest
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
        onView(withText("This error should be displayed")).check(matches(isDisplayed()))
    }

    @SmallTest
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

    @SmallTest
    @Test
    fun shouldChangeTitle() {
        MockActivity.layout = R.layout.activity_file_selector_title
        restartActivity()

        // Given
        val view = onView(withText("This is the Title"))

        // When

        // Then
        view.check(matches(isDisplayed()))
    }

    @SmallTest
    @Test
    fun shouldDisable() {
        MockActivity.layout = R.layout.activity_file_selector_title
        restartActivity()

        // Given
        val fileSelectorField = ruleActivity.activity.findViewById<FileSelectorField>(R.id.myCustomLayoutSelector)
        ruleActivity.activity.runOnUiThread { fileSelectorField.setEnable(false) }

        val view = onView(withId(R.id.myCustomLayoutSelector))

        // When
        runBlocking {
            view.perform(click())
        }

        // Then
        onView(withTagValue(`is`(R.drawable.ic_cloud_upload_file_disabled as Any))).check(matches(isDisplayed()))
        isTextNotDisplayed("Option")
    }

    @SmallTest
    @Test
    fun shouldEnable() {
        MockActivity.layout = R.layout.activity_file_selector_title
        restartActivity()

        // Given
        val fileSelectorField = ruleActivity.activity.findViewById<FileSelectorField>(R.id.myCustomLayoutSelector)
        ruleActivity.activity.runOnUiThread { fileSelectorField.setEnable(false) }

        val view = onView(withId(R.id.myCustomLayoutSelector))

        // When
        runBlocking {
            view.perform(click())
        }
        isTextNotDisplayed("Option")

        ruleActivity.activity.runOnUiThread { fileSelectorField.setEnable(true) }

        // Then
        onView(withTagValue(`is`(R.drawable.ic_cloud_upload_file as Any))).check(matches(isDisplayed()))
        runBlocking {
            view.perform(click())
        }
        isTextDisplayed("Option")
    }

    @SmallTest
    @Test
    fun shouldNotOpenCamera() {
        MockActivity.layout = R.layout.activity_file_selector_camera_test
        restartActivity()

        // Given
        val fileSelectorField = ruleActivity.activity.findViewById<FileSelectorField>(R.id.myCustomLayoutSelector)

        val fileSelectorClickListener = object : FileSelectorClickListener {
            override fun onOptionSelected(fileSelectorOption: FileSelectorOption) {
                Assert.assertFalse(true)
            }
        }

        fileSelectorField.setFileSelectorClickListener(fileSelectorClickListener)

        ruleActivity.activity.runOnUiThread { fileSelectorField.setEnable(false) }

        val view = onView(withId(R.id.myCustomLayoutSelector))

        // When
        runBlocking {
            view.perform(click())
        }
    }

    @SmallTest
    @Test
    fun shouldNotOpenGallery() {
        MockActivity.layout = R.layout.activity_file_selector_gallery_test
        restartActivity()

        // Given
        val fileSelectorField = ruleActivity.activity.findViewById<FileSelectorField>(R.id.myCustomLayoutSelector)

        val fileSelectorClickListener = object : FileSelectorClickListener {
            override fun onOptionSelected(fileSelectorOption: FileSelectorOption) {
                Assert.assertFalse(true)
            }
        }

        fileSelectorField.setFileSelectorClickListener(fileSelectorClickListener)

        ruleActivity.activity.runOnUiThread { fileSelectorField.setEnable(false) }

        val view = onView(withId(R.id.myCustomLayoutSelector))

        // When
        runBlocking {
            view.perform(click())
        }
    }

    @SmallTest
    @Test
    fun shouldShowFileOption() {
        MockActivity.layout = R.layout.activity_file_selector_file_test
        restartActivity()

        // Given
        val view = onView(withId(R.id.ivIcon))
        val field = ruleActivity.activity.findViewById<FileSelectorField>(R.id.fileSelectorOptionFile)
        var result: FileSelectorOption? = null
        field.setFileSelectorClickListener(object : FileSelectorClickListener {
            override fun onOptionSelected(fileSelectorOption: FileSelectorOption) {
                result = fileSelectorOption
            }
        })

        // When
        runBlocking {
            view.perform(click())
            clickWithText("File")
        }

        Assert.assertTrue(result is FileSelectorOption.File)
    }

    @SmallTest
    @Test
    fun shouldNotOpenFilePicker() {
        MockActivity.layout = R.layout.activity_file_selector_file_test
        restartActivity()

        // Given
        val fileSelectorField = ruleActivity.activity.findViewById<FileSelectorField>(R.id.fileSelectorOptionFile)

        val fileSelectorClickListener = object : FileSelectorClickListener {
            override fun onOptionSelected(fileSelectorOption: FileSelectorOption) {
                Assert.assertFalse(true)
            }
        }

        fileSelectorField.setFileSelectorClickListener(fileSelectorClickListener)

        ruleActivity.activity.runOnUiThread { fileSelectorField.setEnable(false) }

        val view = onView(withId(R.id.fileSelectorOptionFile))

        // When
        runBlocking {
            view.perform(click())
        }
    }

    @SmallTest
    @Test
    fun shouldShowPDFIcon() {
        MockActivity.layout = R.layout.activity_file_selector_file_test
        restartActivity()

        // Given
        val fileSelectorField = ruleActivity.activity.findViewById<FileSelectorField>(R.id.fileSelectorOptionFile)

        // When
        ruleActivity.activity.runOnUiThread {
            fileSelectorField.setFileValue(
                FileSelectorValue.FileValue("file/path/filename.pdf", "filename.pdf")
            )
        }

        // Then
        runBlocking {
            onView(withId(R.id.ivIcon)).check(matches(withDrawable(R.drawable.ic_file_pdf)))
        }
    }

    @SmallTest
    @Test
    fun shouldShowDocIcon() {
        MockActivity.layout = R.layout.activity_file_selector_file_test
        restartActivity()

        // Given
        val fileSelectorField = ruleActivity.activity.findViewById<FileSelectorField>(R.id.fileSelectorOptionFile)

        // When
        ruleActivity.activity.runOnUiThread {
            fileSelectorField.setFileValue(
                FileSelectorValue.FileValue("file/path/filename.doc", "filename.doc")
            )
        }

        // Then
        runBlocking {
            onView(withId(R.id.ivIcon)).check(matches(withDrawable(R.drawable.ic_file_doc)))
        }
    }

    @SmallTest
    @Test
    fun shouldShowDocxIcon() {
        MockActivity.layout = R.layout.activity_file_selector_file_test
        restartActivity()

        // Given
        val fileSelectorField = ruleActivity.activity.findViewById<FileSelectorField>(R.id.fileSelectorOptionFile)

        // When
        ruleActivity.activity.runOnUiThread {
            fileSelectorField.setFileValue(
                FileSelectorValue.FileValue("file/path/filename.docx", "filename.docx")
            )
        }

        // Then
        runBlocking {
            onView(withId(R.id.ivIcon)).check(matches(withDrawable(R.drawable.ic_file_doc)))
        }
    }

    @SmallTest
    @Test
    fun shouldShowUnknownIcon() {
        MockActivity.layout = R.layout.activity_file_selector_file_test
        restartActivity()

        // Given
        val fileSelectorField = ruleActivity.activity.findViewById<FileSelectorField>(R.id.fileSelectorOptionFile)

        // When
        ruleActivity.activity.runOnUiThread {
            fileSelectorField.setFileValue(
                FileSelectorValue.FileValue("file/path/filename.txt", "filename.txt")
            )
        }

        // Then
        runBlocking {
            onView(withId(R.id.ivIcon)).check(matches(withDrawable(R.drawable.ic_file_base)))
        }
    }
}
