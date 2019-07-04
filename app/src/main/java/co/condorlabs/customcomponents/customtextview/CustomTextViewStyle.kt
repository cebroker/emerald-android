package co.condorlabs.customcomponents.customtextview

import co.condorlabs.customcomponents.*

sealed class CustomTextViewStyle(
    private val fontSize: FontSize,
    private val colorText: ColorText,
    private val fontType: FontType
) {

    object H2TitleTextViewStyle : CustomTextViewStyle(R.dimen.h2, R.color.textColor, OPEN_SANS_SEMI_BOLD)
    object TitleTextViewStyle : CustomTextViewStyle(R.dimen.h4, R.color.textColor, OPEN_SANS_SEMI_BOLD)
    object SubTitleTextViewStyle : CustomTextViewStyle(R.dimen.body, R.color.subtitleColor, OPEN_SANS_SEMI_BOLD)
    object BodyTextViewStyle : CustomTextViewStyle(R.dimen.body, R.color.labelColor, OPEN_SANS_REGULAR)
    object SectionTitleTextViewStyle : CustomTextViewStyle(R.dimen.section_title_size, R.color.textColor, OPEN_SANS_SEMI_BOLD)
    object LinkTextViewStyle : CustomTextViewStyle(R.dimen.small_button_size, R.color.primaryColor, OPEN_SANS_SEMI_BOLD)

    fun getFontSize(): FontSize {
        return fontSize
    }

    fun getColorText(): ColorText {
        return colorText
    }

    fun getFontType(): FontType {
        return fontType
    }
}
