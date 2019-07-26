package co.condorlabs.customcomponents.test

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.SmallTest
import co.condorlabs.customcomponents.DATE_PICKER_MAX_YEAR
import co.condorlabs.customcomponents.DATE_PICKER_MIN_YEAR
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.PropertyNotImplementedException
import co.condorlabs.customcomponents.customedittext.EditTextMonthYearField
import co.condorlabs.customcomponents.test.util.clickDrawable
import co.condorlabs.customcomponents.test.util.setNumberPickerValue
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

class CustomDatePickerTest : MockActivityTest() {

    private var dpMonthRef = withId(R.id.monthPicker)
    private var dpYearRef = withId(R.id.yearPicker)
    private var formField: EditTextMonthYearField? = null

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_customdatepicker_test
        restartActivity()
        formField = ruleActivity.activity.findViewById<View>(R.id.etMonthYearField) as? EditTextMonthYearField
    }

    @SmallTest
    @Test
    fun shouldFormatOnTextChanged() {
        //Given
        val view = onView(withId(formField!!.editText!!.id))

        // When
        view.perform(ViewActions.typeText("0"))
        // Then
        Assert.assertEquals("0M/YYYY", formField!!.editText!!.text.toString())
        // When
        view.perform(ViewActions.typeText("8"))
        // Then
        Assert.assertEquals("08/YYYY", formField!!.editText!!.text.toString())
        // When
        view.perform(ViewActions.typeText("2"))
        // Then
        Assert.assertEquals("08/2YYY", formField!!.editText!!.text.toString())
        // When
        view.perform(ViewActions.typeText("0"))
        // Then
        Assert.assertEquals("08/20YY", formField!!.editText!!.text.toString())
        // When
        view.perform(ViewActions.typeText("0"))
        // Then
        Assert.assertEquals("08/200Y", formField!!.editText!!.text.toString())
        // When
        view.perform(ViewActions.typeText("8"))
        // Then
        Assert.assertEquals("08/2008", formField!!.editText!!.text.toString())
    }

    @SmallTest
    @Test
    fun shouldNotAllowMonthGreaterThan12() {
        //Given
        val view = onView(withId(formField!!.editText!!.id))

        // When
        view.perform(ViewActions.typeText("99/2001"))
        // Then
        Assert.assertEquals("12/2001", formField!!.editText!!.text.toString())
    }


    @SmallTest
    @Test
    fun shouldNotAllowMonthEqualsToZero() {
        //Given
        val view = onView(withId(formField!!.editText!!.id))
        // When
        view.perform(ViewActions.typeText("00/2001"))
        // Then
        Assert.assertEquals("01/2001", formField!!.editText!!.text.toString())
    }


    @SmallTest
    @Test
    fun shouldNotAllowYearLessThanMinYear() {
        //Given
        val view = onView(withId(formField!!.editText!!.id))
        // When
        view.perform(ViewActions.typeText("01/0000"))
        // Then
        Assert.assertEquals("01/$DATE_PICKER_MIN_YEAR", formField!!.editText!!.text.toString())
    }


    @SmallTest
    @Test
    fun shouldNotAllowYearGreaterThanMaxYear() {
        //Given
        val view = onView(withId(formField!!.editText!!.id))
        // When
        view.perform(ViewActions.typeText("01/5000"))
        // Then
        Assert.assertEquals("01/$DATE_PICKER_MAX_YEAR", formField!!.editText!!.text.toString())
    }

    @SmallTest
    @Test
    fun shouldNotAllowTypeText() {
        //Given
        val view = onView(withId(formField!!.editText!!.id))
        // When
        view.perform(ViewActions.typeText("a"))
        // Then
        Assert.assertEquals("", formField!!.editText!!.text.toString())
    }

    @SmallTest
    @Test
    fun shouldReturnIsValid() {
        //Given
        val view = onView(withId(formField!!.editText!!.id))
        // When
        view.perform(ViewActions.typeText("01/2008"))
        // Then
        Assert.assertEquals(true, formField!!.isValid().isValid)
    }

    @SmallTest
    @Test
    fun shouldNotAllowMoreThan4DigitsYear() {
        //Given
        val view = onView(withId(formField!!.editText!!.id))
        // When
        view.perform(ViewActions.typeText("01/20080"))
        // Then
        Assert.assertEquals(7, formField!!.editText!!.text.length)
        Assert.assertEquals(true, formField!!.isValid().isValid)
    }


    @SmallTest
    @Test
    fun shouldNotAllowMoreThan2DigitsMonth() {
        //Given
        val view = onView(withId(formField!!.editText!!.id))
        // When
        view.perform(ViewActions.typeText("021/2008"))
        // Then
        Assert.assertEquals(7, formField!!.editText!!.text.length)
        Assert.assertEquals(true, formField!!.isValid().isValid)
    }

    @SmallTest
    @Test
    fun shouldReturnIsNotValidIfFieldIsEmptyAndIsRequired() {
        //Given
        val view = onView(withId(formField!!.editText!!.id))
        // When
        view.perform(ViewActions.typeText(""))
        // Then
        Assert.assertEquals(false, formField!!.isValid().isValid)
    }

    @Test
    fun shouldReturnIsValidIfFieldIsEmptyAndIsNotRequired() {
        // Given
        val view = onView(withId(formField!!.editText!!.id))
        formField?.isRequired = false
        // When
        view.perform(ViewActions.typeText(""))
        // Then
        Assert.assertEquals(true, formField!!.isValid().isValid)
    }

    @Test
    fun shouldNotAllowYearGreaterThanUpperLimit() {
        // Given
        val view = onView(withId(formField!!.editText!!.id))
        formField!!.upperLimit = Calendar.getInstance().apply {
            set(2007, Calendar.JULY, 1)
        }
        // When
        view.perform(ViewActions.typeText("01/2008"))
        // Then
        val result = formField!!.isValid()
        Assert.assertEquals(false, result.isValid)
        Assert.assertEquals("The Enter some text can't be after 7/2007", result.error)
    }

    @Test
    fun shouldNotAllowMonthGreaterThanUpperLimit() {
        // Given
        val view = onView(withId(formField!!.editText!!.id))
        formField!!.upperLimit = Calendar.getInstance().apply {
            set(2007, Calendar.JULY, 1)
        }
        // When
        view.perform(ViewActions.typeText("11/2007"))
        // Then
        val result = formField!!.isValid()
        Assert.assertEquals(false, result.isValid)
        Assert.assertEquals("The Enter some text can't be after 7/2007", result.error)
    }


    @Test
    fun shouldAllowDateEqualsThanUpperLimit() {
        // Given
        val view = onView(withId(formField!!.editText!!.id))
        formField!!.upperLimit = Calendar.getInstance().apply {
            set(2007, Calendar.JULY, 1)
        }
        // When
        view.perform(ViewActions.typeText("07/2007"))
        // Then
        Assert.assertEquals(true, formField!!.isValid().isValid)
    }

    @Test
    fun shouldAllowDateLessThanUpperLimit() {
        // Given
        val view = onView(withId(formField!!.editText!!.id))
        formField!!.upperLimit = Calendar.getInstance().apply {
            set(2007, Calendar.JULY, 1)
        }
        // When
        view.perform(ViewActions.typeText("12/2006"))
        // Then
        Assert.assertEquals(true, formField!!.isValid().isValid)
    }

    @Test
    fun shouldGetRightMonthAndYear() {
        // Given
        val view = onView(withId(formField!!.editText!!.id))
        // When
        view.perform(ViewActions.typeText("12/2006"))
        // Then
        Assert.assertEquals(11, formField!!.getMonth())
        Assert.assertEquals(2006, formField!!.getYear())
    }

    @Test
    fun shouldOpenDialog() {
        // Given
        val view = onView(withId(formField!!.editText!!.id))
        // When
        view.perform(clickDrawable())
        onView(dpMonthRef).perform(setNumberPickerValue(11))
        onView(dpYearRef).perform(setNumberPickerValue(2008))
        onView(withSubstring("OK"))
            .check(ViewAssertions.matches(isDisplayed()))
            .perform(ViewActions.click())

        // Then
        Assert.assertEquals(11, formField!!.getMonth())
        Assert.assertEquals(2008, formField!!.getYear())
    }

    @Test
    fun shouldAllowDateWithoutLimits() {
        // Given
        val view = onView(withId(formField!!.editText!!.id))
        formField!!.isRequired = true
        // When
        view.perform(ViewActions.typeText("12/2021"))
        // Then
        val result = formField!!.isValid()
        Assert.assertEquals(true, result.isValid)
        Assert.assertEquals(EMPTY, result.error)
    }

    @Test
    fun shouldAllowDateEqualsThanLowerLimit() {
        // Given
        val view = onView(withId(formField!!.editText!!.id))
        formField!!.lowerLimit = Calendar.getInstance().apply {
            set(2019, Calendar.JUNE, 1)
        }
        // When
        view.perform(ViewActions.typeText("06/2019"))
        // Then
        Assert.assertEquals(true, formField!!.isValid().isValid)
    }

    @Test
    fun shouldNotAllowDateLessThanLowerLimit() {
        // Given
        val view = onView(withId(formField!!.editText!!.id))
        formField!!.lowerLimit = Calendar.getInstance().apply {
            set(2019, Calendar.JUNE, 1)
        }
        // When
        view.perform(ViewActions.typeText("05/2019"))
        // Then
        Assert.assertEquals(false, formField!!.isValid().isValid)
    }

    @Test
    fun shouldAllowDateGreaterThanLowerLimit() {
        // Given
        val view = onView(withId(formField!!.editText!!.id))
        formField!!.lowerLimit = Calendar.getInstance().apply {
            set(2019, Calendar.JUNE, 1)
        }
        // When
        view.perform(ViewActions.typeText("05/2021"))
        // Then
        Assert.assertEquals(true, formField!!.isValid().isValid)
    }

    @Test(expected = PropertyNotImplementedException::class)
    fun shouldThrowPropertyNotImplementedExceptionWhenHasUpperLimitAndLowerLimit() {
        // Given
        val view = onView(withId(formField!!.editText!!.id))
        formField!!.lowerLimit = Calendar.getInstance().apply {
            set(2019, Calendar.JUNE, 1)
        }
        formField!!.upperLimit = Calendar.getInstance().apply {
            set(2020, Calendar.JUNE, 1)
        }
        // When
        view.perform(ViewActions.typeText("05/2021"))

        // Then
        ruleActivity.runOnUiThread {
            formField!!.isValid()
        }
    }
}
