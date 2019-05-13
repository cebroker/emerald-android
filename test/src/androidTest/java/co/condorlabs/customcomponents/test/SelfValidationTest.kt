package co.condorlabs.customcomponents.test

import co.condorlabs.customcomponents.customedittext.*
import co.condorlabs.customcomponents.customspinner.SpinnerData
import co.condorlabs.customcomponents.customspinner.SpinnerFormField
import co.condorlabs.customcomponents.test.util.isTextDisplayed
import org.junit.Ignore
import org.junit.Test

@Ignore
class SelfValidationTest : MockActivityTest() {

    @Test
    fun shouldEvaluateItSelfWhenLoseFocusEditTextPhone() {
        MockActivity.layout = R.layout.selft_validate_edit_text_phone_test
        restartActivity()

        // Given
        val required = ruleActivity.activity.findViewById<EditTextPhoneField>(R.id.required)
        val etNextFocus = ruleActivity.activity.findViewById<EditTextPhoneField>(R.id.losefocus)

        // When
        ruleActivity.runOnUiThread {
            etNextFocus.requestFocus()
            required.requestFocus()
            etNextFocus.requestFocus()
        }

        // Then
        isTextDisplayed(required.isValid().error)
    }

    @Test
    fun shouldEvaluateItSelfWhenLoseFocusEditTextEmail() {
        MockActivity.layout = R.layout.selft_validate_edit_text_email_test
        restartActivity()

        // Given
        val required = ruleActivity.activity.findViewById<EditTextEmailField>(R.id.required)
        val etNextFocus = ruleActivity.activity.findViewById<EditTextEmailField>(R.id.losefocus)

        // When
        ruleActivity.runOnUiThread {
            etNextFocus.requestFocus()
            required.requestFocus()
            etNextFocus.requestFocus()
        }

        // Then
        isTextDisplayed(required.isValid().error)
    }

    @Test
    fun shouldEvaluateItSelfWhenLoseFocusEditTextDate() {
        MockActivity.layout = R.layout.selft_validate_edit_text_date_test
        restartActivity()

        // Given
        val required = ruleActivity.activity.findViewById<EditTextDateField>(R.id.required)
        val etNextFocus = ruleActivity.activity.findViewById<EditTextDateField>(R.id.losefocus)

        // When
        ruleActivity.runOnUiThread {
            etNextFocus.requestFocus()
            required.requestFocus()
            etNextFocus.requestFocus()
        }

        // Then
        isTextDisplayed(required.isValid().error)
    }

    @Test
    fun shouldEvaluateItSelfWhenLoseFocusEditTextCurrency() {
        MockActivity.layout = R.layout.selft_validate_edit_text_currency_test
        restartActivity()

        // Given
        val required = ruleActivity.activity.findViewById<EditTextCurrencyField>(R.id.required)
        val etNextFocus = ruleActivity.activity.findViewById<EditTextCurrencyField>(R.id.losefocus)

        // When
        ruleActivity.runOnUiThread {
            etNextFocus.requestFocus()
            required.requestFocus()
            etNextFocus.requestFocus()
        }

        // Then
        isTextDisplayed(required.isValid().error)
    }

    @Test
    fun shouldEvaluateItSelfWhenLoseFocusEditTextCity() {
        MockActivity.layout = R.layout.selft_validate_edit_text_city_test
        restartActivity()

        // Given
        val required = ruleActivity.activity.findViewById<EditTextCityField>(R.id.required)
        val etNextFocus = ruleActivity.activity.findViewById<EditTextCityField>(R.id.losefocus)

        // When
        ruleActivity.runOnUiThread {
            etNextFocus.requestFocus()
            required.requestFocus()
            etNextFocus.requestFocus()
        }

        // Then
        isTextDisplayed(required.isValid().error)
    }

    @Test
    fun shouldEvaluateItSelfWhenLoseFocusSpinner() {
        MockActivity.layout = R.layout.selft_validate_spinner_test
        restartActivity()
        val etNextFocus = ruleActivity.activity.findViewById<EditTextCityField>(R.id.losefocus)

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val data = SpinnerData("1", "Antioquia")
        val data1 = SpinnerData("2", "Cundinamarca")
        val data3 = SpinnerData("3", "Atlantico")

        // When
        ruleActivity.runOnUiThread {
            formField.setData(arrayListOf(data, data1, data3))
            formField.setItemSelectedById("1")
            etNextFocus.requestFocus()
            formField.requestFocus()
            etNextFocus.requestFocus()
            formField.requestFocus()
        }
        isTextDisplayed(formField.isValid().error)
    }
}
