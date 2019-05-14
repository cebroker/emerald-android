package co.condorlabs.customcomponents.loadingfragment

import android.os.Parcel
import android.os.Parcelable

/**
 * @author Oscar Gallon on 2019-05-03.
 */
data class LoadingItem(
    val title: String
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoadingItem> {
        override fun createFromParcel(parcel: Parcel): LoadingItem {
            return LoadingItem(parcel)
        }

        override fun newArray(size: Int): Array<LoadingItem?> {
            return arrayOfNulls(size)
        }
    }
}
