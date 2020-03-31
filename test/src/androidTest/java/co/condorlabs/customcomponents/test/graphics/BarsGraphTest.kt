package co.condorlabs.customcomponents.test.graphics

import android.graphics.Color
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import co.condorlabs.customcomponents.graphics.barsgraph.BarsGraph
import co.condorlabs.customcomponents.graphics.barsgraph.models.barsgraph.Bar
import co.condorlabs.customcomponents.graphics.barsgraph.models.barsgraph.BarsGraphConfig
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
class BarsGraphTest : MockActivityTest() {

    private val barsGraphConfigTest = BarsGraphConfig(
        1,
        listOf(
            Bar("TEST", 14, Color.BLACK, Color.DKGRAY),
            Bar("EMERALD", 8, Color.BLACK, Color.BLUE),
            Bar("ANDROID", 21, Color.BLACK, Color.RED)
        )
    )

    @Test
    fun shouldDisplayDefaultHint() {
        MockActivity.layout = R.layout.activity_bars_graph_test
        restartActivity()

        // Given
        val barsGraphTest = ruleActivity.activity.findViewById<BarsGraph>(R.id.barsGraphTest)

        // When
        barsGraphTest.setupConfig(barsGraphConfigTest)

        // Then
        onView(withId(barsGraphTest.id)).check(matches(isDisplayed()))
    }
}
