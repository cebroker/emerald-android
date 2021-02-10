package co.condorlabs.customcomponents.customtextview

import android.util.SparseArray
import co.condorlabs.customcomponents.*

object CustomTextViewStyleFactory {

    private val styles = SparseArray<CustomTextViewStyle>().apply {
        put(BODY_TYPE, CustomTextViewStyle.BodyTextViewStyle)
        put(TITLE_TYPE, CustomTextViewStyle.TitleTextViewStyle)
        put(SUBTITLE_TYPE, CustomTextViewStyle.SubTitleTextViewStyle)
        put(SECTION_TITLE_TYPE, CustomTextViewStyle.SectionTitleTextViewStyle)
        put(SECTION_BODY_TYPE, CustomTextViewStyle.SectionBodyTextViewStyle)
        put(LINK_TYPE, CustomTextViewStyle.LinkTextViewStyle)
        put(H2_TITLE_TYPE, CustomTextViewStyle.H2TitleTextViewStyle)
        put(H1_TITLE_TYPE, CustomTextViewStyle.H1TitleTextViewStyle)
        put(H3_TITLE_TYPE, CustomTextViewStyle.H3TitleTextViewStyle)
        put(BODY_SMALL_TYPE, CustomTextViewStyle.BodySmallTextViewStyle)
    }

    fun getStyleFromType(type: Int): CustomTextViewStyle {
        return styles[type] ?: throw StyleNotFoundForType(type)
    }
}
