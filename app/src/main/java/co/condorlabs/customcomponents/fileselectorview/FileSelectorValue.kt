package co.condorlabs.customcomponents.fileselectorview

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

/**
 * @author Oscar Gallon on 3/13/19.
 */
sealed class FileSelectorValue {

    data class PathValue(val path: String) : FileSelectorValue()

    data class DrawableValue(val drawable: Drawable) : FileSelectorValue()

    data class BitmapValue(val bitmap: Bitmap) : FileSelectorValue()

    data class FileValue(val filepath: String, val filename: String? = null) : FileSelectorValue()
}
