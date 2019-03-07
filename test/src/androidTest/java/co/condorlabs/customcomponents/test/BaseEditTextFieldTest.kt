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

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import co.condorlabs.customcomponents.customedittext.BaseEditTextFormField
import co.condorlabs.customcomponents.helper.VALIDATE_EMPTY_ERROR
import co.condorlabs.customcomponents.helper.VALIDATE_INCORRECT_ERROR
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Oscar Gallon on 2/26/19.
 */
@RunWith(AndroidJUnit4::class)
class BaseEditTextFieldTest : MockActivityTest() {

    @Test
    fun shouldDisplayDefaultHint() {
        MockActivity.layout = R.layout.activity_baseedittextfield_test
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)

        //When


        //Then
        Assert.assertEquals("Enter some text", formField?.hint)
    }

    @Test
    fun shouldDisplayZipHint() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)

        //When


        //Then
        Assert.assertEquals("Zip", formField?.hint)
    }

    @Test
    fun shouldNotBeInvalidIfItsNotRequired() {
        MockActivity.layout = R.layout.activity_baseedittext_no_required_test
        restartActivity()

        //Given
        val editText = Espresso.onView(withId(R.id.etBase))
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        formField.setIsRequired(false)
        formField.setMaxLength(5)

        //When
        editText.perform(typeText("156"))
        val result = formField.isValid()

        //Then
        Assert.assertTrue(result.isValid)
    }

    @Test
    fun shouldReturnEmptyIfNothingIsType() {
        MockActivity.layout = R.layout.activity_baseedittext_no_required_test
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)

        //When
        val result = formField.getValue()

        //Then
        Assert.assertEquals("", result)
    }

    @Test
    fun shouldReturnValueTyped() {
        MockActivity.layout = R.layout.activity_baseedittext_no_required_test
        restartActivity()

        //Given
        val editText = Espresso.onView(withId(R.id.etBase))
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)

        //When
        editText.perform(typeText("156"))

        //Then
        Assert.assertEquals("156", formField.getValue())
    }

    @Test
    fun shouldHaveRegexAndHint() {
        MockActivity.layout = R.layout.activity_baseedittext_with_hint_and_regex_test
        restartActivity()

        //Given
        val view = Espresso.onView(withId(R.id.tlBase))
        val editText = Espresso.onView(withId(R.id.etBase))
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        formField.setIsRequired(true)
        formField.setMaxLength(5)

        //When
        editText.perform(typeText("123456"))
        val result = formField.isValid()

        //Then
        Espresso.onView(ViewMatchers.withText("12345")).check(matches(isDisplayed()))
        Assert.assertEquals("Zip", (formField)?.hint)
        Assert.assertTrue(result.isValid)
    }

    @Test
    fun shouldBeAbleToGetTheInputTypeSetOnTheLayout() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        //Given
        val view = Espresso.onView(withId(R.id.etBase))

        //When
        view.perform(typeText("A"))

        //Then
        Espresso.onView(ViewMatchers.withText("")).check(matches(isDisplayed()))
    }

    @Test
    fun lengthShouldBeMatch() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        ruleActivity.runOnUiThread {
            formField.setMaxLength(5)
        }
        val view = Espresso.onView(withId(R.id.etBase))

        //When
        view.perform(typeText("123456"))

        //Then
        ViewMatchers.withText("12345").matches(view)
    }

    @Test
    fun shouldValidateRegex() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        formField.setRegex("^[0-9]{9}$")
        val view = Espresso.onView(withId(R.id.etBase))

        //When
        formField.setMaxLength(9)
        view.perform(typeText("12345678901"))
        val result = formField.isValid()

        //Then
        Espresso.onView(ViewMatchers.withText("123456789")).check(matches(isDisplayed()))
        Assert.assertTrue(result.isValid)
    }

    @Test
    fun shouldNotBeValidIfTextDoesNotMatchRegex() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        formField.setIsRequired(true)
        formField.setRegex("^[0-9]{9}$")
        val view = Espresso.onView(withId(R.id.etBase))

        //When
        view.perform(typeText("121"))
        val result = formField.isValid()

        //Then
        Assert.assertFalse(result.isValid)
        Assert.assertEquals(String.format(VALIDATE_INCORRECT_ERROR, "Zip"), result.error)
    }

    @Test
    fun shouldShowErrorWithHintWithoutValidateRegex() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        formField.setIsRequired(true)

        //When
        val result = formField.isValid()

        //Then
        Assert.assertFalse(result.isValid)
        Assert.assertEquals(String.format(VALIDATE_EMPTY_ERROR, "Zip"), result.error)
    }

    @Test
    fun shouldSetMaxLinesMinLinesAndAlpha() {
        MockActivity.layout = R.layout.activity_baseedittextfield_with_hint_test
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlBase)
        formField.setIsRequired(true)
        ruleActivity.runOnUiThread {
            formField.setMaxLines(10)
            formField.setMinLines(7)
            formField.setBackgroundAlpha(0)
        }

        //When
        val result = formField.isValid()

        //Then
        Assert.assertFalse(result.isValid)
        Assert.assertEquals(String.format(VALIDATE_EMPTY_ERROR, "Zip"), result.error)
    }
}
