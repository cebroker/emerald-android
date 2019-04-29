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

package co.condorlabs.customcomponents.test

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import co.condorlabs.customcomponents.customedittext.EditTextDateField
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Oscar Gallon on 2/25/19.
 */
class EditTextDateFieldTest : MockActivityTest() {

    private val mDefaultDateFormat = "MM/dd/yyyy"

    private val mSimpleDateFormat = SimpleDateFormat(mDefaultDateFormat, Locale.getDefault())

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
    }

    @Test
    fun shouldShowErrorWitheDateIncorrectPart1() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        val view = Espresso.onView(ViewMatchers.withId(R.id.etDate))

        // When
        view.perform(ViewActions.typeText("12/01/2YY9"))

        // Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_DATE_ERROR),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.isValid()
        )
    }

    @Test
    fun shouldShowErrorWitheDateIncorrectPart2() {
        MockActivity.layout = R.layout.activity_baseedittextdate_with_out_is_required
        restartActivity()

        // Given
        val view =
            Espresso.onView(ViewMatchers.withId(R.id.etDate))
        (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.setIsRequired(true)

        // When
        view.perform(ViewActions.replaceText(mDefaultDateFormat))

        // Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_DATE_ERROR),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.isValid()
        )
    }

    @Test
    fun shouldReturnTrueIsNotRequired() {
        MockActivity.layout = R.layout.activity_baseedittextdate_with_out_is_required
        restartActivity()

        // Then
        Assert.assertEquals(
            ValidationResult(true, EMPTY),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.isValid()
        )
    }

    @Test
    fun shouldReturnTrueWitheCorrectDate() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        val view = Espresso.onView(ViewMatchers.withId(R.id.etDate))

        // When
        view.perform(ViewActions.typeText("12/01/2019"))

        // Then
        Assert.assertEquals(
            ValidationResult(true, EMPTY),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.isValid()
        )
    }

    @Test
    fun shouldShowErrorWithMask() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        val view = Espresso.onView(ViewMatchers.withId(R.id.etDate))

        // When
        view.perform(ViewActions.replaceText("MM/DD/YYYY"))

        // Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_DATE_ERROR),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.isValid()
        )
    }

    @Test
    fun shouldParseValidLowerLimit() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        val format = mDefaultDateFormat
        val dateToParse = "02/25/2019"
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)

        // When
        field?.setLowerLimit(dateToParse, format)

        // Then
        Assert.assertEquals(
            SimpleDateFormat(format, Locale.getDefault()).parse(
                dateToParse
            ).time, field?.getLowerLimit()
        )
    }

    @Test
    fun shouldNotParseInvalidLowerLimit() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        val format = mDefaultDateFormat
        val dateToParse = "02/AA/2019"
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)

        // When
        field?.setLowerLimit(dateToParse, format)

        // Then
        Assert.assertNull(
            field?.getLowerLimit()
        )
    }

    @Test
    fun shouldParseValidUpperLimit() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        val format = mDefaultDateFormat
        val dateToParse = "02/25/2019"
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)

        // When
        field?.setUpperLimit(dateToParse, format)

        // Then
        Assert.assertEquals(
            SimpleDateFormat(format, Locale.getDefault()).parse(
                dateToParse
            ).time, field?.getUpperLimit()
        )
    }

    @Test
    fun shouldNotParseInvalidUpperLimit() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        val format = mDefaultDateFormat
        val dateToParse = "02/AA/2019"
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)

        // When
        field?.setUpperLimit(dateToParse, format)

        // Then
        Assert.assertNull(
            field?.getUpperLimit()
        )
    }

    @Test
    fun shouldShowAndErrorWithEmptyDate() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.setIsRequired(true)

        // When

        // Then
        Assert.assertEquals(
            ValidationResult(false, String.format(VALIDATE_EMPTY_ERROR, "Expiration")),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.isValid()
        )
    }

    @Test
    fun shouldReturnErrorIfDateBeforeMinDateIsEntered() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        val todayCalendar = Calendar.getInstance()
        val todayDate = todayCalendar.time
        val view = Espresso.onView(ViewMatchers.withId(R.id.etDate))
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        field?.setIsRequired(true)
        field?.setLowerLimit(todayDate.time)

        // When
        todayCalendar.add(Calendar.DAY_OF_MONTH, -1)
        view.perform(ViewActions.typeText(mSimpleDateFormat.format(todayCalendar.time)))

        // Then
        Assert.assertEquals(
            ValidationResult(
                false,
                String.format(VALIDATE_LOWER_LIMIT_DATE_ERROR, "Expiration", mSimpleDateFormat.format(todayDate))
            ),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.isValid()
        )
    }

    @Test
    fun shouldBeValidIfDateAfterMinDateIsEntered() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        val todayCalendar = Calendar.getInstance()
        val todayDate = todayCalendar.time
        val view = Espresso.onView(ViewMatchers.withId(R.id.etDate))
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        field?.setIsRequired(true)
        field?.setLowerLimit(todayDate.time)

        // When
        todayCalendar.add(Calendar.DAY_OF_MONTH, 1)
        view.perform(ViewActions.typeText(mSimpleDateFormat.format(todayCalendar.time)))

        // Then
        Assert.assertEquals(
            ValidationResult(
                true,
                EMPTY
            ),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.isValid()
        )
    }

    @Test
    fun shouldReturnErrorOnValueChangedAsMinDate() {
        MockActivity.layout = R.layout.activity_edittextdatefield_lower_limit_test
        restartActivity()

        // Given
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        val field2 = (ruleActivity.activity.findViewById<View>(R.id.tlDate2) as? EditTextDateField)
        field?.setIsRequired(true)
        field2?.setIsRequired(true)
        field?.setValueChangeListener(object : ValueChangeListener<String> {
            override fun onValueChange(value: String) {
                field2?.setLowerLimit(value)
            }
        })

        // When
        ruleActivity.runOnUiThread {
            field?.editText?.setText("03/03/2019")
            field2?.editText?.setText("02/03/2019")
        }

        // Then
        Assert.assertEquals(
            ValidationResult(
                false,
                String.format(VALIDATE_LOWER_LIMIT_DATE_ERROR, "Expiration 2", "03/03/2019")
            ),
            field2?.isValid()
        )
    }

    @Test
    fun shouldBeValidAfterOnValueChangedAsMinDate() {
        MockActivity.layout = R.layout.activity_edittextdatefield_lower_limit_test
        restartActivity()

        // Given
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        val field2 = (ruleActivity.activity.findViewById<View>(R.id.tlDate2) as? EditTextDateField)
        field?.setIsRequired(true)
        field2?.setIsRequired(true)
        field?.setValueChangeListener(object : ValueChangeListener<String> {
            override fun onValueChange(value: String) {
                field2?.setLowerLimit(value)
            }
        })

        // When
        ruleActivity.runOnUiThread {
            field?.editText?.setText("03/03/2019")
            field2?.editText?.setText("03/03/2019")
        }

        // Then
        Assert.assertEquals(
            ValidationResult(
                true,
                EMPTY
            ),
            field2?.isValid()
        )
    }

    @Test
    fun shouldReturnErrorIfDateAfterMaxDateIsEntered() {
        restartActivity()
        MockActivity.layout = R.layout.activity_edittextdatefield_test

        // Given
        val todayCalendar = Calendar.getInstance()
        val todayDate = todayCalendar.time
        val view = Espresso.onView(ViewMatchers.withId(R.id.etDate))
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        field?.setIsRequired(true)
        field?.setUpperLimit(todayDate.time)

        // When
        todayCalendar.add(Calendar.DAY_OF_MONTH, 1)
        view.perform(ViewActions.typeText(mSimpleDateFormat.format(todayCalendar.time)))

        // Then
        Assert.assertEquals(
            ValidationResult(
                false,
                String.format(VALIDATE_UPPER_LIMIT_DATE_ERROR, "Expiration", mSimpleDateFormat.format(todayDate))
            ),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.isValid()
        )
    }

    @Test
    fun shouldBeValidIfDateBeforeMaxDateIsEntered() {
        restartActivity()
        MockActivity.layout = R.layout.activity_edittextdatefield_test

        // Given
        val todayCalendar = Calendar.getInstance()
        val todayDate = todayCalendar.time
        val view = Espresso.onView(ViewMatchers.withId(R.id.etDate))
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        field?.setIsRequired(true)
        field?.setUpperLimit(todayDate.time)

        // When
        todayCalendar.add(Calendar.DAY_OF_MONTH, -1)
        view.perform(ViewActions.typeText(mSimpleDateFormat.format(todayCalendar.time)))

        // Then
        Assert.assertEquals(
            ValidationResult(
                true,
                EMPTY
            ),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.isValid()
        )
    }

    @Test
    fun shouldNotBeValidIfDateInsideLimitIsEntered() {
        MockActivity.layout = R.layout.activity_edittextdatefield_lower_and_upper_limit_test
        restartActivity()

        // Given
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        val field2 = (ruleActivity.activity.findViewById<View>(R.id.tlDate2) as? EditTextDateField)
        val field3 = (ruleActivity.activity.findViewById<View>(R.id.tlDate3) as? EditTextDateField)
        field?.setIsRequired(true)
        field2?.setIsRequired(true)
        field3?.setIsRequired(true)

        field?.setValueChangeListener(object : ValueChangeListener<String> {
            override fun onValueChange(value: String) {
                field3?.setLowerLimit(value)
            }
        })

        field2?.setValueChangeListener(object : ValueChangeListener<String> {
            override fun onValueChange(value: String) {
                field3?.setUpperLimit(value)
            }
        })

        // When
        ruleActivity.runOnUiThread {
            field?.editText?.setText("03/03/2019")
            field2?.editText?.setText("03/05/2019")
            field3?.editText?.setText("03/04/2019")
        }

        // Then
        Assert.assertEquals(
            ValidationResult(
                true,
                EMPTY
            ),
            field3?.isValid()
        )
    }

    @Test
    fun shouldNotBeValidIfDateBeforeLowerLimitIsEntered() {
        MockActivity.layout = R.layout.activity_edittextdatefield_lower_and_upper_limit_test
        restartActivity()

        // Given
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        val field2 = (ruleActivity.activity.findViewById<View>(R.id.tlDate2) as? EditTextDateField)
        val field3 = (ruleActivity.activity.findViewById<View>(R.id.tlDate3) as? EditTextDateField)
        field?.setIsRequired(true)
        field2?.setIsRequired(true)
        field3?.setIsRequired(true)

        field?.setValueChangeListener(object : ValueChangeListener<String> {
            override fun onValueChange(value: String) {
                field3?.setLowerLimit(value)
            }
        })

        field2?.setValueChangeListener(object : ValueChangeListener<String> {
            override fun onValueChange(value: String) {
                field3?.setUpperLimit(value)
            }
        })

        // When
        ruleActivity.runOnUiThread {
            field?.editText?.setText("03/03/2019")
            field2?.editText?.setText("03/05/2019")
            field3?.editText?.setText("03/02/2019")
        }

        // Then
        Assert.assertEquals(
            ValidationResult(
                false,
                String.format(VALIDATE_LOWER_LIMIT_DATE_ERROR, "Expiration 3", "03/03/2019")
            ),
            field3?.isValid()
        )
    }

    @Test
    fun shouldNotBeValidIfDateAfterUpperLimitIsEntered() {
        MockActivity.layout = R.layout.activity_edittextdatefield_lower_and_upper_limit_test
        restartActivity()

        // Given
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        val field2 = (ruleActivity.activity.findViewById<View>(R.id.tlDate2) as? EditTextDateField)
        val field3 = (ruleActivity.activity.findViewById<View>(R.id.tlDate3) as? EditTextDateField)
        field?.setIsRequired(true)
        field2?.setIsRequired(true)
        field3?.setIsRequired(true)

        field?.setValueChangeListener(object : ValueChangeListener<String> {
            override fun onValueChange(value: String) {
                field3?.setLowerLimit(value)
            }
        })

        field2?.setValueChangeListener(object : ValueChangeListener<String> {
            override fun onValueChange(value: String) {
                field3?.setUpperLimit(value)
            }
        })

        // When
        ruleActivity.runOnUiThread {
            field?.editText?.setText("03/03/2019")
            field2?.editText?.setText("03/05/2019")
            field3?.editText?.setText("03/06/2019")
        }

        // Then
        Assert.assertEquals(
            ValidationResult(
                false,
                String.format(VALIDATE_UPPER_LIMIT_DATE_ERROR, "Expiration 3", "03/05/2019")
            ),
            field3?.isValid()
        )
    }
}
