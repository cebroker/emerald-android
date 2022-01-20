package co.condorlabs.customcomponents.test

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.matcher.ViewMatchers
import co.condorlabs.customcomponents.graphics.barsgraph.BarsGraph
import co.condorlabs.customcomponents.graphics.barsgraph.models.barsgraph.Bar
import co.condorlabs.customcomponents.graphics.barsgraph.models.barsgraph.BarsGraphConfig
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Author: Felipe Guerrero
 */
class BarsGraphTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_bars_graph_test
        restartActivity()
    }

    @Test
    fun `givenTheBarsGraphInflatedWhenABarIsClickedThenItShouldReturnTheBar`() {
        // Given
        val barsCollection = listOf(
            Bar(
                "bar1",
                1,
                R.color.dangerColor,
                R.color.dangerRippleColor
            ),
            Bar(
                "bar2",
                2,
                R.color.infoColor,
                R.color.defaultHighlightColor
            )
        )
        val barsGraph: BarsGraph = ruleActivity.activity.findViewById(R.id.barsGraphTest)
        var result = ""
        ruleActivity.runOnUiThread {
            barsGraph.run {
                setupConfig(
                    BarsGraphConfig(2, barsCollection)
                )
                setOnBarClickListener {
                    result = it.label.orEmpty()
                }
            }
        }


        // When
        Espresso.onView(ViewMatchers.withId(R.id.barsGraphTest)).perform(
            GeneralClickAction(
                Tap.SINGLE,
                {
                    val screenPos = IntArray(2)
                    it.getLocationOnScreen(screenPos)

                    val screenX: Float = screenPos[0] + 115F
                    val screenY: Float = screenPos[1] + 193F

                    floatArrayOf(screenX, screenY)

                },
                Press.FINGER
            )
        )

        // Then
        Assert.assertEquals("bar1", result)
    }
}
