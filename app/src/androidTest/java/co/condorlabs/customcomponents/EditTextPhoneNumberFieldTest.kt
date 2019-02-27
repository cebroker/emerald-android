package co.condorlabs.customcomponents

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import co.condorlabs.customcomponents.customedittext.EditTextPhoneField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.PHONE_NUMBER_REGEX
import co.condorlabs.customcomponents.helper.VALIDATE_EMPTY_ERROR
import co.condorlabs.customcomponents.helper.VALIDATE_LENGTH_ERROR
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Oscar Gallon on 2/25/19.
 */
class EditTextPhoneNumberFieldTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_phoneedittext_test
    }

    @Test
    fun shouldHaveDefaultHint() {
        restartActivity()

        //Given
        val view = Espresso.onView(ViewMatchers.withId(R.id.tlPhone))

        //When
        view.perform(ViewActions.click())

        //Then
        ViewMatchers.withHint("Phone").matches(view)
    }

    @Test
    fun shouldFormatPhoneNumber() {
        restartActivity()

        //Given
        val view = Espresso.onView(ViewMatchers.withId(R.id.etPhone))

        //When
        view.perform(ViewActions.typeText("1234567890"))

        //Then
        view.check(ViewAssertions.matches(ViewMatchers.withText("123-456-7890")))

    }

    @Test
    fun shouldShowAndErrorWithEmptyText() {
        restartActivity()

        //Given
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlPhone) as? EditTextPhoneField)
        val resultIsValid: ValidationResult?
        txtInputLayout?.setIsRequired(true)

        //When
        resultIsValid = txtInputLayout?.isValid()

        //Then
        txtInputLayout?.setRegex(PHONE_NUMBER_REGEX)
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_EMPTY_ERROR), resultIsValid

        )
    }

    @Test
    fun shouldShowAndErrorWithLessDigits() {
        restartActivity()
        //Given
        val phone = "123456"
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlPhone) as? EditTextPhoneField)
        txtInputLayout?.setIsRequired(true)

        //When
        txtInputLayout?.setRegex(PHONE_NUMBER_REGEX)
        Espresso.onView(ViewMatchers.withId(R.id.etPhone)).perform(ViewActions.typeText(phone))

        //Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_LENGTH_ERROR),
            txtInputLayout?.isValid()
        )
    }
}
