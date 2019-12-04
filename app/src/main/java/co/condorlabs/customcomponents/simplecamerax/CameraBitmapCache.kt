package co.condorlabs.customcomponents.simplecamerax

import android.graphics.Bitmap

/**
 * @author Alexis Duque on 2019-12-04.
 * @company Condor Labs.
 * @email eduque@condorlabs.io.
 */
object CameraBitmapCache {

    private var bitmap: Bitmap? = null

    fun getBitmap(): Bitmap? {
        val bitmapToReturn = bitmap
        bitmap = null
        return bitmapToReturn
    }

    fun setBitmap(bitmap: Bitmap?) {
        this.bitmap = bitmap
    }
}
