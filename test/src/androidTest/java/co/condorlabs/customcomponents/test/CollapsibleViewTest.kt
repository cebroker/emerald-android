package co.condorlabs.customcomponents.test

import android.graphics.Color
import androidx.appcompat.widget.AppCompatButton
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import co.condorlabs.customcomponents.customcollapsibleview.CollapsibleView
import co.condorlabs.customcomponents.test.util.clickWithText
import co.condorlabs.customcomponents.test.util.isTextDisplayed
import co.condorlabs.customcomponents.test.util.isTextNotDisplayed
import co.condorlabs.customcomponents.test.util.withTextColor
import junit.framework.Assert.assertTrue
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Alexis Duque on 2019-06-04.
 * @company Condor Labs
 * @email eduque@condorlabs.io
 */
class CollapsibleViewTest : MockActivityTest(), CollapsibleView.OnCollapseListener {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_custom_collapsible_view_test
        restartActivity()
    }

    @SmallTest
    @Test
    fun shouldShowCollapsibleTitle() {
        // given
        val collapsibleViewTest: CollapsibleView? = ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // then
        isTextDisplayed("Collapsible title")
        Assert.assertEquals("Collapsible title", collapsibleViewTest?.getTitle())
    }

    @SmallTest
    @Test
    fun shouldShowCollapsibleSubtitle() {
        // given
        val collapsibleViewTest: CollapsibleView? = ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // then
        isTextDisplayed("Collapsible subtitle")
        Assert.assertEquals("Collapsible subtitle", collapsibleViewTest?.getSubtitle())
    }

    @SmallTest
    @Test
    fun shouldShowCollapsibleHiddenFooterText() {
        // given
        val collapsibleViewTest: CollapsibleView? = ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // then
        isTextDisplayed("Hide")
        Assert.assertEquals("Hide", collapsibleViewTest?.getHiddenFooterText())
    }

    @MediumTest
    @Test
    fun shouldBeCollapsedWhenICallCollapseFunction() {
        // given
        collapseView()

        // then
        isTextNotDisplayed("BUTTON TEST")
        isTextDisplayed("Show")
    }

    @SmallTest
    @Test
    fun shouldShowCollapsibleShowFooterTextWhenTheViewIsCollapse() {
        // given
        val collapsibleViewTest = collapseView()

        // then
        isTextDisplayed("Show")
        Assert.assertEquals("Show", collapsibleViewTest?.getShowFooterText())
    }

    @MediumTest
    @Test
    fun shouldCollapseAndExpandTheContentWhenIsClicked() {
        // given
        collapseView()

        // then
        isTextNotDisplayed("BUTTON TEST")
        clickWithText("Collapsible title")
        isTextDisplayed("BUTTON TEST")
    }

    @MediumTest
    @Test
    fun shouldGetCollapsibleViewChildAndMakeClick() {
        // given
        val collapsibleViewTest: CollapsibleView? = ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // when
        val buttonChild = collapsibleViewTest?.getContent() as? AppCompatButton
        buttonChild?.performClick()

        // then
        buttonChild?.text = "BUTTON TEST"
    }

    @SmallTest
    @Test
    fun shouldSetSectionHiddenFooterText() {
        // given
        val collapsibleViewTest: CollapsibleView? = ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.setHiddenFooterText("NEW HIDE TEXT")
        }

        // then
        isTextDisplayed("NEW HIDE TEXT")
        Assert.assertEquals("NEW HIDE TEXT", collapsibleViewTest?.getHiddenFooterText())
    }

    @SmallTest
    @Test
    fun shouldSetSectionShowFooterText() {
        // given
        val collapsibleViewTest: CollapsibleView? = ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.apply {
                setShowFooterText("NEW SHOW TEXT")
                collapse()
            }
        }

        // then
        isTextDisplayed("NEW SHOW TEXT")
        Assert.assertEquals("NEW SHOW TEXT", collapsibleViewTest?.getShowFooterText())
    }

    @SmallTest
    @Test
    fun shouldSetOnCollapseListener() {
        // given
        val collapsibleViewTest: CollapsibleView? = ruleActivity.activity.findViewById(R.id.collapsibleViewTest)
        collapsibleViewTest?.setOnCollapseListener(this)

        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.collapse()
        }
    }

    @SmallTest
    @Test
    fun shouldSetSectionSubtitle() {
        // given
        val collapsibleViewTest: CollapsibleView? = ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.setSubtitle("NEW SUBTITLE")
        }

        // then
        isTextDisplayed("NEW SUBTITLE")
        Assert.assertEquals("NEW SUBTITLE", collapsibleViewTest?.getSubtitle())
    }

    @SmallTest
    @Test
    fun shouldSetFooterTextColor() {
        // given
        val collapsibleViewTest: CollapsibleView? = ruleActivity.activity.findViewById(R.id.collapsibleViewTest)


        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.setFooterTextColor(Color.MAGENTA)
        }

        // then
        onView(withId(R.id.footerTextViewId)).check(matches(withTextColor(Color.MAGENTA)))
    }

    @SmallTest
    @Test
    fun shouldSetSectionTitle() {
        // given
        val collapsibleViewTest: CollapsibleView? = ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.setTitle("NEW TITLE")
        }

        // then
        isTextDisplayed("NEW TITLE")
        Assert.assertEquals("NEW TITLE", collapsibleViewTest?.getTitle())
    }

    @SmallTest
    @Test
    fun shouldStartCollapse() {
        // given
        val collapsibleViewTest: CollapsibleView? = ruleActivity.activity.findViewById(R.id.collapsibleViewTest)
        collapsibleViewTest?.setOnCollapseListener(this)

        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.startCollapsed()
        }
    }

    override fun onCollapse(isCollapsed: Boolean) {
        assertTrue(isCollapsed)
    }

    private fun collapseView(): CollapsibleView? {
        // given
        val collapsibleViewTest = ruleActivity.activity.findViewById<CollapsibleView>(R.id.collapsibleViewTest)

        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.collapse()
        }

        // then
        return collapsibleViewTest
    }
}
