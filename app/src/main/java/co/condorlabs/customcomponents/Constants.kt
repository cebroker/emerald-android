/*
 * Copyright 2019 CondorLabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.condorlabs.customcomponents

const val VALIDATE_EMPTY_ERROR = "Must not be empty."
const val VALIDATE_LOWER_LIMIT_DATE_ERROR = "The %s must be after %s"
const val VALIDATE_UPPER_LIMIT_DATE_ERROR = "The %s must be before %s"
const val VALIDATE_UPPER_THAN_CURRENT_DATE = "The %s can't be after the current date"
const val VALIDATE_LOWER_THAN_CURRENT_DATE = "The %s can't be before the current date"
const val VALIDATE_LENGTH_ERROR = "This field must have ten digits."
const val VALIDATE_EMAIL_ERROR = "Email incorrect."
const val VALIDATE_DATE_ERROR = "Date incorrect."
const val VALIDATE_CURRENCY_ERROR = "Currency incorrect."
const val VALIDATE_CITY_ERROR = "City must belong to the state "
const val VALIDATE_INCORRECT_ERROR = "Field %s is not valid."
const val MESSAGE_FORMAT_ERROR = "Field %s must have an element selected."
const val NO_RADIO_GROUP_SELECTED_VALUE_FOUND_RETURNED_VALUE = -1
const val STATE_SPINNER_HINT_POSITION = 0

const val EMPTY = ""
const val MAX_LENGTH = 13
const val DIGITS_PHONE = "0123456789-"

const val DEFAULT_STYLE_ATTR = 0
const val DEFAULT_STYLE_RES = 0
const val PADDING_TOP = 5
const val DEFAULT_PADDING_RADIO_BUTTON = 16
const val DEFAULT_PADDING = 0

/**PHONE NUMBER FORMATS**/
const val PHONE_NUMBER_FORMAT_FIRST_HYPHEN_INDEX = 3
const val PHONE_NUMBER_FORMAT_SECOND_HYPHEN_INDEX = 8
const val HYPHEN = "-"
const val PHONE_NUMBER_REGEX = "\\d{3}-\\d{3}-\\d{4}"
const val NO_DIGITS_REGEX = "[^\\d.]|\\."
const val DATE_REGEX = "\\d{2}\\/\\d{2}\\/\\d{4}"
const val SLASH = "/"
const val ZERO = 0
const val ONE = 1
const val PHONE_NUMBER_REGEX_FIRST_GROUP_RANGE_BOTTOM = 0
const val PHONE_NUMBER_REGEX_FIRST_GROUP_RANGE_TOP = 3
const val PHONE_NUMBER_REGEX_SECOND_GROUP_RANGE_BOTTOM = 4
const val PHONE_NUMBER_REGEX_SECOND_GROUP_RANGE_TOP = 6
const val PHONE_NUMBER_REGEX_THIRD_GROUP_RANGE_BOTTOM = 7
const val PHONE_NUMBER_REGEX_THIRD_GROUP_RANGE_TOP = 10
const val PHONE_NUMBER_REGEX_FIRST_AND_SECOND_GROUP_MATCHER = "(\\d{3})"
const val PHONE_NUMBER_REGEX_SECOND_GROUP_MATCHER = "(\\d{0,3})"
const val PHONE_NUMBER_REGEX_THIRD_GROUP_MATCHER = "(\\d{0,4})"
const val PHONE_NUMBER_REGEX_FIRST_GROUP_REPLACEMENT_MATCHER = "($1)"
const val PHONE_NUMBER_REGEX_SECOND_GROUP_REPLACEMENT_MATCHER = "($1)$2"
const val PHONE_NUMBER_REGEX_THIRD_GROUP_REPLACEMENT_MATCHER = "($1)$2-$3"
const val FIRST_EDITTEXT_SELECTION_CHARACTER = 0
const val PHONE_NUMBER_SEPARATOR_TOKEN = "-"

