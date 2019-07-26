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
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.runner.AndroidJUnit4
import android.view.View
import androidx.test.filters.SmallTest
import co.condorlabs.customcomponents.customedittext.BaseEditTextFormField
import co.condorlabs.customcomponents.customedittext.EditTextEmailField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.VALIDATE_EMAIL_ERROR
import co.condorlabs.customcomponents.VALIDATE_EMPTY_ERROR
import co.condorlabs.customcomponents.customedittext.EditTextCityField
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Oscar Gallon on 2/25/19.
 */
@RunWith(AndroidJUnit4::class)
class EditTextEmailFieldTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_emailtestfield_test
    }

    @SmallTest
    @Test
    fun shouldHaveDefaultHint() {
        restartActivity()

        // Given
        val view = Espresso.onView(ViewMatchers.withId(R.id.tlEmail))

        // When
        view.perform(ViewActions.click())

        // Then
        ViewMatchers.withHint("Email").matches(view)
    }

    @SmallTest
    @Test
    fun shouldShowAndErrorWithEmptyEmail() {
        restartActivity()

        // Given
        (ruleActivity.activity.findViewById<View>(R.id.tlEmail) as? EditTextEmailField)?.setIsRequired(true)

        // When
        val result = (ruleActivity.activity.findViewById<View>(R.id.tlEmail) as? BaseEditTextFormField)?.isValid()

        // Then
        Assert.assertEquals(
            ValidationResult(false, String.format(VALIDATE_EMPTY_ERROR, "Enter some text")), result
        )
    }

    @SmallTest
    @Test
    fun shouldShowErrorWitheEmailIncorrectPart1() {
        restartActivity()
        // Given
        val text = "kdfkd"
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlEmail) as? EditTextEmailField)
        txtInputLayout?.setIsRequired(true)
        val formField = ruleActivity.activity.findViewById<EditTextEmailField>(R.id.tlEmail)
        val view = Espresso.onView(ViewMatchers.withId(formField!!.editText!!.id))

        // When
        txtInputLayout?.setRegex(android.util.Patterns.EMAIL_ADDRESS.toString())
        view.perform(typeText(text))

        // Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_EMAIL_ERROR),
            txtInputLayout?.isValid()
        )
    }

    @SmallTest
    @Test
    fun shouldShowErrorWitheEmailIncorrectPart2() {
        restartActivity()
        // Given
        val text = "kdfkd@"
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlEmail) as? EditTextEmailField)
        txtInputLayout?.setIsRequired(true)
        val formField = ruleActivity.activity.findViewById<EditTextEmailField>(R.id.tlEmail)
        val view = Espresso.onView(ViewMatchers.withId(formField!!.editText!!.id))

        // When
        txtInputLayout?.setRegex(android.util.Patterns.EMAIL_ADDRESS.toString())
        view.perform(typeText(text))

        // Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_EMAIL_ERROR),
            txtInputLayout?.isValid()
        )
    }

    @SmallTest
    @Test
    fun shouldShowErrorWitheEmailIncorrectPart3() {
        restartActivity()
        // Given
        val text = "kdfkd@smdms"
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlEmail) as? EditTextEmailField)
        txtInputLayout?.setIsRequired(true)
        val formField = ruleActivity.activity.findViewById<EditTextEmailField>(R.id.tlEmail)
        val view = Espresso.onView(ViewMatchers.withId(formField!!.editText!!.id))

        // When
        txtInputLayout?.setRegex(android.util.Patterns.EMAIL_ADDRESS.toString())
        view.perform(typeText(text))

        // Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_EMAIL_ERROR),
            txtInputLayout?.isValid()
        )
    }

    @SmallTest
    @Test
    fun shouldShowErrorWitheEmailIncorrectPart4() {
        restartActivity()
        // Given
        val text = "kdfkd@smdms."
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlEmail) as? EditTextEmailField)
        txtInputLayout?.setIsRequired(true)
        val formField = ruleActivity.activity.findViewById<EditTextEmailField>(R.id.tlEmail)
        val view = Espresso.onView(ViewMatchers.withId(formField!!.editText!!.id))

        // When
        txtInputLayout?.setRegex(android.util.Patterns.EMAIL_ADDRESS.toString())
        view.perform(typeText(text))

        // Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_EMAIL_ERROR),
            txtInputLayout?.isValid()
        )
    }

    @SmallTest
    @Test
    fun shouldMatch() {
        restartActivity()
        // Given
        val text = "o@gmail.co"
        val txtInputLayout = (ruleActivity.activity.findViewById<View>(R.id.tlEmail) as? EditTextEmailField)
        txtInputLayout?.setIsRequired(true)
        val formField = ruleActivity.activity.findViewById<EditTextEmailField>(R.id.tlEmail)
        val view = Espresso.onView(ViewMatchers.withId(formField!!.editText!!.id))
        // When
        txtInputLayout?.setRegex(android.util.Patterns.EMAIL_ADDRESS.toString())
        view.perform(typeText(text))

        // Then
        Assert.assertEquals(
            ValidationResult(true, EMPTY),
            txtInputLayout?.isValid()
        )
    }
}
