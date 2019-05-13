/*
 * Copyright 2019 CondorLabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.condorlabs.customcomponents.test

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import android.view.View
import co.condorlabs.customcomponents.customedittext.EditTextPhoneField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.PHONE_NUMBER_REGEX
import co.condorlabs.customcomponents.VALIDATE_EMPTY_ERROR
import co.condorlabs.customcomponents.VALIDATE_LENGTH_ERROR
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

@Ignore
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

        // Given
        val view = Espresso.onView(ViewMatchers.withId(R.id.tlPhone))

        // When
        view.perform(ViewActions.click())

        // Then
        ViewMatchers.withHint("Phone").matches(view)
    }

    @Test
    fun shouldFormatPhoneNumber() {
        restartActivity()

        // Given
        val view = Espresso.onView(ViewMatchers.withId(R.id.etPhone))

        // When
        view.perform(ViewActions.typeText("1234567890"))

        // Then
        view.check(ViewAssertions.matches(ViewMatchers.withText("123-456-7890")))
    }

    @Test
    fun shouldShowAndErrorWithEmptyText() {
        restartActivity()

        // Given
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlPhone) as? EditTextPhoneField)
        val resultIsValid: ValidationResult?
        txtInputLayout?.setIsRequired(true)

        // When
        resultIsValid = txtInputLayout?.isValid()

        // Then
        txtInputLayout?.setRegex(PHONE_NUMBER_REGEX)
        Assert.assertEquals(
            ValidationResult(false, String.format(VALIDATE_EMPTY_ERROR, "Enter some text")), resultIsValid

        )
    }

    @Test
    fun shouldShowAndErrorWithLessDigits() {
        restartActivity()
        // Given
        val phone = "123456"
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlPhone) as? EditTextPhoneField)
        txtInputLayout?.setIsRequired(true)

        // When
        txtInputLayout?.setRegex(PHONE_NUMBER_REGEX)
        Espresso.onView(ViewMatchers.withId(R.id.etPhone)).perform(ViewActions.typeText(phone))

        // Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_LENGTH_ERROR),
            txtInputLayout?.isValid()
        )
    }

    @Test
    fun shouldNotBeValidIfNotRequiredAndRegexNotMatch() {
        restartActivity()
        // Given
        val phone = "123"
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlPhone) as? EditTextPhoneField)
        txtInputLayout?.setIsRequired(false)
        txtInputLayout?.setRegex(PHONE_NUMBER_REGEX)

        // When
        Espresso.onView(ViewMatchers.withId(R.id.etPhone)).perform(ViewActions.typeText(phone))

        // Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_LENGTH_ERROR),
            txtInputLayout?.isValid()
        )
    }

    @Test
    fun shouldBeValidIfNotRequiredAndRegexMatch() {
        restartActivity()
        // Given
        val phone = "1234567890"
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlPhone) as EditTextPhoneField)
        txtInputLayout.setIsRequired(false)
        txtInputLayout.setRegex(PHONE_NUMBER_REGEX)

        // When
        Espresso.onView(ViewMatchers.withId(R.id.etPhone)).perform(ViewActions.typeText(phone))

        // Then
        Assert.assertTrue(txtInputLayout.isValid().isValid)
        Assert.assertEquals("123-456-7890", txtInputLayout?.getValue())
    }
}
