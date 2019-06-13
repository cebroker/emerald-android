package co.condorlabs.customcomponents.customtextview

import android.util.SparseArray
import co.condorlabs.customcomponents.*

class CustomTextViewStyleFactory {

    private val styles = SparseArray<CustomTextViewStyle>().apply {
        put(BODY_TYPE, CustomTextViewStyle.BodyTextViewStyle)
        put(TITLE_TYPE, CustomTextViewStyle.TitleTextViewStyle)
        put(SUBTITLE_TYPE, CustomTextViewStyle.SubTitleTextViewStyle)
        put(SECTION_TITLE_TYPE, CustomTextViewStyle.SectionTitleTextViewStyle)
    }

    fun getStyleFromType(type: Int): CustomTextViewStyle {
        return styles[type] ?: throw StyleNotFoundForType(type)
    }
}
