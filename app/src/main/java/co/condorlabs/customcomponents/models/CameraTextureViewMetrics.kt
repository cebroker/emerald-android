package co.condorlabs.customcomponents.models

import android.util.Rational
import android.util.Size

/**
 * @author Alexis Duque on 2019-11-14.
 * @company Condor Labs.
 * @email eduque@condorlabs.io.
 */
data class CameraTextureViewMetrics(
    val aspectRatio: Rational,
    val rotation: Int,
    val resolution: Size
)
