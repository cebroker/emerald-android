package co.condorlabs.customcomponents

import co.condorlabs.customcomponents.customcheckbox.CheckboxFormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.EMPTY
import co.condorlabs.customcomponents.helper.VALIDATE_CHECKBOX_NO_SELECTION_ERROR
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class ChechBoxFieldTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_baseradiogroup_test
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

}