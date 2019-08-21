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

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.SmallTest
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.customedittext.EditTextDateField
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.test.util.isTextNotDisplayed
import kotlinx.coroutines.runBlocking
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

    @SmallTest
    @Test
    fun shouldShowErrorWitheDateIncorrectPart1() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        val realEditText = field!!.textInputLayout!!.editText!!
        val view = Espresso.onView(ViewMatchers.withId(realEditText.id))

        // When
        view.perform(ViewActions.typeText("12/01/2YY9"))

        // Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_DATE_ERROR),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.isValid()
        )
    }

    @SmallTest
    @Test
    fun shouldShowErrorWitheDateIncorrectPart2() {
        MockActivity.layout = R.layout.activity_baseedittextdate_with_out_is_required
        restartActivity()

        // Given
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        val realEditText = field!!.textInputLayout!!.editText!!
        val view = Espresso.onView(ViewMatchers.withId(realEditText.id))
        field.setIsRequired(true)

        // When
        view.perform(ViewActions.replaceText(mDefaultDateFormat))

        // Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_DATE_ERROR),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.isValid()
        )
    }

    @SmallTest
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

    @SmallTest
    @Test
    fun shouldReturnTrueWitheCorrectDate() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        val realEditText = field!!.textInputLayout!!.editText!!
        val view = Espresso.onView(ViewMatchers.withId(realEditText.id))

        // When
        view.perform(ViewActions.typeText("12/01/2019"))

        // Then
        Assert.assertEquals(
            ValidationResult(true, EMPTY),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.isValid()
        )
    }

    @SmallTest
    @Test
    fun shouldShowErrorWithMask() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        val realEditText = field!!.textInputLayout!!.editText!!
        val view = Espresso.onView(ViewMatchers.withId(realEditText.id))

        // When
        view.perform(ViewActions.replaceText("MM/DD/YYYY"))

        // Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_DATE_ERROR),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.isValid()
        )
    }

    @SmallTest
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

    @SmallTest
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

    @SmallTest
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

    @SmallTest
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

    @SmallTest
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

    @SmallTest
    @Test
    fun shouldReturnErrorIfDateBeforeMinDateIsEntered() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        val todayCalendar = Calendar.getInstance()
        val todayDate = todayCalendar.time
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        field?.setIsRequired(true)
        field?.setLowerLimit(todayDate.time)
        val realEditText = field!!.textInputLayout!!.editText!!
        val view = Espresso.onView(ViewMatchers.withId(realEditText.id))

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

    @SmallTest
    @Test
    fun shouldBeValidIfDateAfterMinDateIsEntered() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
        restartActivity()

        // Given
        val todayCalendar = Calendar.getInstance()
        val todayDate = todayCalendar.time
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        field?.setIsRequired(true)
        field?.setLowerLimit(todayDate.time)
        val realEditText = field!!.textInputLayout!!.editText!!
        val view = Espresso.onView(ViewMatchers.withId(realEditText.id))

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

    @SmallTest
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
            field?.text = "03/03/2019"
            field2?.text = "02/03/2019"
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

    @SmallTest
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
            field?.text = "03/03/2019"
            field2?.text = "03/03/2019"
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

    @SmallTest
    @Test
    fun shouldReturnErrorIfDateAfterMaxDateIsEntered() {
        restartActivity()
        MockActivity.layout = R.layout.activity_edittextdatefield_test

        // Given
        val todayCalendar = Calendar.getInstance()
        val todayDate = todayCalendar.time
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        field?.setIsRequired(true)
        field?.setUpperLimit(todayDate.time)
        val realEditText = field!!.textInputLayout!!.editText!!
        val view = onView(withId(realEditText.id))

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

    @SmallTest
    @Test
    fun shouldBeValidIfDateBeforeMaxDateIsEntered() {
        restartActivity()
        MockActivity.layout = R.layout.activity_edittextdatefield_test

        // Given
        val todayCalendar = Calendar.getInstance()
        val todayDate = todayCalendar.time
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        field!!.setIsRequired(true)
        field.setUpperLimit(todayDate.time)
        val realEditText = field.textInputLayout!!.editText!!
        val view = onView(withId(realEditText.id))

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

    @SmallTest
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
            field?.text = "03/03/2019"
            field2?.text = "03/05/2019"
            field3?.text = "03/04/2019"
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

    @SmallTest
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
            field?.text = "03/03/2019"
            field2?.text = "03/05/2019"
            field3?.text = "03/02/2019"
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

    @SmallTest
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
            field?.text = "03/03/2019"
            field2?.text = "03/05/2019"
            field3?.text = "03/06/2019"
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

    @SmallTest
    @Test
    fun shouldDisable() {
        MockActivity.layout = R.layout.activity_edittextdatefield_disable_enable_test
        restartActivity()

        // Given
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        ruleActivity.activity.runOnUiThread { field?.setEnable(false) }

        val view = onView(withId(R.id.tlDate))

        // When
        runBlocking {
            view.perform(click())
        }

        // Then
        isTextNotDisplayed("S")
        isTextNotDisplayed("M")
        isTextNotDisplayed("T")
        isTextNotDisplayed("W")
        isTextNotDisplayed("F")
    }

    @SmallTest
    @Test
    fun shouldEnable() {
        MockActivity.layout = R.layout.activity_edittextdatefield_disable_enable_test
        restartActivity()

        // Given
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)
        ruleActivity.activity.runOnUiThread { field?.setEnable(false) }

        val view = onView(withId(R.id.tlDate))

        // When
        runBlocking {
            view.perform(click())
        }
        isTextNotDisplayed("S")
        isTextNotDisplayed("M")
        isTextNotDisplayed("T")
        isTextNotDisplayed("W")
        isTextNotDisplayed("F")

        ruleActivity.activity.runOnUiThread { field?.setEnable(true) }

        runBlocking {
            view.perform(click())
        }

        // then
        isTextNotDisplayed("S")
        isTextNotDisplayed("M")
        isTextNotDisplayed("T")
        isTextNotDisplayed("W")
        isTextNotDisplayed("F")
    }

    @SmallTest
    @Test
    fun shouldBeValidWhenFieldIsNotRequiredAndDateIsCorrect() {
        // Given
        MockActivity.layout = R.layout.activity_edittextdatefield_disable_enable_test
        restartActivity()
        val field = (ruleActivity.activity.findViewById<EditTextDateField>(R.id.tlDate))

        // when
        ruleActivity.activity.runOnUiThread {
            field?.isRequired = false
            field?.text = "03/03/2012"

            // Then
            Assert.assertTrue("The EditTextDateField should be valid when the field is not required and date is correct", field?.isValid()?.isValid ?: false)
        }
    }

    @SmallTest
    @Test
    fun shouldBeInvalidWhenFieldIsNotRequiredAndDateIsWrong() {
        // Given
        MockActivity.layout = R.layout.activity_edittextdatefield_disable_enable_test
        restartActivity()
        val field = (ruleActivity.activity.findViewById<EditTextDateField>(R.id.tlDate))

        // when
        ruleActivity.activity.runOnUiThread {
            field?.isRequired = false
            field?.text = "03/03/202"

            // Then
            Assert.assertFalse("the EditTextDateField should be invalid when the field is not required and date is wrong", field?.isValid()?.isValid ?: true)
        }
    }

    @SmallTest
    @Test
    fun shouldBeInvalidWhenFieldIsNotRequiredAndDateIsGreaterThanUpperLimit() {
        // Given
        MockActivity.layout = R.layout.activity_edittextdatefield_disable_enable_test
        restartActivity()
        val field = (ruleActivity.activity.findViewById<EditTextDateField>(R.id.tlDate))

        // when
        ruleActivity.activity.runOnUiThread {
            field?.isRequired = false
            val format = mDefaultDateFormat
            val dateToParse = "02/25/2019"
            field?.setUpperLimit(dateToParse, format)
            field?.text = "03/11/2020"
            // Then
            Assert.assertFalse("the EditTextDateField should be invalid when the field is not required and date is greater than upper limit", field?.isValid()?.isValid ?: true)
        }
    }

    @SmallTest
    @Test
    fun shouldBeInvalidWhenFieldIsNotRequiredAndDateIsLessThanLowerLimit() {
        // Given
        MockActivity.layout = R.layout.activity_edittextdatefield_disable_enable_test
        restartActivity()
        val field = (ruleActivity.activity.findViewById<EditTextDateField>(R.id.tlDate))

        // when
        ruleActivity.activity.runOnUiThread {
            field?.isRequired = false
            val format = mDefaultDateFormat
            val dateToParse = "02/25/2019"
            field?.setLowerLimit(dateToParse, format)
            field?.text = "03/11/2012"
            // Then
            Assert.assertFalse("the EditTextDateField should be invalid when the field is not required and date is less than lower limit", field?.isValid()?.isValid ?: true)
        }
    }

    @SmallTest
    @Test
    fun shouldBeValidWhenFieldIsNotRequiredAndDateIsLessThanUpperLimit() {
        // Given
        MockActivity.layout = R.layout.activity_edittextdatefield_disable_enable_test
        restartActivity()
        val field = (ruleActivity.activity.findViewById<EditTextDateField>(R.id.tlDate))

        // when
        ruleActivity.activity.runOnUiThread {
            field?.isRequired = false
            val format = mDefaultDateFormat
            val dateToParse = "02/25/2019"
            field?.setUpperLimit(dateToParse, format)
            field?.text = "03/11/2018"

            // Then
            Assert.assertTrue("the EditTextDateField should be valid when the field is not required and date is less than upper limit", field?.isValid()?.isValid ?: false)
        }
    }

    @SmallTest
    @Test
    fun shouldBeValidWhenFieldIsNotRequiredAndDateIsGreaterThanLowerLimit() {
        // Given
        MockActivity.layout = R.layout.activity_edittextdatefield_disable_enable_test
        restartActivity()
        val field = (ruleActivity.activity.findViewById<EditTextDateField>(R.id.tlDate))

        // when
        ruleActivity.activity.runOnUiThread {
            field?.isRequired = false
            val format = mDefaultDateFormat
            val dateToParse = "02/25/2019"
            field?.setLowerLimit(dateToParse, format)
            field?.text = "03/11/2020"

            // Then
            Assert.assertTrue("the EditTextDateField should be valid when the field is not required and date is greater than lower limit", field?.isValid()?.isValid ?: false)
        }
    }
}
