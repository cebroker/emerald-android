package co.condorlabs.customcomponents.test

import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import co.condorlabs.customcomponents.customcollapsibleview.CollapsibleView
import co.condorlabs.customcomponents.test.util.isTextDisplayed
import co.condorlabs.customcomponents.test.util.isTextNotDisplayed
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Alexis Duque on 2019-06-04.
 * @company Condor Labs
 * @email eduque@condorlabs.io
 */
@RunWith(AndroidJUnit4::class)
class CollapsibleViewTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_custom_collapsible_view_test
        restartActivity()
    }

    @SmallTest
    @Test
    fun shouldShowCollapsibleTag() {
        // Given
        val collapsibleViewTest: CollapsibleView? = ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // Then
        isTextDisplayed("Collapsible tag")
        Assert.assertEquals("Collapsible tag", collapsibleViewTest?.getSectionTag())
    }

    @SmallTest
    @Test
    fun shouldShowCollapsibleTitle() {
        // Given
        val collapsibleViewTest: CollapsibleView? = ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // Then
        isTextDisplayed("Collapsible title")
        Assert.assertEquals("Collapsible title", collapsibleViewTest?.getSectionTitle())
    }

    @SmallTest
    @Test
    fun shouldShowCollapsibleSubtitle() {
        // Given
        val collapsibleViewTest: CollapsibleView? = ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // Then
        isTextDisplayed("Collapsible subtitle")
        Assert.assertEquals("Collapsible subtitle", collapsibleViewTest?.getSectionSubtitle())
    }

    @SmallTest
    @Test
    fun shouldBeCollapsedWhenICallCollapsibleFunction() {
        // given
        val collapsibleViewTest = ruleActivity.activity.findViewById<CollapsibleView>(R.id.collapsibleViewTest)

        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.collapse()
        }

        // then
        isTextNotDisplayed("SIRVE")
    }
}