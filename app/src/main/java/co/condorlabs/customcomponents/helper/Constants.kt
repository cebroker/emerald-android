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

const val VALIDATE_EMPTY_ERROR = "Field %s must not be empty"
const val VALIDATE_NUMERIC_ERROR = "This field must be numeric"
const val VALIDATE_LENGTH_ERROR = "This field must have ten digits."
const val VALIDATE_EMAIL_ERROR = "Email incorrect."
const val VALIDATE_DATE_ERROR = "Date incorrect."
const val VALIDATE_CURRENCY_ERROR = "Currency incorrect."
const val VALIDATE_CITY_ERROR = "City must belong to the state "
const val VALIDATE_SPINNER_NO_SELECTION_ERROR = "Field %s must have an element selected"
const val VALIDATE_RADIOGROUP_NO_SELECTION_ERROR = "Field must have an element selected"

const val EMPTY = ""
const val MAX_LENGHT = 12
const val DIGITS_PHONE = "0123456789-"

const val DEFAULT_STYLE_ATTR = 0
const val DEFAULT_STYLE_RES = 0

/**PHONE NUMBER FORMATS**/
const val PHONE_NUMBER_FORMAT_FIRST_HYPHEN_INDEX = 3
const val PHONE_NUMBER_FORMAT_SECOND_HYPHEN_INDEX = 7
const val PHONE_NUMBER_FORMAT_FIRST_NUMBER_AFTER_HYPHEN_INDEX = 4
const val PHONE_NUMBER_FORMAT_SECOND_NUMBER_AFTER_HYPHEN_INDEX = 8
const val PHONE_NUMBER_FORMAT_NO_HYPHEN_COUNT = 0
const val PHONE_NUMBER_FORMAT_ONE_HYPHEN_COUNT = 1
const val HYPHEN = "-"
const val OPTION_PHONE = 0
const val OPTION_EMAIL = 1
const val OPTION_DATE = 2
const val PHONE_NUMBER_REGEX = "\\d{3}-\\d{3}-\\d{4}"
const val NO_DIGITS_REGEX = "[^\\d.]|\\."
const val DATE_REGEX = "\\d{2}\\/\\d{2}\\/\\d{4}"
const val SLASH = "/"
const val ONE = 1
const val ZERO = 0
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
const val HEALTH_PROVIDER_MINIMUM_NUMBER_OF_DAYS_TO_RENEW_VACCINES_ = 30
const val RESPONSE_DATE_FORMAT = "yyyy-MM-dd"
const val DATE_FORMAT_EXPIRATION_DATE = "LLLL yyyy"
const val DATE_FORMAT_EXPIRATION_DATE_OCR = "MM/yyyy"
const val COMPOUND_DRAWABLE_POSITION_ARRAY_SIZE = 2
const val DATE_EDIT_TEXT_RIGHT_COMPOUND_DRAWABLE_POSITION = 0
const val DRAWABLE_RIGHT_POSITION = 2
const val COMPOUND_DRAWABLE_TOUCH_OFF_SET = 32
