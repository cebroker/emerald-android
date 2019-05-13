package co.condorlabs.customcomponents.test

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.loadingfragment.LoadingFragment
import co.condorlabs.customcomponents.loadingfragment.LoadingItem
import co.condorlabs.customcomponents.loadingfragment.LoadingViewHolder
import co.condorlabs.customcomponents.loadingfragment.Status
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Oscar Gallon on 2019-05-10.
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class LoadingFragmentTest {

    @LargeTest
    @Test
    fun shouldInitWithElementsAndTitle() {
        //given
        val scenario = launchFragmentInContainer<LoadingFragment>(Bundle().apply {
            putString(ARGUMENT_TITLE, "Some title")
            putParcelableArrayList(
                ARGUMENT_LOADING_ITEM_LIST, arrayListOf(
                    LoadingItem("Element 1"),
                    LoadingItem("Element 2"),
                    LoadingItem("Element 3"),
                    LoadingItem("Element 4")
                )
            )
            putString(ARGUMENT_SUCCESS_TITLE, "Done!")
            putString(ARGUMENT_ERROR_TITLE, "Failing")
            putString(ARGUMENT_SUCCESS_MESSAGE, "You have submitted everything thank you")
            putString(ARGUMENT_ERROR_MESSAGE, "Error")
        })

        val view = Espresso.onView(ViewMatchers.withText("Some title"))
        val element1 = Espresso.onView(ViewMatchers.withText("Element 1"))
        val element2 = Espresso.onView(ViewMatchers.withText("Element 2"))
        val element3 = Espresso.onView(ViewMatchers.withText("Element 3"))
        val element4 = Espresso.onView(ViewMatchers.withText("Element 4"))

        //when

        //then
        view.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        element1.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        element2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        element3.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        element4.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @LargeTest
    @Test
    fun shouldInitForSuccess() {

        val scenario = launchFragmentInContainer<LoadingFragment>(Bundle().apply {
            putString(ARGUMENT_TITLE, "Some title")
            putParcelableArrayList(
                ARGUMENT_LOADING_ITEM_LIST, arrayListOf(
                    LoadingItem("Element 1"),
                    LoadingItem("Element 2"),
                    LoadingItem("Element 3"),
                    LoadingItem("Element 4")
                )
            )
            putString(ARGUMENT_SUCCESS_TITLE, "Done!")
            putString(ARGUMENT_ERROR_TITLE, "Failing")
            putString(ARGUMENT_SUCCESS_MESSAGE, "You have submitted everything thank you")
            putString(ARGUMENT_ERROR_MESSAGE, "Error")
        })

        val view = Espresso.onView(ViewMatchers.withText("Some title"))
        val element1 = Espresso.onView(ViewMatchers.withText("Element 1"))
        val element2 = Espresso.onView(ViewMatchers.withText("Element 2"))
        val element3 = Espresso.onView(ViewMatchers.withText("Element 3"))
        val element4 = Espresso.onView(ViewMatchers.withText("Element 4"))
        val successTitle = Espresso.onView(ViewMatchers.withText("Done!"))
        val successMessage = Espresso.onView(ViewMatchers.withText("You have submitted everything thank you"))
        val actionButton = Espresso.onView(ViewMatchers.withText("Continue"))

        //when
        scenario.onFragment {
            runBlocking {
                it.showSuccessStatus("Continue")
            }
        }

        //then
        view.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        element1.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        element2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        element3.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        element4.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        successTitle.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        successMessage.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        actionButton.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    @LargeTest
    @Test
    fun shouldInitForError() {

        //Given
        val scenario = launchFragmentInContainer<LoadingFragment>(Bundle().apply {
            putString(ARGUMENT_TITLE, "Some title")
            putParcelableArrayList(
                ARGUMENT_LOADING_ITEM_LIST, arrayListOf(
                    LoadingItem("Element 1"),
                    LoadingItem("Element 2"),
                    LoadingItem("Element 3"),
                    LoadingItem("Element 4")
                )
            )
            putString(ARGUMENT_SUCCESS_TITLE, "Done!")
            putString(ARGUMENT_ERROR_TITLE, "Ups!")
            putString(ARGUMENT_SUCCESS_MESSAGE, "You have submitted everything thank you")
            putString(ARGUMENT_ERROR_MESSAGE, "Something went wrong")
        })

        val view = Espresso.onView(ViewMatchers.withText("Some title"))
        val element1 = Espresso.onView(ViewMatchers.withText("Element 1"))
        val element2 = Espresso.onView(ViewMatchers.withText("Element 2"))
        val element3 = Espresso.onView(ViewMatchers.withText("Element 3"))
        val element4 = Espresso.onView(ViewMatchers.withText("Element 4"))
        val errorTitle = Espresso.onView(ViewMatchers.withText("Ups!"))
        val errorMessage = Espresso.onView(ViewMatchers.withText("Something went wrong"))
        val actionButton = Espresso.onView(ViewMatchers.withText("Try again"))

        //When
        scenario.onFragment {
            runBlocking {
                it.showErrorStatus("Try again")
            }
        }

        //Then
        view.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        element1.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        element2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        element3.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        element4.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        errorTitle.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        errorMessage.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        actionButton.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @LargeTest
    @Test
    fun shouldCallMyActionOnButtonPressedSuccessScenario() {
        //Given
        val scenario = launchFragmentInContainer<LoadingFragment>(Bundle().apply {
            putString(ARGUMENT_TITLE, "Some title")
            putParcelableArrayList(
                ARGUMENT_LOADING_ITEM_LIST, arrayListOf(
                    LoadingItem("Element 1"),
                    LoadingItem("Element 2"),
                    LoadingItem("Element 3"),
                    LoadingItem("Element 4")
                )
            )
            putString(ARGUMENT_SUCCESS_TITLE, "Done!")
            putString(ARGUMENT_ERROR_TITLE, "Ups!")
            putString(ARGUMENT_SUCCESS_MESSAGE, "You have submitted everything thank you")
            putString(ARGUMENT_ERROR_MESSAGE, "Something went wrong")
        })
        var count = 0

        //When
        scenario.onFragment {
            runBlocking {
                it.showSuccessStatus("Continue 2") {
                    count++
                }
            }
        }

        Espresso.onView(ViewMatchers.withText("Continue 2")).perform(ViewActions.click())

        //Then
        Assert.assertEquals(1, count)
    }

    @LargeTest
    @Test
    fun shouldCallMyActionOnButtonPressedErrorScenario() {
        //Given
        val scenario = launchFragmentInContainer<LoadingFragment>(Bundle().apply {
            putString(ARGUMENT_TITLE, "Some title")
            putParcelableArrayList(
                ARGUMENT_LOADING_ITEM_LIST, arrayListOf(
                    LoadingItem("Element 1"),
                    LoadingItem("Element 2"),
                    LoadingItem("Element 3"),
                    LoadingItem("Element 4")
                )
            )
            putString(ARGUMENT_SUCCESS_TITLE, "Done!")
            putString(ARGUMENT_ERROR_TITLE, "Ups!")
            putString(ARGUMENT_SUCCESS_MESSAGE, "You have submitted everything thank you")
            putString(ARGUMENT_ERROR_MESSAGE, "Something went wrong")
        })
        var count = 1

        //When
        scenario.onFragment {
            runBlocking {
                it.showSuccessStatus("Try") {
                    count++
                }
            }
        }
        Espresso.onView(ViewMatchers.withText("Try")).perform(ViewActions.click())


        //Then
        Assert.assertEquals(2, count)
    }

    @LargeTest
    @Test
    fun shouldMarkOneElementAsCompleted() {
        //Given
        val scenario = launchFragmentInContainer<LoadingFragment>(Bundle().apply {
            putString(ARGUMENT_TITLE, "Some title")
            putParcelableArrayList(
                ARGUMENT_LOADING_ITEM_LIST, arrayListOf(
                    LoadingItem("Element 1"),
                    LoadingItem("Element 2"),
                    LoadingItem("Element 3"),
                    LoadingItem("Element 4")
                )
            )
            putString(ARGUMENT_SUCCESS_TITLE, "Done!")
            putString(ARGUMENT_ERROR_TITLE, "Ups!")
            putString(ARGUMENT_SUCCESS_MESSAGE, "You have submitted everything thank you")
            putString(ARGUMENT_ERROR_MESSAGE, "Something went wrong")
        })

        //when
        scenario.onFragment {
            runBlocking {
                it.updateItemsTilPosition(1, status = Status.Loaded)
                delay(1 * DEFAULT_TIME_BETWEEN_OBJECT_ANIMATION + 1)
            }

            val recyclerView = it.view!!.findViewById<RecyclerView>(R.id.rvItems)
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
    }

    @LargeTest
    @Test
    fun shouldMarkTwoElementAsCompleted() {
        //Given
        val scenario = launchFragmentInContainer<LoadingFragment>(Bundle().apply {
            putString(ARGUMENT_TITLE, "Some title")
            putParcelableArrayList(
                ARGUMENT_LOADING_ITEM_LIST, arrayListOf(
                    LoadingItem("Element 1"),
                    LoadingItem("Element 2"),
                    LoadingItem("Element 3"),
                    LoadingItem("Element 4")
                )
            )
            putString(ARGUMENT_SUCCESS_TITLE, "Done!")
            putString(ARGUMENT_ERROR_TITLE, "Ups!")
            putString(ARGUMENT_SUCCESS_MESSAGE, "You have submitted everything thank you")
            putString(ARGUMENT_ERROR_MESSAGE, "Something went wrong")
        })

        //when
        scenario.onFragment {
            runBlocking {
                it.updateItemsTilPosition(2, status = Status.Loaded)
                delay(2 * DEFAULT_TIME_BETWEEN_OBJECT_ANIMATION + 1)
            }

            val recyclerView = it.view!!.findViewById<RecyclerView>(R.id.rvItems)
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
    }

    @LargeTest
    @Test
    fun shouldMarkThreeElementAsCompleted() {
        //Given
        val scenario = launchFragmentInContainer<LoadingFragment>(Bundle().apply {
            putString(ARGUMENT_TITLE, "Some title")
            putParcelableArrayList(
                ARGUMENT_LOADING_ITEM_LIST, arrayListOf(
                    LoadingItem("Element 1"),
                    LoadingItem("Element 2"),
                    LoadingItem("Element 3"),
                    LoadingItem("Element 4")
                )
            )
            putString(ARGUMENT_SUCCESS_TITLE, "Done!")
            putString(ARGUMENT_ERROR_TITLE, "Ups!")
            putString(ARGUMENT_SUCCESS_MESSAGE, "You have submitted everything thank you")
            putString(ARGUMENT_ERROR_MESSAGE, "Something went wrong")
        })

        //when
        scenario.onFragment {
            runBlocking {
                it.updateItemsTilPosition(3, status = Status.Loaded)
                delay(3 * DEFAULT_TIME_BETWEEN_OBJECT_ANIMATION + 1)
            }

            val recyclerView = it.view!!.findViewById<RecyclerView>(R.id.rvItems)
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
    }

    @LargeTest
    @Test
    fun shouldMarkFourElementAsCompleted() {
        //Given
        val scenario = launchFragmentInContainer<LoadingFragment>(Bundle().apply {
            putString(ARGUMENT_TITLE, "Some title")
            putParcelableArrayList(
                ARGUMENT_LOADING_ITEM_LIST, arrayListOf(
                    LoadingItem("Element 1"),
                    LoadingItem("Element 2"),
                    LoadingItem("Element 3"),
                    LoadingItem("Element 4")
                )
            )
            putString(ARGUMENT_SUCCESS_TITLE, "Done!")
            putString(ARGUMENT_ERROR_TITLE, "Ups!")
            putString(ARGUMENT_SUCCESS_MESSAGE, "You have submitted everything thank you")
            putString(ARGUMENT_ERROR_MESSAGE, "Something went wrong")
        })

        //when
        scenario.onFragment {
            runBlocking {
                it.updateItemsTilPosition(4, status = Status.Loaded)
                delay(4 * DEFAULT_TIME_BETWEEN_OBJECT_ANIMATION + 1)
            }

            val recyclerView = it.view!!.findViewById<RecyclerView>(R.id.rvItems)
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
    }

    @LargeTest
    @Test
    fun shouldShowErrorOnItems() {
        //Given
        val scenario = launchFragmentInContainer<LoadingFragment>(Bundle().apply {
            putString(ARGUMENT_TITLE, "Some title")
            putParcelableArrayList(
                ARGUMENT_LOADING_ITEM_LIST, arrayListOf(
                    LoadingItem("Element 1"),
                    LoadingItem("Element 2"),
                    LoadingItem("Element 3"),
                    LoadingItem("Element 4")
                )
            )
            putString(ARGUMENT_SUCCESS_TITLE, "Done!")
            putString(ARGUMENT_ERROR_TITLE, "Ups!")
            putString(ARGUMENT_SUCCESS_MESSAGE, "You have submitted everything thank you")
            putString(ARGUMENT_ERROR_MESSAGE, "Something went wrong")
        })

        //when
        scenario.onFragment {
            runBlocking {
                it.updateItemsTilPosition(4, status = Status.Error)
                delay(4 * ERROR_TIME_BETWEEN_OBJECT_ANIMATION + 1)
            }

            val recyclerView = it.view!!.findViewById<RecyclerView>(R.id.rvItems)

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
    }
}
