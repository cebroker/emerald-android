package co.condorlabs.customcomponents.customedittext

import android.os.Parcel
import android.os.Parcelable
import androidx.customview.view.AbsSavedState

/**
 * Created by Camilo Medina on 6/01/21
 */
class SavedState : AbsSavedState {
    var text: String? = null

    constructor(superState: Parcelable) : super(superState)

    constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
        text = source.readString()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeString(text)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.ClassLoaderCreator<SavedState> = object : Parcelable.ClassLoaderCreator<SavedState> {
            override fun createFromParcel(source: Parcel, loader: ClassLoader): SavedState {
                return SavedState(source, loader)
            }

            override fun createFromParcel(source: Parcel): SavedState {
                return SavedState(source, null)
            }

            override fun newArray(size: Int): Array<SavedState> {
                return newArray(size)
            }
        }
    }
}