/**DATE FORMATS**/
const val DATE_MASK_DATE_FORMAT_WITHOUT_SLASH = "MMDDYYYY"
const val DATE_MASK_MIN_MONTH_INDEX = 1
const val DATE_MASK_MAX_MONTH_INDEX = 12
const val DATE_MASK_MIN_YEAR = 1900
const val DATE_MASK_MAX_YEAR = 2100
const val DATE_MASK_LOOP_STEP = 2
const val DATE_MASK_LENGTH = 8
const val DATE_MASK_DAY_INITIAL_INDEX = 2
const val DATE_MASK_DAY_FINAL_INDEX = 4
const val DATE_MASK_MONTH_INITIAL_INDEX = 0
const val DATE_MASK_MONTH_FINAL_INDEX = 2
const val DATE_MASK_YEAR_INITIAL_INDEX = 4
const val DATE_MASK_YEAR_FINAL_INDEX = 8
const val DATE_MASK_SELECTION_MIN_INDEX = 0
const val DATE_MASK_MONTH_INDEX_DEFAULT_AGGREGATOR_VALUE = 1
const val DATE_MASK_JUST_DIGITS_LENGTH = 6
const val DATE_MASK_DIGITS_STRING_FORMAT = "%02d%02d%02d"
const val DATE_MASK_MAX_EMS = 10
const val DEFAULT_DATE_FORMAT = "MM/dd/yyyy"
const val COMPOUND_DRAWABLE_POSITION_ARRAY_SIZE = 2
const val DATE_EDIT_TEXT_RIGHT_COMPOUND_DRAWABLE_POSITION = 0
const val DRAWABLE_RIGHT_POSITION = 2
const val COMPOUND_DRAWABLE_TOUCH_OFF_SET = 32

const val NOT_DEFINED_ATTRIBUTE_DEFAULT_VALUE = -1
const val FILE_SELECTOR_GALLERY_OPTION_INDEX = 0
const val LINES_DEFAULT = 3
const val MAXLINES_DEFAULT = 5

/**MONEY FORMATS**/
const val DOLLAR_SYMBOL = "$"
const val MONEY_FORMAT = "$###,###.##"
const val MONEY_TWO_DECIMALS = "#.00"
const val MONEY_MAX_AMOUNT = 1000000000000
const val NON_NUMERICAL_SYMBOLS = "[$,]"
const val COMMA_AS_DECIMAL = ","
const val DOT_CHARACTER = '.'
const val DOT_STRING = "."
const val ZERO_CHARACTER = '0'
const val THREE_DIGITS = "\\d{3}"
const val ZERO_AFTER_DIGIT = "\\d[0]"

const val BUTTON_DEFAULT_TYPE = "default"
const val BUTTON_PRIMARY_TYPE = "primary"
const val BUTTON_DANGER_TYPE = "danger"
const val BUTTON_WARNING_TYPE = "warning"
const val BUTTON_INFO_TYPE = "info"
const val BUTTON_SHAPE_WHITE_TYPE = "shapeWhite"
const val BUTTON_SHAPE_TYPE = "shape"
const val BUTTON_SUCCESS_TYPE = "success"
const val BUTTON_OVERLAY_TYPE = "overlay"
const val BUTTON_FLAT_PRIMARY_TYPE = "flatPrimary"

typealias ButtonType = String

/** FONT PATHS **/
const val OPEN_SANS_SEMI_BOLD = "fonts/OpenSans-SemiBold.ttf"
const val OPEN_SANS_REGULAR = "fonts/OpenSans-Regular.ttf"

/** RADIO GROUP **/
const val DEFAULT_SPACE_BETWEEN_ITEMS = 30

/** MONTH AND YEAR PICKER **/
const val DATE_PICKER_MAX_YEAR = 2099
const val DATE_PICKER_MIN_YEAR = 1900
const val DATE_PICKER_MIN_MONTH = 0
const val DATE_PICKER_MAX_MONTH = 11
const val DATE_PICKER_FIRST_DAY_OF_MONTH = 1
val MONTHS = arrayOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

