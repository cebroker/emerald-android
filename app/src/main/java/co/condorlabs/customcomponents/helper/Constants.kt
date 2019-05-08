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

package co.condorlabs.customcomponents.helper

const val VALIDATE_EMPTY_ERROR = "Field %s must not be empty."
const val VALIDATE_LOWER_LIMIT_DATE_ERROR = "The %s can't be before the %s"
const val VALIDATE_UPPER_LIMIT_DATE_ERROR = "The %s can't be after the %s"
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
const val MAX_LENGHT = 12
const val DIGITS_PHONE = "0123456789-"

const val DEFAULT_STYLE_ATTR = 0
const val DEFAULT_STYLE_RES = 0
const val PADDING_TOP = 5
const val DEFAULT_PADDING_RADIO_BUTTON = 16
const val DEFAULT_PADDING = 0

/**PHONE NUMBER FORMATS**/
const val PHONE_NUMBER_FORMAT_FIRST_HYPHEN_INDEX = 3
const val PHONE_NUMBER_FORMAT_SECOND_HYPHEN_INDEX = 7
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
const val PHONE_NUMBER_REGEX_THIRD_GROUP_MATCHER = "(\\d{0,4})"
const val PHONE_NUMBER_REGEX_FIRST_GROUP_REPLACEMENT_MATCHER = "$1"
const val PHONE_NUMBER_REGEX_SECOND_GROUP_REPLACEMENT_MATCHER = "$1-$2"
const val PHONE_NUMBER_REGEX_THIRD_GROUP_REPLACEMENT_MATCHER = "$1-$2-$3"
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
const val BUTTON_SUCCESS_TYPE = "success"

typealias ButtonType = String

/** FONT PATHS **/
const val OPEN_SANS_SEMI_BOLD = "fonts/OpenSans-SemiBold.ttf"
const val OPEN_SANS_REGULAR = "fonts/OpenSans-Regular.ttf"

/** RADIO GROUP **/
const val DEFAULT_SPACE_BETWEEN_ITEMS = 10

/** MONTH AND YEAR PICKER **/
const val MAX_YEAR = 2099
const val MIN_YEAR = 1900
const val MIN_MONTH = 0
const val MAX_MONTH = 11
const val FIRST_DAY_OF_MONTH = 11
val MONTHS = arrayOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)
const val MONTH_YEAR_PICKER_DIALOG_TAG = "MonthYearPickerDialog"

/** MONTH AND YEAR FORMAT **/
const val MONTH_YEAR_REGEX = "\\d{2}\\/\\d{2}"
const val MONTH_YEAR_FORMAT_WITHOUT_SLASH = "MMYYYY"
const val MONTH_YEAR_MASK_LENGTH = 6
const val MONTH_YEAR_MASK_MONTH_INITIAL_INDEX = 0
const val MONTH_YEAR_MASK_MONTH_FINAL_INDEX = 2
const val MONTH_YEAR_MASK_YEAR_INITIAL_INDEX = 2
const val MONTH_YEAR_MASK_YEAR_FINAL_INDEX = 6
const val MONTH_YEAR_MASK_JUST_DIGITS_LENGTH = 4
const val MONTH_YEAR_MASK_DIGITS_STRING_FORMAT = "%02d%02d"
const val MONTH_YEAR_FORMAT = "MM/yyyy"
