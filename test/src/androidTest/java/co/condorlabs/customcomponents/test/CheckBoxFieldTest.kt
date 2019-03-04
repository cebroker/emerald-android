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
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import co.condorlabs.customcomponents.customcheckbox.CheckboxFormField
import co.condorlabs.customcomponents.formfield.Selectable
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.EMPTY
import co.condorlabs.customcomponents.helper.MESSAGE_FORMAT_ERROR
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CheckBoxFieldTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_basechecbox_test
    }

    @Test
    fun shouldShowMessageIfNoSelectedWhenIsRequired() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilChecbox)
        formField.setIsRequired(true)

        //When
        val result = formField.isValid()

        //Then
        Assert.assertEquals(
            ValidationResult(false, String.format(MESSAGE_FORMAT_ERROR, "Custom check")), result
        )
    }

    @Test
    fun shouldShowErrorMessageInLabelIfNoSelected() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilChecbox)
        formField.setIsRequired(true)

        //When
        formField?.let {
            showErrorInInputLayout(it, it.isValid().error)
        }

        //Then
        ViewMatchers.hasErrorText(String.format(MESSAGE_FORMAT_ERROR, "Custom check")).matches(formField.getChildAt(0))
    }

    @Test
    fun shouldShowErrorMessageIfNothingIsSelected() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilChecbox)
        formField.setIsRequired(true)
        ruleActivity.runOnUiThread {
            formField.setSelectables(
                arrayListOf(
                    Selectable("Item 1", false),
                    Selectable("Item 2", false),
                    Selectable("Item 3", false),
                    Selectable("Item 4", false)
                )
            )
        }

        //When
        val result = formField.isValid()

        //Then
        Assert.assertEquals(
            ValidationResult(false, String.format(MESSAGE_FORMAT_ERROR, "Custom check")), result
        )
    }


    @Test
    fun shouldBeValidIfItsNotRequired() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilChecbox)

        formField.setIsRequired(false)
        //When
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
        val view = Espresso.onView(ViewMatchers.withText("Custom check"))

        //When
        view.perform(ViewActions.click())

        //Then
        view.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun shouldBeInitFromValues() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilChecbox)
        ruleActivity.runOnUiThread {
            formField.setSelectables(arrayListOf(
                    Selectable("Item 1", true),
            Selectable("Item 2", false),
            Selectable("Item 3", true),
            Selectable("Item 4", false)
            ))
        }

        formField.setIsRequired(false)

        //When
        val result = formField.getValue()
        Espresso.onView(ViewMatchers.withSubstring("Item 2"))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withSubstring("Item 3"))
            .perform(ViewActions.click())

        //Then
        Assert.assertEquals(
            arrayListOf(
                Selectable("Item 1", true),
                Selectable("Item 2", true),
                Selectable("Item 3", false),
                Selectable("Item 4", false)
            ), result
        )
    }

    @Test
    fun shouldReturnEmptyListWithNoSelectables() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilChecbox)

        formField.setIsRequired(false)
        //When
        val result = formField.getValue()

        //Then
        Assert.assertEquals(
            ArrayList<Selectable>(), result
        )
    }


}