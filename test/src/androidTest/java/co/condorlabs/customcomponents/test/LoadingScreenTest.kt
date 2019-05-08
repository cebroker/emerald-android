package co.condorlabs.customcomponents.test

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.loadingfragment.LoadingFragment
import co.condorlabs.customcomponents.loadingfragment.LoadingViewHolder
import co.condorlabs.customcomponents.loadingfragment.Status
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
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

    @Test
    fun shouldCallMyActionOnButtonPressedSuccessScenario() {
        //Given
        val intent = Intent()
        intent.putExtra(ARGUMENT_LOADING_ACTIVITY_TEST_INIT_OPTION, INIT_FOR_SUCCESS_WITH_ACTION)
        ruleActivity.launchActivity(intent)

        Thread.sleep(3000)
        val fragment = ruleActivity.activity.supportFragmentManager.fragments[0] as LoadingFragment
        var count = 0

        ruleActivity.runOnUiThread {
            runBlocking {
                fragment.showSuccessStatus("Continue 2") {
                    count++
                }
            }
        }

        //When
        Espresso.onView(withText("Continue 2")).perform(click())


        //Then
        Assert.assertEquals(1, count)
    }

    @Test
    fun shouldCallMyActionOnButtonPressedErrorScenario() {
        //Given
        val intent = Intent()
        intent.putExtra(ARGUMENT_LOADING_ACTIVITY_TEST_INIT_OPTION, INIT_FOR_ERROR)
        ruleActivity.launchActivity(intent)

        Thread.sleep(3000)
        val fragment = ruleActivity.activity.supportFragmentManager.fragments[0] as LoadingFragment
        var count = 1

        ruleActivity.runOnUiThread {
            runBlocking {
                fragment.showErrorStatus("Try") {
                    count++
                }
            }
        }

        //When
        Espresso.onView(withText("Try")).perform(click())


        //Then
        Assert.assertEquals(2, count)
    }

    @Test
    fun shouldMarkOneElementAsCompleted() {
        //Given
        val intent = Intent()
        intent.putExtra(ARGUMENT_LOADING_ACTIVITY_TEST_INIT_OPTION, INIT_WITH_ELEMENTS)
        ruleActivity.launchActivity(intent)

        Thread.sleep(3000)
        val fragment = ruleActivity.activity.supportFragmentManager.fragments[0] as LoadingFragment
        val recyclerView = fragment.view!!.findViewById<RecyclerView>(R.id.rvItems)!!

        //When
        ruleActivity.runOnUiThread {
            runBlocking {
                fragment.updateItemsTilPosition(1, status = Status.Loaded)
                delay(1 * DEFAULT_TIME_BETWEEN_OBJECT_ANIMATION + 1)
            }
        }

        //Then
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(0) as LoadingViewHolder).getStatus(),
            Status.Loaded
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(1) as LoadingViewHolder).getStatus(),
            Status.Pending
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(2) as LoadingViewHolder).getStatus(),
            Status.Pending
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(3) as LoadingViewHolder).getStatus(),
            Status.Pending
        )
    }

    @Test
    fun shouldMarkTwoElementAsCompleted() {
        //Given
        val intent = Intent()
        intent.putExtra(ARGUMENT_LOADING_ACTIVITY_TEST_INIT_OPTION, INIT_WITH_ELEMENTS)
        ruleActivity.launchActivity(intent)

        Thread.sleep(3000)
        val fragment = ruleActivity.activity.supportFragmentManager.fragments[0] as LoadingFragment
        val recyclerView = fragment.view!!.findViewById<RecyclerView>(R.id.rvItems)!!

        //When
        ruleActivity.runOnUiThread {
            runBlocking {
                fragment.updateItemsTilPosition(2, status = Status.Loaded)
                delay(2 * DEFAULT_TIME_BETWEEN_OBJECT_ANIMATION + 1)
            }

        }

        //Then
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(0) as LoadingViewHolder).getStatus(),
            Status.Loaded
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(1) as LoadingViewHolder).getStatus(),
            Status.Loaded
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(2) as LoadingViewHolder).getStatus(),
            Status.Pending
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(3) as LoadingViewHolder).getStatus(),
            Status.Pending
        )
    }

    @Test
    fun shouldMarkThreeElementAsCompleted() {
        //Given
        val intent = Intent()
        intent.putExtra(ARGUMENT_LOADING_ACTIVITY_TEST_INIT_OPTION, INIT_WITH_ELEMENTS)
        ruleActivity.launchActivity(intent)

        Thread.sleep(3000)
        val fragment = ruleActivity.activity.supportFragmentManager.fragments[0] as LoadingFragment
        val recyclerView = fragment.view!!.findViewById<RecyclerView>(R.id.rvItems)!!

        //When
        ruleActivity.runOnUiThread {
            runBlocking {
                fragment.updateItemsTilPosition(3, status = Status.Loaded)
                delay(3 * DEFAULT_TIME_BETWEEN_OBJECT_ANIMATION + 1)
            }
        }

        //Then
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(0) as LoadingViewHolder).getStatus(),
            Status.Loaded
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(1) as LoadingViewHolder).getStatus(),
            Status.Loaded
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(2) as LoadingViewHolder).getStatus(),
            Status.Loaded
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(3) as LoadingViewHolder).getStatus(),
            Status.Pending
        )
    }

    @Test
    fun shouldMarkFourElementAsCompleted() {
        //Given
        val intent = Intent()
        intent.putExtra(ARGUMENT_LOADING_ACTIVITY_TEST_INIT_OPTION, INIT_WITH_ELEMENTS)
        ruleActivity.launchActivity(intent)

        Thread.sleep(3000)
        val fragment = ruleActivity.activity.supportFragmentManager.fragments[0] as LoadingFragment
        val recyclerView = fragment.view!!.findViewById<RecyclerView>(R.id.rvItems)!!

        //When
        ruleActivity.runOnUiThread {
            runBlocking {
                fragment.updateItemsTilPosition(4, Status.Loaded)
                delay(4 * DEFAULT_TIME_BETWEEN_OBJECT_ANIMATION + 1)
            }
        }

        //Then
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(0) as LoadingViewHolder).getStatus(),
            Status.Loaded
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(1) as LoadingViewHolder).getStatus(),
            Status.Loaded
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(2) as LoadingViewHolder).getStatus(),
            Status.Loaded
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(3) as LoadingViewHolder).getStatus(),
            Status.Loaded
        )
    }

    @Test
    fun shouldShowErrorOnItems() {
        //Given
        val intent = Intent()
        intent.putExtra(ARGUMENT_LOADING_ACTIVITY_TEST_INIT_OPTION, INIT_WITH_ELEMENTS)
        ruleActivity.launchActivity(intent)

        Thread.sleep(3000)
        val fragment = ruleActivity.activity.supportFragmentManager.fragments[0] as LoadingFragment
        val recyclerView = fragment.view!!.findViewById<RecyclerView>(R.id.rvItems)!!

        //When
        ruleActivity.runOnUiThread {
            runBlocking {
                fragment.updateItemsTilPosition(4, Status.Error, ERROR_TIME_BETWEEN_OBJECT_ANIMATION)
                delay(4 * ERROR_TIME_BETWEEN_OBJECT_ANIMATION + 1)
            }
        }

        //Then
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(0) as LoadingViewHolder).getStatus(),
            Status.Error
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(1) as LoadingViewHolder).getStatus(),
            Status.Error
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(2) as LoadingViewHolder).getStatus(),
            Status.Error
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(3) as LoadingViewHolder).getStatus(),
            Status.Error
        )
    }

    @Test
    fun shouldShowErrorOnItemsAndErrorScreen() {
        //Given
        val intent = Intent()
        intent.putExtra(ARGUMENT_LOADING_ACTIVITY_TEST_INIT_OPTION, INIT_FOR_ERROR)
        ruleActivity.launchActivity(intent)

        val errorTitle = Espresso.onView(withText("Ups!"))
        val errorMessage = Espresso.onView(withText("Something went wrong"))
        val actionButton = Espresso.onView(withText("Try again"))

        Thread.sleep(3000)
        val fragment = ruleActivity.activity.supportFragmentManager.fragments[0] as LoadingFragment
        val recyclerView = fragment.view!!.findViewById<RecyclerView>(R.id.rvItems)!!

        //When
        ruleActivity.runOnUiThread {
            runBlocking {
                fragment.updateItemsTilPosition(4, Status.Error, ERROR_TIME_BETWEEN_OBJECT_ANIMATION)
                delay(4 * DEFAULT_TIME_BETWEEN_OBJECT_ANIMATION + 1)
                fragment.showErrorStatus("Try again") {

                }
            }
        }

        //Then
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(0) as LoadingViewHolder).getStatus(),
            Status.Error
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(1) as LoadingViewHolder).getStatus(),
            Status.Error
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(2) as LoadingViewHolder).getStatus(),
            Status.Error
        )
        Assert.assertEquals(
            (recyclerView.findViewHolderForAdapterPosition(3) as LoadingViewHolder).getStatus(),
            Status.Error
        )
        errorTitle.check(matches(isDisplayed()))
        errorMessage.check(matches(isDisplayed()))
        actionButton.check(matches(isDisplayed()))

    }
}
