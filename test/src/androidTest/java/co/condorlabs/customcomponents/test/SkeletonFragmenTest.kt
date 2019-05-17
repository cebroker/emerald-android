package co.condorlabs.customcomponents.test

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import co.condorlabs.customcomponents.ARGUMENT_SKELETON_TYPE
import co.condorlabs.customcomponents.SkeletonTypeNotSupportedException
import co.condorlabs.customcomponents.skeletonview.SkeletonFactory
import co.condorlabs.customcomponents.skeletonview.SkeletonFragment
import org.junit.Test


/**
 * @author Oscar Gallon on 2019-05-17.
 */

class SkeletonFragmenTest {

    @Test
    fun shouldLoadCompletionSkeleton() {
        //given
        launchFragmentInContainer<SkeletonFragment>(Bundle().apply {
            putString(ARGUMENT_SKELETON_TYPE, "Completion")
        })

        val titleView = Espresso.onView(ViewMatchers.withId(R.id.tvTitle))
        val subtitleView = Espresso.onView(ViewMatchers.withId(R.id.tvTitle))
        val contentView = Espresso.onView(ViewMatchers.withId(R.id.tvContent))
        val contentView1 = Espresso.onView(ViewMatchers.withId(R.id.tvContent1))
        val contentView2 = Espresso.onView(ViewMatchers.withId(R.id.tvContent2))
        val contentView3 = Espresso.onView(ViewMatchers.withId(R.id.tvContent3))
        val contentView4 = Espresso.onView(ViewMatchers.withId(R.id.tvContent4))

        //when

        //then
        titleView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        subtitleView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        contentView.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        contentView1.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        contentView2.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        contentView3.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        contentView4.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test(expected = SkeletonTypeNotSupportedException::class)
    fun shouldThrowExceptionIfSkeletonTypeIsNotSupported() {
        //given
        val factory = SkeletonFactory()

        //when
        factory.getSkeletonTypeFromValue("completion")

        //then

    }
}
