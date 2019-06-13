package co.condorlabs.customcomponents.customtextview

import android.util.SparseArray
import co.condorlabs.customcomponents.*

class CustomTextViewStyleFactory {

    private val styles = SparseArray<CustomTextViewStyle>()

    fun getStyleFromType(type: Int): CustomTextViewStyle {
        styles.put(TYPE_BODY, CustomTextViewStyle.BodyTextViewStyle)
        styles.put(TYPE_TITLE, CustomTextViewStyle.TitleTextViewStyle)
        styles.put(TYPE_SUBTITLE, CustomTextViewStyle.SubTitleTextViewStyle)
        styles.put(TYPE_SECTION_TITLE, CustomTextViewStyle.SectionTitleTextViewStyle)
        return styles[type] ?: throw StyleNotFoundForType(type)
    }
}