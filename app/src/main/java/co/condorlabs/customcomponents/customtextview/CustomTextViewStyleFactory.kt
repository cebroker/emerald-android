package co.condorlabs.customcomponents.customtextview

import android.util.SparseArray
import co.condorlabs.customcomponents.StyleNotFoundForType
import co.condorlabs.customcomponents.TYPE_BODY
import co.condorlabs.customcomponents.TYPE_SUBTITLE
import co.condorlabs.customcomponents.TYPE_TITLE

class CustomTextViewStyleFactory {

    private val styles = SparseArray<CustomTextViewStyle>()

    fun getStyleFromType(type: Int): CustomTextViewStyle {
        styles.put(TYPE_BODY, CustomTextViewStyle.BodyTextViewStyle)
        styles.put(TYPE_TITLE, CustomTextViewStyle.TitleTextViewStyle)
        styles.put(TYPE_SUBTITLE, CustomTextViewStyle.SubTitleTextViewStyle)
        return styles[type] ?: throw StyleNotFoundForType(type)
    }
}