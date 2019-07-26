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

import android.graphics.drawable.Drawable
import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import co.condorlabs.customcomponents.VALIDATE_EMPTY_ERROR
import co.condorlabs.customcomponents.customedittext.EditTextPasswordField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.test.util.isTextDisplayed
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

    @SmallTest
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

    @SmallTest
    @Test
    fun shouldNotSeePassword() {
        restartActivity()

        // Given
        val formField = (ruleActivity.activity.findViewById<View>(R.id.tlPassword) as? EditTextPasswordField)
        val view = Espresso.onView(ViewMatchers.withId(formField!!.editText!!.id))

        // When
        view.perform(ViewActions.typeText("1234567890"))

        // Then
        not(isTextDisplayed("1234567890"))
    }

    @SmallTest
    @Test
    fun eyeIconIsDisplay() {
        restartActivity()
        // Given
        val view = (ruleActivity.activity.findViewById<View>(R.id.tlPassword) as? EditTextPasswordField)
        val textInpuntLayout = view?.textInputLayout ?: throw NullPointerException()
        var drawable: Drawable? = null

        // When
        ruleActivity.runOnUiThread {
            drawable = textInpuntLayout.passwordVisibilityToggleDrawable
        }

        // Then
        Assert.assertNotNull(drawable)
    }

    @SmallTest
    @Test
    fun tapEyeToSeePassword() {
        restartActivity()
        // Given
        val view = (ruleActivity.activity.findViewById<View>(R.id.tlPassword) as? EditTextPasswordField)
        val textInpuntLayout = view?.textInputLayout ?: throw NullPointerException()
        val editTextView = Espresso.onView(ViewMatchers.withId(view.editText!!.id))

        // When
        editTextView.perform(ViewActions.typeText("1234567890"))

        ruleActivity.runOnUiThread {
            textInpuntLayout.editText!!.transformationMethod = null
        }

        // Then
        isTextDisplayed("1234567890")
    }

    @SmallTest
    @Test
    fun tapEyeToHidePassword() {
        restartActivity()
        // Given
        val formField = ruleActivity.activity.findViewById<EditTextPasswordField>(R.id.tlPassword)
        val view = Espresso.onView(ViewMatchers.withId(formField!!.editText!!.id))

        // When
        view.perform(typeText("1234567890"))

        // Then
        not(isTextDisplayed("1234567890"))
    }
}
