package co.condorlabs.customcomponents.test

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import co.condorlabs.customcomponents.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Oscar Gallon on 2019-05-06.
 */
@RunWith(AndroidJUnit4::class)
class LoadingScreenTest {

    @Rule
    @JvmField
    val ruleActivity =
        ActivityTestRule<LoadingFragmentActivityTest>(LoadingFragmentActivityTest::class.java, true, false)


    @Test
    fun shouldInitWithTitle() {
        //given
        val intent = Intent()
        intent.putExtra(ARGUMENT_LOADING_ACTIVITY_TEST_INIT_OPTION, INIT_WITH_TITLE)
        ruleActivity.launchActivity(intent)
        val view = Espresso.onView(withText("Some title"))

        //when


        //then
        view.check(matches(isDisplayed()))
    }

    @Test
    fun shouldInitWithElementsAndTitle() {
        //given
        val intent = Intent()
        intent.putExtra(ARGUMENT_LOADING_ACTIVITY_TEST_INIT_OPTION, INIT_WITH_ELEMENTS)
        ruleActivity.launchActivity(intent)
        val view = Espresso.onView(withText("Some title"))
        val element1 = Espresso.onView(withText("Element 1"))
        val element2 = Espresso.onView(withText("Element 2"))
        val element3 = Espresso.onView(withText("Element 3"))
        val element4 = Espresso.onView(withText("Element 4"))

        //when

        //then
        view.check(matches(isDisplayed()))
        element1.check(matches(isDisplayed()))
        element2.check(matches(isDisplayed()))
        element3.check(matches(isDisplayed()))
        element4.check(matches(isDisplayed()))
    }

    @Test
    fun shouldInitForSuccess() {
        //given
        val intent = Intent()
        intent.putExtra(ARGUMENT_LOADING_ACTIVITY_TEST_INIT_OPTION, INIT_FOR_SUCCESS)
        ruleActivity.launchActivity(intent)

        Thread.sleep(3000)

        val view = Espresso.onView(withText("Some title"))
        val element1 = Espresso.onView(withText("Element 1"))
        val element2 = Espresso.onView(withText("Element 2"))
        val element3 = Espresso.onView(withText("Element 3"))
        val element4 = Espresso.onView(withText("Element 4"))
        val successTitle = Espresso.onView(withText("Done!"))
        val successMessage = Espresso.onView(withText("You have submitted everything thank you"))
        val actionButton = Espresso.onView(withText("Continue"))

        //when

        //then
        view.check(matches(isDisplayed()))
        element1.check(matches(isDisplayed()))
        element2.check(matches(isDisplayed()))
        element3.check(matches(isDisplayed()))
        element4.check(matches(isDisplayed()))
        successTitle.check(matches(isDisplayed()))
        successMessage.check(matches(isDisplayed()))
        actionButton.check(matches(isDisplayed()))
    }

    @Test
    fun shouldInitForError() {

        //Given
        val intent = Intent()
        intent.putExtra(ARGUMENT_LOADING_ACTIVITY_TEST_INIT_OPTION, INIT_FOR_ERROR)
        ruleActivity.launchActivity(intent)

        Thread.sleep(3000)

        val view = Espresso.onView(withText("Some title"))
        val element1 = Espresso.onView(withText("Element 1"))
        val element2 = Espresso.onView(withText("Element 2"))
        val element3 = Espresso.onView(withText("Element 3"))
        val element4 = Espresso.onView(withText("Element 4"))
        val errorTitle = Espresso.onView(withText("Ups!"))
        val errorMessage = Espresso.onView(withText("Something went wrong"))
        val actionButton = Espresso.onView(withText("Try again"))

        //When

        //Then
        view.check(matches(isDisplayed()))
        element1.check(matches(isDisplayed()))
        element2.check(matches(isDisplayed()))
        element3.check(matches(isDisplayed()))
        element4.check(matches(isDisplayed()))
        errorTitle.check(matches(isDisplayed()))
        errorMessage.check(matches(isDisplayed()))
        actionButton.check(matches(isDisplayed()))
    }
}