/** MONTH AND YEAR FORMAT **/
const val MONTH_YEAR_REGEX = "\\d{2}\\/\\d{4}"
const val MONTH_YEAR_FORMAT_WITHOUT_SLASH = "MMYYYY"
const val MONTH_YEAR_MASK_LENGTH = 6
const val MONTH_YEAR_MASK_MONTH_INITIAL_INDEX = 0
const val MONTH_YEAR_MASK_MONTH_FINAL_INDEX = 2
const val MONTH_YEAR_MASK_YEAR_INITIAL_INDEX = 2
const val MONTH_YEAR_MASK_YEAR_FINAL_INDEX = 6
const val MONTH_YEAR_MASK_JUST_DIGITS_LENGTH = 4
const val MONTH_YEAR_MASK_DIGITS_STRING_FORMAT = "%02d%02d"
const val MONTH_YEAR_FORMAT = "MM/yyyy"
const val MONTH_YEAR_STRING_TO_REPLACE = "%d/%d"
const val HUMAN_READABLE_MONTH_INDEX = 1

/** CUSTOM VIEW ACTIONS **/
const val CLICK_DRAWABLE_DESCRIPTION = "click drawable"
const val CLICK_DRAWABLE_DESCRIPTION_APPEND = "has drawable"
const val NUMBER_PICKER_VALUE_SETTER_DESCRIPTION = "Set the value of a Number Picker"
const val TEXT_VIEW_BOUNDS_SIDES = 4
const val DRAWABLE_START_INDEX = 0
const val DRAWABLE_END_INDEX = 3
const val ZERO_FLOAT = 0.0F
const val TINT_COLOR_IN_RADIO_BUTTON_DESCRIPTION = "with expected color: "
const val RADIO_GROUP_POSITION = 2
const val WITH_FONT_SIZE_DESCRIPTION = "with expected font size: "
const val WITH_TEXT_IN_LINES_DESCRIPTION = "with expected text in lines: "
const val WITH_TEXT_COLOR_DESCRIPTION = "with expected text color: "
const val CALCULATE_KEYBOARD_DISPLAYED_MARGIN_ERROR = 50F

/**LOADING FRAGMENTS **/
const val DEFAULT_TIME_BETWEEN_OBJECT_ANIMATION = 500.toLong()
const val LOADING_ADAPTER_FIRST_POSITION = 0
const val ARGUMENT_LOADING_ITEM_LIST = "ARGUMENT_LOADING_ITEM_LIST"
const val ARGUMENT_TITLE = "ARGUMENT_TITLE"
const val LOADING_FRAGMENT_MAX_ELEMENTS = 4
const val ARGUMENT_SUCCESS_TITLE = "ARGUMENT_SUCCESS_TITLE"
const val ARGUMENT_ERROR_TITLE = "ARGUMENT_ERROR_TITLE"
const val ARGUMENT_SUCCESS_MESSAGE = "ARGUMENT_SUCCESS_MESSAGE"
const val ARGUMENT_ERROR_MESSAGE = "ARGUMENT_ERROR_MESSAGE"

const val DEFAULT_STROKE_WIDTH = 3
const val SIGNATURE_DRAW_STROKE_WIDTH = 4f
const val SIGNATURE_LINE_STROKE_WIDTH = 2f
const val SIGNATURE_CANVAS_DIP_VALUE = 5f
const val SIGNATURE_CANVAS_DIP_FACTOR = 3
const val SIGNATURE_CANVAS_WIDTH_PERCENTAGE = 0.05
const val SIGNATURE_CANVAS_HEIGHT_PERCENTAGE = 0.7
const val SIGNATURE_USE_YOUR_FINGER_TO_DRAW = "Use your finger to draw"
const val SIGNATURE_CANVAS_FONT = "font-family: sans-serif;"
const val SIGNATURE_INDICATOR = "X"

const val ARGUMENT_SKELETON_TYPE = "ARGUMENT_SKELETON_TYPE"

