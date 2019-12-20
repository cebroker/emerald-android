package co.condorlabs.customcomponents.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Alexis Duque on 2019-11-14.
 * @company Condor Labs.
 * @email eduque@condorlabs.io.
 */
@Parcelize
data class CameraConfig(
    val titleText: String? = null,
    val descriptionText: String? = null,
    val cancelButtonText: String? = null,
    val cropButtonText: String? = null,
    val keepAspectRatio: Boolean = false,
    val savePhotoPath: String? = null
) : Parcelable
