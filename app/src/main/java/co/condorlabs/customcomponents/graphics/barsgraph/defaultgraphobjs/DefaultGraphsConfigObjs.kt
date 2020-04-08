package co.condorlabs.customcomponents.graphics.barsgraph.defaultgraphobjs

import android.graphics.Color
import co.condorlabs.customcomponents.graphics.barsgraph.models.barsgraph.Bar
import co.condorlabs.customcomponents.graphics.barsgraph.models.barsgraph.BarsGraphConfig
import co.condorlabs.customcomponents.graphics.barsgraph.models.stackedbarsgraph.BarSection
import co.condorlabs.customcomponents.graphics.barsgraph.models.stackedbarsgraph.StackedBar
import co.condorlabs.customcomponents.graphics.barsgraph.models.stackedbarsgraph.StackedBarsGraphConfig

/**
 * Created by E Duque on 4/8/20.
 * Condor Labs
 * eduque@condorlabs.io
 */
val defaultStackedBarsGraphConfigObj = StackedBarsGraphConfig(
    2, listOf(
        StackedBar(
            "A", listOf(BarSection(3, Color.GRAY), BarSection(12, Color.BLUE))
        ),
        StackedBar("B", listOf(BarSection(30, Color.DKGRAY))),
        StackedBar("C", listOf(BarSection(5, Color.DKGRAY))),
        StackedBar("D", listOf(BarSection(15, Color.DKGRAY))),
        StackedBar("E", listOf(BarSection(23, Color.DKGRAY))),
        StackedBar(
            "F", listOf(BarSection(7, Color.RED), BarSection(2, Color.BLACK))
        )
    )
)

val defaultBarsGraphConfigObj = BarsGraphConfig(
    1, listOf(
        Bar("A", 38, Color.GRAY, Color.DKGRAY),
        Bar("B", 10, Color.DKGRAY, Color.BLUE),
        Bar("C", 23, Color.DKGRAY, Color.RED)
    )
)
