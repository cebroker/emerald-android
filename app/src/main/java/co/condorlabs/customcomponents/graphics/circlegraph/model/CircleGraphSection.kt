package co.condorlabs.customcomponents.graphics.circlegraph.model

import androidx.annotation.ColorRes

/**
 * Created by E Duque on 4/16/20.
 * Condor Labs
 * eduque@condorlabs.io
 */
data class CircleGraphSection(
    val value: Int,
    @ColorRes val borderColor: Int,
    @ColorRes val fillColor: Int
)
