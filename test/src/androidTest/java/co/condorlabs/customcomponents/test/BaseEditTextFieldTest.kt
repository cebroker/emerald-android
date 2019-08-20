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
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.customedittext.BaseEditTextFormField
import co.condorlabs.customcomponents.test.util.isTextInLines
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Oscar Gallon on 2/26/19.
 */
@RunWith(AndroidJUnit4::class)
class BaseEditTextFieldTest : MockActivityTest() {

    @SmallTest
    @Test
    fun shouldDisplayDefaultHint() {
        MockActivity.layout = R.layout.activity_baseedittextfield_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)

        // When

        // Then
        Assert.assertEquals("Enter some text", formField?.hint)
    }

    @SmallTest
    @Test
    fun shouldDisplayZipHint() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)

        // When

        // Then
        Assert.assertEquals("Zip", formField?.hint)
    }

    @SmallTest
    @Test
    fun shouldDisplayTextWithMultiline() {
        MockActivity.layout = R.layout.activity_baseedittextfield_multiline_test
        restartActivity()

        // Given
        val base = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        val realEditText = base.textInputLayout!!.editText!!
        val editText = Espresso.onView(withId(realEditText.id))

        // When
        editText.perform(typeText("Amy normally hated Monday mornings, but this year was different. Kamal was in her art class and she liked Kamal. She was waiting outside the classroom when her friend Tara arrived."))

        // Then
        isTextInLines(3)
    }

    @SmallTest
    @Test
    fun shouldNotBeInvalidIfItsNotRequired() {
        MockActivity.layout = R.layout.activity_baseedittext_no_required_test
        restartActivity()

        // Given

        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        formField.setIsRequired(false)
        formField.setMaxLength(5)
        val realEditText = formField.textInputLayout!!.editText!!
        val editText = Espresso.onView(withId(realEditText.id))

        // When
        editText.perform(typeText("12345"))
        val result = formField.isValid()

        // Then
        Assert.assertTrue(result.isValid)
    }

    @SmallTest
    @Test
    fun shouldReturnEmptyIfNothingIsType() {
        MockActivity.layout = R.layout.activity_baseedittext_no_required_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)

        // When
        val result = formField.getValue()

        // Then
        Assert.assertEquals("", result)
    }

    @SmallTest
    @Test
    fun shouldReturnValueTyped() {
        MockActivity.layout = R.layout.activity_baseedittext_no_required_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        val editText = Espresso.onView(withId(formField.textInputLayout!!.editText!!.id))

        // When
        editText.perform(typeText("156"))

        // Then
        Assert.assertEquals("156", formField.getValue())
    }

    @SmallTest
    @Test
    fun shouldOnlyAllowToTypeUntilMaxLength() {
        MockActivity.layout = R.layout.activity_baseedittext_with_hint_and_regex_test
        restartActivity()

        // Given
        val base = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        val realEditText = base.textInputLayout!!.editText!!
        val editText = Espresso.onView(withId(realEditText.id))
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        formField.setIsRequired(true)
        formField.setMaxLength(5)

        // When
        editText.perform(typeText("123456"))
        val result = formField.isValid()

        // Then
        Espresso.onView(ViewMatchers.withText("12345")).check(matches(isDisplayed()))
        Assert.assertEquals("Zip", (formField)?.hint)
        Assert.assertTrue(result.isValid)
    }

    @SmallTest
    @Test
    fun shouldOnlyLetTypeAccordingTheInputType() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        // Given
        val base = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        val realEditText = base.textInputLayout!!.editText!!
        val editText = Espresso.onView(withId(realEditText.id))

        // When
        editText.perform(typeText("A1"))

        // Then
        editText.check(matches(withText("1")))
    }

    @SmallTest
    @Test
    fun shouldNotTypeMoreCharacyersThanTheRegexAllow() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        formField.setRegex("^[0-9]{9}$")
        val realEditText = formField.textInputLayout!!.editText!!
        val editText = Espresso.onView(withId(realEditText.id))

        // When
        formField.setMaxLength(9)
        editText.perform(typeText("12345678901"))
        val result = formField.isValid()

        // Then
        Espresso.onView(ViewMatchers.withText("123456789")).check(matches(isDisplayed()))
        Assert.assertTrue(result.isValid)
    }

    @SmallTest
    @Test
    fun shouldNotBeValidIfTextDoesNotMatchRegex() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        formField.setIsRequired(true)
        formField.setRegex("^[0-9]{9}$")
        val realEditText = formField.textInputLayout!!.editText!!
        val editText = Espresso.onView(withId(realEditText.id))

        // When
        editText.perform(typeText("121"))
        val result = formField.isValid()

        // Then
        Assert.assertFalse(result.isValid)
        Assert.assertEquals(String.format(VALIDATE_INCORRECT_ERROR, "Zip"), result.error)
    }

    @SmallTest
    @Test
    fun shouldShowErrorWithHintWithoutValidateRegex() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        formField.setIsRequired(true)

        // When
        val result = formField.isValid()

        // Then
        Assert.assertFalse(result.isValid)
        Assert.assertEquals(String.format(VALIDATE_EMPTY_ERROR, "Zip"), result.error)
    }

    @SmallTest
    @Test
    fun shouldSetMaxLinesMinLinesAndAlpha() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        formField.setIsRequired(true)
        ruleActivity.runOnUiThread {
            formField.setMaxLines(10)
            formField.setMinLines(7)
            formField.setBackgroundAlpha(0)
        }

        // When
        val result = formField.isValid()

        // Then
        Assert.assertFalse(result.isValid)
        Assert.assertEquals(String.format(VALIDATE_EMPTY_ERROR, "Zip"), result.error)
    }

    @SmallTest
    @Test
    fun shouldShowPlaceHolderFromXML() {
        MockActivity.layout = R.layout.activity_baseedittext_placeholder
        restartActivity()

        // given
        val baseView = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)

        // when
        Espresso.onView(withId(R.id.tlBase)).perform(click())
        Thread.sleep(210)

        // then
       Assert.assertEquals(baseView.textInputLayout!!.editText!!.hint, "Hola")
    }

    @SmallTest
    @Test
    fun shouldShowValidationIconIfMatchRegex() {
        MockActivity.layout = R.layout.activity_baseedittext_with_regex_and_icon_validation
        restartActivity()

        // Given
        val view = Espresso.onView(withId(R.id.tlBase))
        val baseView = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        val realEditText = baseView.textInputLayout!!.editText!!
        val editText = Espresso.onView(withId(realEditText.id))

        // when
        editText.perform(typeText("12345"))

        // Then
        Assert.assertNotNull(realEditText!!.compoundDrawables[2])
    }

    @SmallTest
    @Test
    fun shouldNotShowValidationIconIfTextDoesNotMatchTheRegex() {
        MockActivity.layout = R.layout.activity_baseedittext_with_regex_and_icon_validation
        restartActivity()

        // Given
        val view = Espresso.onView(withId(R.id.tlBase))
        val baseView = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        val realEditText = baseView.textInputLayout!!.editText!!
        val editText = Espresso.onView(withId(realEditText.id))

        // when
        editText.perform(typeText("123454"))

        // Then
        Assert.assertNull(realEditText.compoundDrawables[2])
    }

    @SmallTest
    @Test
    fun shouldNotShowIconIsNoValidationIsEnableButMatchRegex() {
        MockActivity.layout = R.layout.activity_baseedittext_with_hint_and_regex_test
        restartActivity()

        // Given
        val view = Espresso.onView(withId(R.id.tlBase))
        val baseView = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        val realEditText = baseView.textInputLayout!!.editText!!
        val editText = Espresso.onView(withId(realEditText.id))

        // when
        editText.perform(typeText("123454"))

        // Then
        Assert.assertNull(realEditText.compoundDrawables[2])
    }

    @SmallTest
    @Test
    fun shouldBeDisable() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        val editText = Espresso.onView(withId(formField.textInputLayout!!.editText!!.id))

        // When
        editText.perform(typeText("12345"))
        ruleActivity.runOnUiThread {
            formField.setEnable(false)
        }

        // Then
        Espresso.onView(withText("12345")).check(matches(CoreMatchers.not(isEnabled())))
    }

    @SmallTest
    @Test
    fun shouldBeEnable() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        val editText = Espresso.onView(withId(formField.textInputLayout!!.editText!!.id))

        // When
        editText.perform(typeText("12345"))
        ruleActivity.runOnUiThread {
            formField.setEnable(false)
        }
        Espresso.onView(withText("12345")).check(matches(CoreMatchers.not(isEnabled())))
        ruleActivity.runOnUiThread {
            formField.setEnable(true)
        }
        // Then
        Espresso.onView(withText("12345")).check(matches(isEnabled()))
    }

    @Test
    fun shouldBeInvalidIfDoesNotMatchWithAnyRegex() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        formField.setIsRequired(true)
        formField.setRegex(listOf("^[0-9]{9}$", "^[a-z]+$"))
        val realEditText = formField.textInputLayout!!.editText!!
        val editText = Espresso.onView(withId(realEditText.id))

        // When
        editText.perform(typeText("121"))
        val result = formField.isValid()

        // Then
        Assert.assertFalse(result.isValid)
        Assert.assertEquals(String.format(VALIDATE_INCORRECT_ERROR, "Zip"), result.error)
    }

    @Test
    fun shouldBeValidIfMatchesAtLeastOneRegex() {
        MockActivity.layout = R.layout.activity_baseedittextfield_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        formField.setIsRequired(true)
        formField.setRegex(listOf("^[0-9]{9}$", "^[a-z]+$"))
        val realEditText = formField.textInputLayout!!.editText!!
        val editText = Espresso.onView(withId(realEditText.id))

        // When
        editText.perform(typeText("a"))
        val result = formField.isValid()

        // Then
        Assert.assertTrue(result.isValid)
    }

    @SmallTest
    @Test
    fun shouldGetInputTypePassword() {
        MockActivity.layout = R.layout.activity_baseedittextfield_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.etPasswordField)

        // When
        val result = formField.getInputType()

        // Then
        Assert.assertEquals(INPUT_TYPE_PASSWORD, result)
    }

    @SmallTest
    @Test
    fun shouldGetInputTypePhone() {
        MockActivity.layout = R.layout.activity_baseedittextfield_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.etPhoneField)

        // When
        val result = formField.getInputType()

        // Then
        Assert.assertEquals(INPUT_TYPE_PHONE, result)
    }

    @SmallTest
    @Test
    fun shouldGetInputTypeNumberDecimal() {
        MockActivity.layout = R.layout.activity_baseedittextfield_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.etNumberDecimalField)

        // When
        val result = formField.getInputType()

        // Then
        Assert.assertEquals(INPUT_TYPE_NUMBER_DECIMAL, result)
    }

    @SmallTest
    @Test
    fun shouldGetInputTypeNumber() {
        MockActivity.layout = R.layout.activity_baseedittextfield_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.etNumberField)

        // When
        val result = formField.getInputType()

        // Then
        Assert.assertEquals(INPUT_TYPE_NUMBER, result)
    }

    @SmallTest
    @Test
    fun shouldGetInputTypeTextCapCharacters() {
        MockActivity.layout = R.layout.activity_baseedittextfield_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.etTextCapCharactersField)

        // When
        val result = formField.getInputType()

        // Then
        Assert.assertEquals(INPUT_TYPE_TEXT_CAP_CHARACTERS, result)
    }

    @SmallTest
    @Test
    fun shouldGetInputTypeDefault() {
        MockActivity.layout = R.layout.activity_baseedittextfield_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)

        // When
        val result = formField.getInputType()

        // Then
        Assert.assertEquals(INPUT_TYPE_TEXT, result)
    }

    @SmallTest
    @Test
    fun shouldGetRegex() {
        MockActivity.layout = R.layout.activity_baseedittext_with_regex_and_icon_validation
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)

        // When
        val result = formField.getRegex()

        // Then
        Assert.assertTrue(result.contains("^[0-9]{5}\$"))
    }

    @SmallTest
    @Test
    fun shouldGetDigitsFromXml() {
        MockActivity.layout = R.layout.activity_baseedittext_with_digits
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)

        // When
        val result = formField.getDigits()

        // Then
        Assert.assertEquals("12345ABCDE?", result)
    }

    @SmallTest
    @Test
    fun settingInputTypeShouldNotAffectDigits() {
        MockActivity.layout = R.layout.activity_baseedittext_with_digits
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBaseWithInputType)

        // When
        val digits = formField.getDigits()

        // Then
        Assert.assertEquals("123BAR?", digits)
    }

    @SmallTest
    @Test
    fun shoulAcceptOnlyTheSpecifiedCharacters() {
        MockActivity.layout = R.layout.activity_baseedittext_with_digits
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBaseWithInputType)

        // When
        Espresso.onView(withId(formField.editText!!.id))
            .perform(typeText("0123456789?ABCMNOXYZ."))
        val result = formField.getValue()

        // Then
        Assert.assertEquals("123?AB", result)
    }
}