const val COMPLETION_TYPE = "completion"

const val PROGRESS_DRAWABLE_SIZE_MULTIPLIER = 2
const val PROGRESS_DRAWABLE_DIAMETER_DIVIDER = 2
const val PROGRESS_DRAWABLE_LAYER_INSET_INDEX = 1

const val BODY_TYPE = 0
const val TITLE_TYPE = 1
const val SUBTITLE_TYPE = 2
const val SECTION_TITLE_TYPE = 3
const val LINK_TYPE = 4
const val H2_TITLE_TYPE = 5
const val SECTION_BODY_TYPE = 6
const val H1_TITLE_TYPE = 7
const val H3_TITLE_TYPE = 8
const val MILLISECONDS_TO_SHOW_PLACE_HOLDER = 200L

/** COLLAPSIBLE VIEW **/
const val DEFAULT_COLLAPSIBLE_SUBTITLE_TOP_MARGIN = 8
const val DEFAULT_COLLAPSIBLE_MARGIN_ACTION_TEXT = 34
const val DEFAULT_COLLAPSIBLE_ANIMATION_TIME = 300L
const val DEFAULT_COLLAPSIBLE_TITLE_TEXT_SIZE = 16F
const val DEFAULT_COLLAPSIBLE_IMAGE_HEIGHT = 100
const val DEFAULT_COLLAPSIBLE_IMAGE_WIDTH = 100
const val DEFAULT_COLLAPSIBLE_PADDING = 8
const val DEFAULT_COLLAPSIBLE_MARGIN = 0
const val COLLAPSIBLE_LINE_SEPARATOR_START_MARGIN = 60
const val COLLAPSIBLE_CARD_VIEW_START_MARGIN = -8
const val COLLAPSIBLE_CARD_VIEW_END_MARGIN = -8
const val COLLAPSIBLE_INDICATOR_ROTATION = 180F
const val COLLAPSIBLE_LINE_SEPARATOR_HEIGHT = 2
const val DEFAULT_COLLAPSIBLE_INDICATOR_MARGIN_END = 20
const val DEFAULT_COLLAPSIBLE_TITLE_MARGIN_TOP = 50
const val DEFAULT_COLLAPSIBLE_SUBTITLE_MARGIN_BOTTOM = 40
const val ONE_HUNDRED_FLOAT = 100F
const val MIN_DEFAULT_PADDING = 0

/** MASKS **/
const val MASK_TO_OBTAIN_FILE_SELECTOR_OPTION_GALLERY = 0x01
const val MASK_TO_OBTAIN_FILE_SELECTOR_OPTION_CAMERA = 0x02
const val MASK_TO_OBTAIN_FILE_SELECTOR_OPTION_FILE = 0x04

/** TAGS**/
const val TAG_IMAGE_VIEW_FILE_SELECTOR_VALUE = 35967070

/** EDIT TEXT INPUT TYPES **/
const val INPUT_TYPE_NUMBER = "number"
const val INPUT_TYPE_NUMBER_DECIMAL = "numberDecimal"
const val INPUT_TYPE_PHONE = "phone"
const val INPUT_TYPE_PASSWORD = "password"
const val INPUT_TYPE_TEXT = "text"
const val INPUT_TYPE_TEXT_CAP_CHARACTERS = "textCapCharacters"

/** FILE EXTENSIONS **/
const val EXTENSION_PDF = "pdf"
const val EXTENSION_DOC = "doc"
const val EXTENSION_DOCX = "docx"
const val EXTENSION_JPG = "jpg"
const val EXTENSION_JPEG = "jpeg"
const val EXTENSION_PNG = "png"

/** FILE SELECTOR OPTIONS **/
const val FILE_SELECTOR_OPTION_PHOTO = "Photo"
const val FILE_SELECTOR_OPTION_GALLERY = "Gallery"
const val FILE_SELECTOR_OPTION_FILE = "File"

/** FILE SELECTOR VALUES **/
const val FILE_AFTER_DOT_INDEX = 1
