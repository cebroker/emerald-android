package co.condorlabs.customcomponents

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import co.condorlabs.customcomponents.customcheckbox.CheckboxFormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.EMPTY
import co.condorlabs.customcomponents.helper.VALIDATE_CHECKBOX_NO_SELECTION_ERROR
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CheckBoxFieldTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_basechecbox_test
    }


    @Test
    fun shouldShowMessageIfNoSelectedWhenIsRequired() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilChecbox)

        formField.setIsRequired(true)
        //When
        val result = formField.isValid()

        //Then
        Assert.assertEquals(
                ValidationResult(false, VALIDATE_CHECKBOX_NO_SELECTION_ERROR), result
        )
    }

    @Test
    fun shouldShowMessageInLabelIfNoSelected() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilChecbox)

        formField.setIsRequired(true)
        //When
        formField?.let {
            showErrorInInputLayout(it, it.isValid().error)
        }

        //Then
        ViewMatchers.hasErrorText(VALIDATE_CHECKBOX_NO_SELECTION_ERROR).matches(formField.getChildAt(0))
    }

    @Test
    fun shouldValidateIfSelected() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilChecbox)

        formField.setIsRequired(true)
        //When
        Espresso.onView(ViewMatchers.withSubstring("Item 1"))
                .perform(ViewActions.click())
        val result = formField.isValid()

        //Then
        Assert.assertEquals(
                ValidationResult(true, EMPTY), result
        )
    }


    @Test
    fun shouldReturnTrueIfNoSelected() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilChecbox)

        formField.setIsRequired(false)
        //When
        val result = formField.isValid()

        //Then
        Assert.assertEquals(
                ValidationResult(true, EMPTY), result
        )
    }

    @Test
    fun shouldDisplayTitle() {
        restartActivity()

        //Given
        val view = Espresso.onView(ViewMatchers.withText("Custom check"))

        //When
        view.perform(ViewActions.click())

        //Then
        view.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}