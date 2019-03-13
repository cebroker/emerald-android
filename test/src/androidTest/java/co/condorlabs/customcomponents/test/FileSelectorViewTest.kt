package co.condorlabs.customcomponents.test

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withHint
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import co.condorlabs.customcomponents.test.util.isTextDisplayed
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FileSelectorViewTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_file_selector_view_test
    }

    @Test
    fun shouldHaveDefaultText() {
        restartActivity()

        //Given
        val view = onView(withId(R.id.myCustomLayoutSelector))

        //Then
        isTextDisplayed("Upload Documentation")
        isTextDisplayed("Select a photo or file")
    }

    @Test
    fun shouldOpenFileSelectorDialog() {
        restartActivity()

        //Given
        val view = onView(withId(R.id.myCustomLayoutSelector))

        //When
        view.perform(click())

        //Then
        isTextDisplayed("Options")
        isTextDisplayed("Open cam")
        isTextDisplayed("Select file from device")
    }
}
