package co.condorlabs.customcomponents.test

import android.graphics.Color
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import co.condorlabs.customcomponents.customcollapsibleview.CollapsibleView
import co.condorlabs.customcomponents.customcollapsibleview.OnCollapseListener
import co.condorlabs.customcomponents.test.util.clickWithText
import co.condorlabs.customcomponents.test.util.isTextDisplayed
import co.condorlabs.customcomponents.test.util.isTextNotDisplayed
import co.condorlabs.customcomponents.test.util.withTextColor
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Alexis Duque on 2019-06-04.
 * @company Condor Labs
 * @email eduque@condorlabs.io
 */
class CollapsibleViewTest : MockActivityTest(), OnCollapseListener {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_custom_collapsible_view_test
        restartActivity()
    }

    @SmallTest
    @Test
    fun shouldShowCollapsibleTitle() {
        // given
        val collapsibleViewTest: CollapsibleView? =
            ruleActivity.activity.findViewById(R.id.collapsibleViewTest)
        // then
        isTextDisplayed("Collapsible title")
        Assert.assertEquals("Collapsible title", collapsibleViewTest?.getTitle())
    }

    @SmallTest
    @Test
    fun shouldShowCollapsibleSubtitle() {
        // given
        val collapsibleViewTest: CollapsibleView? =
            ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // then
        isTextDisplayed("Collapsible subtitle")
        Assert.assertEquals("Collapsible subtitle", collapsibleViewTest?.getSubtitle())
    }

    @SmallTest
    @Test
    fun shouldShowCollapsibleHideActionLabel() {
        // given
        val collapsibleViewTest: CollapsibleView? =
            ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.collapse()
        }

        // then
        isTextDisplayed("Hide")
        Assert.assertEquals("Hide", collapsibleViewTest?.getHideActionLabel())
    }

    @MediumTest
    @Test
    fun shouldBeCollapsedWhenICallCollapseFunction() {
        // given
        val collapsibleViewTest =
            ruleActivity.activity.findViewById<CollapsibleView>(R.id.collapsibleViewTest)

        // then
        isTextDisplayed(collapsibleViewTest.getTitle())
        isTextNotDisplayed("BUTTON TEST")
        isTextDisplayed("Show")
    }

    @SmallTest
    @Test
    fun shouldShowCollapsibleShowActionLabelWhenTheViewIsCollapse() {
        // given
        val collapsibleViewTest = ruleActivity.activity.findViewById<CollapsibleView>(R.id.collapsibleViewTest)

        // then
        isTextDisplayed("Show")
        Assert.assertEquals("Show", collapsibleViewTest?.getShowActionLabel())
    }

    @MediumTest
    @Test
    fun shouldCollapseAndExpandTheContentWhenIsClicked() {
        // given
        val collapsibleViewTest =
            ruleActivity.activity.findViewById<CollapsibleView>(R.id.collapsibleViewTest)

        // then
        isTextNotDisplayed("BUTTON TEST")
        isTextDisplayed(collapsibleViewTest.getTitle())
        clickWithText("Collapsible title")
        isTextDisplayed("BUTTON TEST")
    }

    @MediumTest
    @Test
    fun shouldGetCollapsibleViewChildAndMakeClick() {
        // given
        val collapsibleViewTest: CollapsibleView? =
            ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // when
        val buttonChild = collapsibleViewTest?.getContent() as? AppCompatButton
        buttonChild?.performClick()

        // then
        buttonChild?.text = "BUTTON TEST"
    }

    @SmallTest
    @Test
    fun shouldSetSectionHideActionLabel() {
        // given
        val collapsibleViewTest: CollapsibleView? =
            ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.apply {
                setHideActionLabel("NEW HIDE TEXT")
                collapse()
            }
        }

        // then
        isTextDisplayed("NEW HIDE TEXT")
        Assert.assertEquals("NEW HIDE TEXT", collapsibleViewTest?.getHideActionLabel())
    }

    @SmallTest
    @Test
    fun shouldSetSectionShowActionLabel() {
        // given
        val collapsibleViewTest: CollapsibleView? =
            ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.setShowActionLabel("NEW SHOW TEXT")
        }

        // then
        isTextDisplayed("NEW SHOW TEXT")
        Assert.assertEquals("NEW SHOW TEXT", collapsibleViewTest?.getShowActionLabel())
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
    fun shouldNotSeeArrowIndicator() {
        // given
        val collapsibleViewTest: CollapsibleView? =
            ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        //then
        val arrowIndicatorImageView =
            collapsibleViewTest?.findViewById<AppCompatImageView>(R.id.actionImageViewId)
        Assert.assertEquals(arrowIndicatorImageView?.visibility ?: View.NO_ID, View.INVISIBLE)
    }

    @SmallTest
    @Test
    fun shouldSetActionLabelColor() {
        // given
        val collapsibleViewTest: CollapsibleView? =
            ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.setActionLabelColor(Color.MAGENTA)
        }

        // then
        onView(withId(R.id.actionLabelViewId)).check(matches(withTextColor(Color.MAGENTA)))
    }

    @SmallTest
    @Test
    fun shouldSetSectionSubtitle() {
        // given
        val collapsibleViewTest: CollapsibleView? =
            ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

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
    fun shouldSeeArrowIndicator() {
        // given
        val collapsibleViewTest: CollapsibleView? =
            ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.visibleIndicatorArrow(true)
        }

        //then
        val arrowIndicatorImageView =
            collapsibleViewTest?.findViewById<AppCompatImageView>(R.id.actionImageViewId)
        Assert.assertEquals(arrowIndicatorImageView?.visibility ?: View.NO_ID, View.VISIBLE)
    }

    @SmallTest
    @Test
    fun shouldSetSectionTitle() {
        // given
        val collapsibleViewTest: CollapsibleView? =
            ruleActivity.activity.findViewById(R.id.collapsibleViewTest)

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
    fun shouldStartExpanded() {
        // given
        val collapsibleViewTest: CollapsibleView? =
            ruleActivity.activity.findViewById(R.id.collapsibleViewTest)
        collapsibleViewTest?.setOnCollapseListener(this)

        // when
        ruleActivity.runOnUiThread {
            collapsibleViewTest?.startExpanded()
        }
    }

    override fun onCollapse(isCollapsed: Boolean) {
        Assert.assertFalse(isCollapsed)
    }
}
