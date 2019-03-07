package co.condorlabs.customcomponents.test

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.widget.EditText
import co.condorlabs.customcomponents.customcheckbox.CheckboxFormField
import co.condorlabs.customcomponents.customedittext.*
import co.condorlabs.customcomponents.customradiogroup.RadioGroupFormField
import co.condorlabs.customcomponents.customspinner.SpinnerData
import co.condorlabs.customcomponents.customspinner.SpinnerFormField
import co.condorlabs.customcomponents.formfield.Selectable
import co.condorlabs.customcomponents.test.util.clickWithId
import co.condorlabs.customcomponents.test.util.clickWithText
import co.condorlabs.customcomponents.test.util.isTextDisplayed
import org.junit.Test

class SelfValidationTest : MockActivityTest() {

    @Test
    fun shouldEvaluateItSelfWhenLoseFocusCheckBox() {
        MockActivity.layout = R.layout.selft_validate_check_box_test
        restartActivity()

        //Given
        val formFields = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilCheckbox)
        formFields.setIsRequired(true)
        ruleActivity.runOnUiThread {
            formFields.setSelectables(
                arrayListOf(
                    Selectable("Item 1", false),
                    Selectable("Item 2", false),
                    Selectable("Item 3", false),
                    Selectable("Item 4", false)
                )
            )
        }

        clickWithText("Item 1")
        clickWithText("Item 2")
        clickWithText("Item 3")
        clickWithText("Item 3")
        clickWithText("Item 2")
        clickWithText("Item 1")

        val etNextFocus = ruleActivity.activity.findViewById<EditText>(R.id.etNextFocus)

        //When
        ruleActivity.runOnUiThread {
            etNextFocus.requestFocus()
            formFields.requestFocus()
            etNextFocus.requestFocus()
        }

        //Then
        //isTextDisplayed(formFields.isValid().error)

    }

    @Test
    fun shouldEvaluateItSelfWhenLoseFocusRadioGroup() {
        MockActivity.layout = R.layout.selft_validate_radio_group_test
        restartActivity()

        restartActivity()

        //Given
        val formFields = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tilCheckbox)
        formFields.setIsRequired(true)
        ruleActivity.runOnUiThread {
            formFields.setSelectables(
                arrayListOf(
                    Selectable("Item 1", false),
                    Selectable("Item 2", false),
                    Selectable("Item 3", false),
                    Selectable("Item 4", false)
                )
            )
        }

        clickWithText("Item 1")
        val etNextFocus = ruleActivity.activity.findViewById<EditText>(R.id.etNextFocus)

        //When
        etNextFocus.requestFocus()
        formFields.requestFocus()
        etNextFocus.requestFocus()

        //Then
        isTextDisplayed(formFields.isValid().error)

    }


    @Test
    fun shouldEvaluateItSelfWhenLoseFocusEditTextPhone() {
        MockActivity.layout = R.layout.selft_validate_edit_text_phone_test
        restartActivity()

        //Given
        val required = ruleActivity.activity.findViewById<EditTextPhoneField>(R.id.required)
        val etNextFocus = ruleActivity.activity.findViewById<EditTextPhoneField>(R.id.losefocus)

        //When
        ruleActivity.runOnUiThread {
            etNextFocus.requestFocus()
            required.requestFocus()
            etNextFocus.requestFocus()
        }

        //Then
        isTextDisplayed(required.isValid().error)

    }

    @Test
    fun shouldEvaluateItSelfWhenLoseFocusEditTextEmail() {
        MockActivity.layout = R.layout.selft_validate_edit_text_email_test
        restartActivity()

        //Given
        val required = ruleActivity.activity.findViewById<EditTextEmailField>(R.id.required)
        val etNextFocus = ruleActivity.activity.findViewById<EditTextEmailField>(R.id.losefocus)

        //When
        ruleActivity.runOnUiThread {
            etNextFocus.requestFocus()
            required.requestFocus()
            etNextFocus.requestFocus()
        }

        //Then
        isTextDisplayed(required.isValid().error)

    }

    @Test
    fun shouldEvaluateItSelfWhenLoseFocusEditTextDate() {
        MockActivity.layout = R.layout.selft_validate_edit_text_date_test
        restartActivity()

        //Given
        val required = ruleActivity.activity.findViewById<EditTextDateField>(R.id.required)
        val etNextFocus = ruleActivity.activity.findViewById<EditTextDateField>(R.id.losefocus)

        //When
        ruleActivity.runOnUiThread {
            etNextFocus.requestFocus()
            required.requestFocus()
            etNextFocus.requestFocus()
        }

        //Then
        isTextDisplayed(required.isValid().error)

    }

    @Test
    fun shouldEvaluateItSelfWhenLoseFocusEditTextCurrency() {
        MockActivity.layout = R.layout.selft_validate_edit_text_currency_test
        restartActivity()

        //Given
        val required = ruleActivity.activity.findViewById<EditTextCurrencyField>(R.id.required)
        val etNextFocus = ruleActivity.activity.findViewById<EditTextCurrencyField>(R.id.losefocus)

        //When
        ruleActivity.runOnUiThread {
            etNextFocus.requestFocus()
            required.requestFocus()
            etNextFocus.requestFocus()
        }

        //Then
        isTextDisplayed(required.isValid().error)

    }

    @Test
    fun shouldEvaluateItSelfWhenLoseFocusEditTextCity() {
        MockActivity.layout = R.layout.selft_validate_edit_text_city_test
        restartActivity()

        //Given
        val required = ruleActivity.activity.findViewById<EditTextCityField>(R.id.required)
        val etNextFocus = ruleActivity.activity.findViewById<EditTextCityField>(R.id.losefocus)

        //When
        ruleActivity.runOnUiThread {
            etNextFocus.requestFocus()
            required.requestFocus()
            etNextFocus.requestFocus()
        }

        //Then
        isTextDisplayed(required.isValid().error)

    }

    @Test
    fun shouldEvaluateItSelfWhenLoseFocusSpinner() {
        MockActivity.layout = R.layout.selft_validate_spinner_test
        restartActivity()

        //Given
        val etNextFocus = ruleActivity.activity.findViewById<EditText>(R.id.etNextFocus)
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val view = Espresso.onView(ViewMatchers.withId(R.id.spState))
        val data = SpinnerData("1", "Antioquia")
        val data1 = SpinnerData("2", "Cundinamarca")
        val data3 = SpinnerData("3", "Atlantico")

        //When
        ruleActivity.runOnUiThread {
            formField.setData(arrayListOf(data, data1, data3))
        }

        view.perform(ViewActions.click())
        Espresso.pressBack()

        ruleActivity.runOnUiThread {
            formField.requestFocus()
            etNextFocus.requestFocus()
        }

        //Then
        isTextDisplayed(formField.isValid().error)
    }
}