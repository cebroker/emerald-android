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

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.VALIDATE_CITY_ERROR
import co.condorlabs.customcomponents.VALIDATE_EMPTY_ERROR
import co.condorlabs.customcomponents.customedittext.BaseEditTextFormField
import co.condorlabs.customcomponents.customedittext.EditTextCityField
import co.condorlabs.customcomponents.formfield.ValidationResult
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Oscar Gallon on 2/25/19.
 */
@RunWith(AndroidJUnit4::class)
class EditTextCityFieldTest : MockActivityTest() {



    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_edittextcityfield_test
    }

    @SmallTest
    @Test
    fun shouldHaveDefaultHint() {
        restartActivity()
        val formField = ruleActivity.activity.findViewById<EditTextCityField>(R.id.tlCity)

        // Given
        val view = Espresso.onView(ViewMatchers.withId(formField!!.id))

        // When
        view.perform(click())

        // Then
        ViewMatchers.withHint("City").matches(view)
    }

    @SmallTest
    @Test
    fun shouldDisplayACustomHint() {
        restartActivity()
        val formField = ruleActivity.activity.findViewById<EditTextCityField>(R.id.tlCity)
        ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.tlCity).hint = "Custom Hint"

        // Given
        val view = Espresso.onView(ViewMatchers.withId(formField!!.id))

        // When
        view.perform(click())

        // Then
        ViewMatchers.withHint("Custom Hint").matches(view)
    }

    @SmallTest
    @Test
    fun shouldReturnErrorIfCityNotBelongToTheState() {
        restartActivity()
        val formField = ruleActivity.activity.findViewById<EditTextCityField>(R.id.tlCity)
        // Given
        val view = Espresso.onView(ViewMatchers.withId(formField!!.editText!!.id))
        val editTextCityField = (ruleActivity.activity.findViewById<View>(R.id.tlCity) as? EditTextCityField)
        editTextCityField?.setIsRequired(true)

        // When
        view.perform(typeText("C"))
        editTextCityField?.setCities(arrayListOf("Medellin", "Sabaneta"))
        editTextCityField?.setStateName("Antioquia")

        // Then
        Assert.assertEquals(ValidationResult(false, "$VALIDATE_CITY_ERROR Antioquia"), editTextCityField?.isValid())
    }

    @SmallTest
    @Test
    fun shouldReturnErrorWithEmptyCity() {
        restartActivity()
        val formField = ruleActivity.activity.findViewById<EditTextCityField>(R.id.tlCity)
        // Given
        val editTextCityField = (ruleActivity.activity.findViewById<View>(formField!!.id) as? EditTextCityField)
        editTextCityField?.setIsRequired(true)

        // When
        val result = editTextCityField?.isValid()

        // Then
        Assert.assertEquals(ValidationResult(false, String.format(VALIDATE_EMPTY_ERROR, "City")), result)
    }

    @SmallTest
    @Test
    fun shouldNotReturnErrorWithEmptyCity() {
        restartActivity()
        val formField = ruleActivity.activity.findViewById<EditTextCityField>(R.id.tlCity)

        // Given
        val editTextCityField = (ruleActivity.activity.findViewById<View>(formField!!.id) as? EditTextCityField)

        // When
        val result = editTextCityField?.isValid()

        // Then
        Assert.assertEquals(ValidationResult(true, EMPTY), result)
    }

    @SmallTest
    @Test
    fun shouldDisplayErrorWithStateName() {
        restartActivity()
        val formField = ruleActivity.activity.findViewById<EditTextCityField>(R.id.tlCity)

        // Given
        val view = Espresso.onView(ViewMatchers.withId(formField!!.editText!!.id))
        val editTextCityField = (ruleActivity.activity.findViewById<View>(formField.id) as? EditTextCityField)
        editTextCityField?.setIsRequired(true)

        // When
        view.perform(typeText("C"))
        editTextCityField?.setCities(arrayListOf("Medellin", "Sabaneta"))
        editTextCityField?.setStateName("Antioquia")
        editTextCityField?.let {
            showErrorInInputLayout(it.textInputLayout!!, it.isValid().error)
        }

        // Then
        ViewMatchers.hasErrorText("$VALIDATE_CITY_ERROR Antioquia").matches(view)
    }
}
