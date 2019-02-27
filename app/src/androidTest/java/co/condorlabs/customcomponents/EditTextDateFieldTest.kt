package co.condorlabs.customcomponents

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import co.condorlabs.customcomponents.customedittext.EditTextDateField
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.VALIDATE_DATE_ERROR
import co.condorlabs.customcomponents.helper.VALIDATE_EMPTY_ERROR
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

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_edittextdatefield_test
    }

    @Test
    fun shouldShowErrorWitheDateIncorrectPart1() {
        restartActivity()

        //Given
        val view = Espresso.onView(ViewMatchers.withId(R.id.etDate))
        (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.setIsRequired(true)

        //When
        view.perform(ViewActions.typeText("11/DD/2019"))

        //then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_DATE_ERROR),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? FormField)?.isValid()
        )
    }

    @Test
    fun shouldShowErrorWitheDateIncorrectPart2() {
        restartActivity()

        //Given
        val view = Espresso.onView(ViewMatchers.withId(R.id.etDate))
        (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.setIsRequired(true)

        //When
        view.perform(ViewActions.typeText("M1/01/2019"))

        //Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_DATE_ERROR),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? FormField)?.isValid()
        )
    }

    @Test
    fun shouldShowErrorWitheDateIncorrectPart3() {
        restartActivity()

        //Given
        val view = Espresso.onView(ViewMatchers.withId(R.id.etDate))
        (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.setIsRequired(true)

        //When
        view.perform(ViewActions.typeText("12/01/2YY9"))

        //Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_DATE_ERROR),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? FormField)?.isValid()
        )
    }

    @Test
    fun shouldShowErrorWitheDateIncorrectPart4() {
        restartActivity()

        //Given
        val view =
            Espresso.onView(ViewMatchers.withId(R.id.etDate))
        (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.setIsRequired(true)

        //When
        view.perform(ViewActions.replaceText(mDefaultDateFormat))

        //Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_DATE_ERROR),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? FormField)?.isValid()
        )
    }

    @Test
    fun shouldParseValidLowerLimit() {
        restartActivity()

        //Given
        val format = mDefaultDateFormat
        val dateToParse = "02/25/2019"
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)

        //When
        field?.setLowerLimit(dateToParse, format)

        //Then
        Assert.assertEquals(
            SimpleDateFormat(format, Locale.getDefault()).parse(
                dateToParse
            ).time, field?.getLowerLimit()
        )
    }

    @Test
    fun shouldNotParseInvalidLowerLimit() {
        restartActivity()

        //Given
        val format = mDefaultDateFormat
        val dateToParse = "02/AA/2019"
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)

        //When
        field?.setLowerLimit(dateToParse, format)

        //Then
        Assert.assertNull(
            field?.getLowerLimit()
        )
    }

    @Test
    fun shouldParseValidUpperLimit() {
        restartActivity()

        //Given
        val format = mDefaultDateFormat
        val dateToParse = "02/25/2019"
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)

        //When
        field?.setUpperLimit(dateToParse, format)

        //Then
        Assert.assertEquals(
            SimpleDateFormat(format, Locale.getDefault()).parse(
                dateToParse
            ).time, field?.getUpperLimit()
        )
    }

    @Test
    fun shouldNotParseInvalidUpperLimit() {
        restartActivity()

        //Given
        val format = mDefaultDateFormat
        val dateToParse = "02/AA/2019"
        val field = (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)

        //When
        field?.setUpperLimit(dateToParse, format)

        //Then
        Assert.assertNull(
            field?.getUpperLimit()
        )
    }

    @Test
    fun shouldShowAndErrorWithEmptyDate() {
        restartActivity()

        //Given
        (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? EditTextDateField)?.setIsRequired(true)

        //When


        //Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_EMPTY_ERROR),
            (ruleActivity.activity.findViewById<View>(R.id.tlDate) as? FormField)?.isValid()
        )
    }

}
