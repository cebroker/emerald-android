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
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import android.view.View
import co.condorlabs.customcomponents.customedittext.EditTextPasswordField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.VALIDATE_EMPTY_ERROR
import co.condorlabs.customcomponents.test.util.isTextDisplayed
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditTextPasswordFieldTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_passwordtestfield_test
    }

    @Test
    fun shouldShowAndErrorWithEmptyPassword() {
        restartActivity()

        // Given
        val view = (ruleActivity.activity.findViewById<View>(R.id.tlPassword) as? EditTextPasswordField)
        view?.setIsRequired(true)

        // When
        val result = view?.isValid()

        // Then
        Assert.assertEquals(
            ValidationResult(false, String.format(VALIDATE_EMPTY_ERROR, "Enter some text")),
            result
        )
    }

    @Test
    fun shouldNotSeePassword() {
        restartActivity()

        // Given
        val view = Espresso.onView(ViewMatchers.withId(R.id.etPassword))

        // When
        view.perform(ViewActions.typeText("1234567890"))

        // Then
        not(isTextDisplayed("1234567890"))
    }

    @Test
    fun eyeIconIsDisplay() {
        Espresso.onView(allOf(withId(R.id.text_input_password_toggle), isDisplayed()))
    }

    @Test
    fun tapEyeToSeePassword() {
        // Given
        shouldNotSeePassword()
        val checkableImageButton = Espresso.onView(withId(R.id.text_input_password_toggle))

        // When
        checkableImageButton.perform(ViewActions.click())

        // Then
        isTextDisplayed("1234567890")
    }

    @Test
    fun tapEyeToHidePassword() {
        // Given
        shouldNotSeePassword()
        val checkableImageButton = Espresso.onView(withId(R.id.text_input_password_toggle))

        // When
        checkableImageButton.perform(ViewActions.doubleClick())

        // Then
        not(isTextDisplayed("1234567890"))
    }
}
