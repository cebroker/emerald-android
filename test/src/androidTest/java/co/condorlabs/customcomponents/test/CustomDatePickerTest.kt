package co.condorlabs.customcomponents.test

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import co.condorlabs.customcomponents.customedittext.EditTextMonthYearField
import co.condorlabs.customcomponents.helper.DATE_PICKER_MAX_YEAR
import co.condorlabs.customcomponents.helper.DATE_PICKER_MIN_YEAR
import co.condorlabs.customcomponents.test.util.text
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class CustomDatePickerTest : MockActivityTest() {

    private var editTextRef = ViewMatchers.withId(R.id.etMonthYear)
    private var editText: EditTextMonthYearField? = null

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_customdatepicker_test
        restartActivity()
        editText = ruleActivity.activity.findViewById<View>(R.id.etMonthYearField) as? EditTextMonthYearField
    }

    @Test
    fun shouldFormatOnTextChanged() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("0"))
        // Then
        Assert.assertEquals("0M/YYYY", editText?.text())
        // When
        onView(editTextRef).perform(ViewActions.typeText("8"))
        // Then
        Assert.assertEquals("08/YYYY", editText?.text())
        // When
        onView(editTextRef).perform(ViewActions.typeText("2"))
        // Then
        Assert.assertEquals("08/2YYY", editText?.text())
        // When
        onView(editTextRef).perform(ViewActions.typeText("0"))
        // Then
        Assert.assertEquals("08/20YY", editText?.text())
        // When
        onView(editTextRef).perform(ViewActions.typeText("0"))
        // Then
        Assert.assertEquals("08/200Y", editText?.text())
        // When
        onView(editTextRef).perform(ViewActions.typeText("8"))
        // Then
        Assert.assertEquals("08/2008", editText?.text())

    }

    @Test
    fun shouldNotAllowMonthGreaterThan12() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("99/2001"))
        // Then
        Assert.assertEquals("12/2001", editText?.text())
    }

    @Test
    fun shouldNotAllowMonthEqualsToZero() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("00/2001"))
        // Then
        Assert.assertEquals("01/2001", editText?.text())
    }

    @Test
    fun shouldNotAllowYearLessThanMinYear() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("01/0000"))
        // Then
        Assert.assertEquals("01/$DATE_PICKER_MIN_YEAR", editText?.text())
    }

    @Test
    fun shouldNotAllowYearGreaterThanMaxYear() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("01/5000"))
        // Then
        Assert.assertEquals("01/$DATE_PICKER_MAX_YEAR", editText?.text())
    }

    @Test
    fun shouldNotAllowTypeText() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("a"))
        // Then
        Assert.assertEquals("", editText?.text())
    }

    @Test
    fun shouldReturnIsValid() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("01/2008"))
        // Then
        Assert.assertEquals(true, editText?.isValid()?.isValid)
    }

    @Test
    fun shouldNotAllowMoreThan4DigitsYear() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("01/20080"))
        // Then
        Assert.assertEquals(7, editText?.text()?.length)
        Assert.assertEquals(true, editText?.isValid()?.isValid)
    }

    @Test
    fun shouldNotAllowMoreThan2DigitsMonth() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("021/2008"))
        // Then
        Assert.assertEquals(7, editText?.text()?.length)
        Assert.assertEquals(true, editText?.isValid()?.isValid)
    }

    @Test
    fun shouldReturnIsNotValidIfFieldIsEmptyAndIsRequired() {
        // Given
        editText?.isRequired = true
        // When
        onView(editTextRef).perform(ViewActions.typeText(""))
        // Then
        Assert.assertEquals(false, editText?.isValid()?.isValid)
    }

    @Test
    fun shouldReturnIsValidIfFieldIsEmptyAndIsNotRequired() {
        // Given
        editText?.isRequired = false
        // When
        onView(editTextRef).perform(ViewActions.typeText(""))
        // Then
        Assert.assertEquals(true, editText?.isValid()?.isValid)
    }

    @Test
    fun shouldNotAllowYearGreaterThanUpperLimit() {
        // Given
        editText?.upperLimit = Calendar.getInstance().apply {
            set(2007, Calendar.JULY, 1)
        }
        // When
        onView(editTextRef).perform(ViewActions.typeText("01/2008"))
        // Then
        Assert.assertEquals(false, editText?.isValid()?.isValid)
        Assert.assertEquals("The Enter some text can't be after the 6/2007", editText?.isValid()?.error)

    }

    @Test
    fun shouldNotAllowMonthGreaterThanUpperLimit() {
        // Given
        editText?.upperLimit = Calendar.getInstance().apply {
            set(2007, Calendar.JULY, 1)
        }
        // When
        onView(editTextRef).perform(ViewActions.typeText("11/2007"))
        // Then
        Assert.assertEquals(false, editText?.isValid()?.isValid)
        Assert.assertEquals("The Enter some text can't be after the 6/2007", editText?.isValid()?.error)
    }

    @Test
    fun shouldAllowDateEqualsThanUpperLimit() {
        // Given
        editText?.upperLimit = Calendar.getInstance().apply {
            set(2007, Calendar.JULY, 1)
        }
        // When
        onView(editTextRef).perform(ViewActions.typeText("07/2007"))
        // Then
        Assert.assertEquals(true, editText?.isValid()?.isValid)
    }

    @Test
    fun shouldAllowDateLessThanUpperLimit() {
        // Given
        editText?.upperLimit = Calendar.getInstance().apply {
            set(2007, Calendar.JULY, 1)
        }
        // When
        onView(editTextRef).perform(ViewActions.typeText("12/2006"))
        // Then
        Assert.assertEquals(true, editText?.isValid()?.isValid)
    }
}
