package co.condorlabs.customcomponents.fileselectorview

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

/**
 * @author Oscar Gallon on 3/13/19.
 */
sealed class FileSelectorValue {

    class PathValue(val path: String) : FileSelectorValue()

    class DrawableValue(val drawable: Drawable) : FileSelectorValue()

    class BitmapValue(val bitmap: Bitmap) : FileSelectorValue()

    class FileValue(val filepath: String, val filename: String? = null) : FileSelectorValue()
}
