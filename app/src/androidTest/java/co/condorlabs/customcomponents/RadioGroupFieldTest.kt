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

package co.condorlabs.customcomponents

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import co.condorlabs.customcomponents.customradiogroup.RadioGroupFormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.EMPTY
import co.condorlabs.customcomponents.helper.MESSAGE_FORMAT_ERROR
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RadioGroupFieldTest : MockActivityTest(){

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_baseradiogroup_test
    }

    @Test
    fun shouldShowMessageIfNoSelected() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)

        formField.setIsRequired(true)
        //When
        val result = formField.isValid()

        //Then
        Assert.assertEquals(
            ValidationResult(false, String.format(MESSAGE_FORMAT_ERROR, "Custom radio group")), result
        )
    }


    @Test
    fun shouldShowMessageInLabelIfNoSelected() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)

        formField.setIsRequired(true)
        //When
        formField?.let {
            showErrorInInputLayout(it, it.isValid().error)
        }

        //Then
        ViewMatchers.hasErrorText(String.format(MESSAGE_FORMAT_ERROR, "Custom radio group")).matches(formField.getChildAt(0))
    }

    @Test
    fun shouldValidateIfSelected() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)

        formField.setIsRequired(true)
        //When
        Espresso.onView(ViewMatchers.withSubstring("Item 3"))
            .perform(ViewActions.click())
        val result = formField.isValid()

        //Then
        Assert.assertEquals(
            ValidationResult(true, EMPTY), result
        )
    }

    @Test
    fun shouldDisplayTitle() {
        restartActivity()

        //Given
        val view = Espresso.onView(ViewMatchers.withText("Custom radio group"))

        //When
        view.perform(ViewActions.click())

        //Then
        view.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}
