package co.condorlabs.customcomponents.test.graphics

import android.graphics.Color
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import co.condorlabs.customcomponents.graphics.barsgraph.StackedBarsGraph
import co.condorlabs.customcomponents.graphics.barsgraph.models.stackedbarsgraph.BarSection
import co.condorlabs.customcomponents.graphics.barsgraph.models.stackedbarsgraph.StackedBar
import co.condorlabs.customcomponents.graphics.barsgraph.models.stackedbarsgraph.StackedBarsGraphConfig
import co.condorlabs.customcomponents.test.MockActivity
import co.condorlabs.customcomponents.test.MockActivityTest
import co.condorlabs.customcomponents.test.R
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by E Duque on 3/31/20.
 * Condor Labs
 * eduque@condorlabs.io
 */
@RunWith(AndroidJUnit4::class)
class StackedBarsGraphTest : MockActivityTest() {

    private val stackedBarsGraphConfigTest = StackedBarsGraphConfig(
        3,
        listOf(
            StackedBar(
                "ENE",
                listOf(
                    BarSection(
                        3,
                        Color.RED
                    ),
                    BarSection(
                        12,
                        Color.GREEN
                    )
                )
            ),
            StackedBar(
                "FEB", listOf(
                    BarSection(
                        38,
                        Color.BLUE
                    )
                )
            )
        )
    )

    @Test
    fun shouldDisplayDefaultHint() {
        MockActivity.layout = R.layout.activity_stacked_bars_graph_test
        restartActivity()

        // Given
        val stackedBarsGraphTest =
            ruleActivity.activity.findViewById<StackedBarsGraph>(R.id.stackedBarsGraphTest)

        // When
        stackedBarsGraphTest.setupConfig(stackedBarsGraphConfigTest)

        // Then
        onView(withId(stackedBarsGraphTest.id)).check(matches(isDisplayed()))
    }
}
