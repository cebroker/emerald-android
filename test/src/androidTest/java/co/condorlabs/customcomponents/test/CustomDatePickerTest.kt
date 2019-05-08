package co.condorlabs.customcomponents.test


import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import co.condorlabs.customcomponents.MAX_YEAR
import co.condorlabs.customcomponents.MIN_YEAR
import co.condorlabs.customcomponents.customedittext.EditTextMonthYearField
import co.condorlabs.customcomponents.test.util.text
import org.junit.Assert
import org.junit.Before
import org.junit.Test

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
        Assert.assertEquals("01/$MIN_YEAR", editText?.text())
    }

    @Test
    fun shouldNotAllowYearGreaterThanMaxYear() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("01/5000"))
        // Then
        Assert.assertEquals("01/$MAX_YEAR", editText?.text())
    }

    @Test
    fun shouldNotAllowTypeText() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("a"))
        // Then
        Assert.assertEquals("", editText?.text())
    }
}
