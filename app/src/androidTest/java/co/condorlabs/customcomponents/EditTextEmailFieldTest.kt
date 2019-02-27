package co.condorlabs.customcomponents

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.runner.AndroidJUnit4
import android.view.View
import co.condorlabs.customcomponents.customedittext.EditTextEmailField
import co.condorlabs.customcomponents.formfield.FormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.EMPTY
import co.condorlabs.customcomponents.helper.VALIDATE_EMAIL_ERROR
import co.condorlabs.customcomponents.helper.VALIDATE_EMPTY_ERROR
import org.junit.runner.RunWith
import org.junit.*

/**
 * @author Oscar Gallon on 2/25/19.
 */
@RunWith(AndroidJUnit4::class)
class EditTextEmailFieldTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_emailtestfield_test
    }

    @Test
    fun shouldHaveDefaultHint() {
        restartActivity()

        //Given
        val view = Espresso.onView(ViewMatchers.withId(R.id.tlEmail))

        //When
        view.perform(ViewActions.click())

        //Then
        ViewMatchers.withHint("Email").matches(view)
    }


    @Test
    fun shouldShowAndErrorWithEmptyEmail() {
        restartActivity()

        //Given
        (ruleActivity.activity.findViewById<View>(R.id.tlEmail) as? EditTextEmailField)?.setIsRequired(true)

        //When
        val result = (ruleActivity.activity.findViewById<View>(R.id.tlEmail) as? FormField)?.isValid()

        //Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_EMPTY_ERROR), result
        )
    }

    @Test
    fun shouldShowErrorWitheEmailIncorrectPart1() {
        restartActivity()
        //Given
        val text = "kdfkd"
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlEmail) as? EditTextEmailField)
        txtInputLayout?.setIsRequired(true)

        //When
        txtInputLayout?.setRegex(android.util.Patterns.EMAIL_ADDRESS.toString())
        Espresso.onView(ViewMatchers.withId(R.id.etEmail)).perform(typeText(text))

        //Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_EMAIL_ERROR),
            txtInputLayout?.isValid()
        )
    }

    @Test
    fun shouldShowErrorWitheEmailIncorrectPart2() {
        restartActivity()
        //Given
        val text = "kdfkd@"
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlEmail) as? EditTextEmailField)
        txtInputLayout?.setIsRequired(true)

        //When
        txtInputLayout?.setRegex(android.util.Patterns.EMAIL_ADDRESS.toString())
        Espresso.onView(ViewMatchers.withId(R.id.etEmail)).perform(typeText(text))

        //Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_EMAIL_ERROR),
            txtInputLayout?.isValid()
        )
    }

    @Test
    fun shouldShowErrorWitheEmailIncorrectPart3() {
        restartActivity()
        //Given
        val text = "kdfkd@smdms"
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlEmail) as? EditTextEmailField)
        txtInputLayout?.setIsRequired(true)

        //When
        txtInputLayout?.setRegex(android.util.Patterns.EMAIL_ADDRESS.toString())
        Espresso.onView(ViewMatchers.withId(R.id.etEmail)).perform(typeText(text))

        //Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_EMAIL_ERROR),
            txtInputLayout?.isValid()
        )
    }

    @Test
    fun shouldShowErrorWitheEmailIncorrectPart4() {
        restartActivity()
        //Given
        val text = "kdfkd@smdms."
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlEmail) as? EditTextEmailField)
        txtInputLayout?.setIsRequired(true)

        //When
        txtInputLayout?.setRegex(android.util.Patterns.EMAIL_ADDRESS.toString())
        Espresso.onView(ViewMatchers.withId(R.id.etEmail)).perform(typeText(text))

        //Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_EMAIL_ERROR),
            txtInputLayout?.isValid()
        )
    }

    fun shouldMatch() {
        restartActivity()
        //Given
        val text = "o@gmail.co"
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlEmail) as? EditTextEmailField)
        txtInputLayout?.setIsRequired(true)

        //When
        txtInputLayout?.setRegex(android.util.Patterns.EMAIL_ADDRESS.toString())
        Espresso.onView(ViewMatchers.withId(R.id.etEmail)).perform(typeText(text))

        //Then
        Assert.assertEquals(
            ValidationResult(true, EMPTY),
            txtInputLayout?.isValid()
        )
    }
}
