package co.condorlabs.customcomponents.graphics.barsgraph.models.barsgraph

import android.graphics.RectF

/**
 * Created by E Duque on 3/30/20.
 * Condor Labs
 * eduque@condorlabs.io
 */
class Bar(
    val label: String?,
    val value: Int,
    val fillColor: Int,
    val strokeColor: Int,
    left: Float = 0F,
    top: Float = 0F,
    right: Float = 0F,
    bottom: Float = 0F
) : RectF(left, top, right, bottom)
