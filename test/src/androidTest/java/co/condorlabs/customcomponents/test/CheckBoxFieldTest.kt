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

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.SmallTest
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.MESSAGE_FORMAT_ERROR
import co.condorlabs.customcomponents.customcheckbox.CheckboxFormField
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.formfield.Selectable
import co.condorlabs.customcomponents.formfield.ValidationResult
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CheckBoxFieldTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_basecheckbox_test
    }

    @SmallTest
    @Test
    fun shouldShowMessageIfNoSelectedWhenIsRequired1() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilCheckBox)
        formField.setIsRequired(true)

        // When
        val result = formField.isValid()

        // Then
        Assert.assertEquals(
            ValidationResult(false, String.format(MESSAGE_FORMAT_ERROR, "Custom check")), result
        )
    }

    @SmallTest
    @Test
    fun shouldShowMessageIfNoSelectedWhenIsRequired2() {
        MockActivity.layout = R.layout.activity_basecheckbox_is_required_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.til1Checbox)

        // When
        val result = formField.isValid()

        // Then
        Assert.assertEquals(
            ValidationResult(false, String.format(MESSAGE_FORMAT_ERROR, "Custom check1")), result
        )
    }

    @SmallTest
    @Test
    fun shouldShowErrorMessageInLabelIfNoSelected() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilCheckBox)
        formField.setIsRequired(true)

        // When
        formField?.let {
            showErrorInInputLayout(it, it.isValid().error)
        }

        // Then
        onView(withId(R.id.tilCheckBox))
            .check(
                matches(
                    hasTextInputLayoutErrorText(
                        String.format(
                            MESSAGE_FORMAT_ERROR,
                            "Custom check"
                        )
                    )
                )
            )
    }

    @SmallTest
    @Test
    fun shouldShowErrorMessageIfNothingIsSelected() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilCheckBox)
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

        // When
        val result = formField.isValid()

        // Then
        Assert.assertEquals(
            ValidationResult(false, String.format(MESSAGE_FORMAT_ERROR, "Custom check")), result
        )
    }

    @SmallTest
    @Test
    fun shouldBeValidIfItsNotRequired() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilCheckBox)

        formField.setIsRequired(false)
        // When
        val result = formField.isValid()

        // Then
        Assert.assertEquals(
            ValidationResult(true, EMPTY), result
        )
    }

    @SmallTest
    @Test
    fun shouldBeInitFromValues() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilCheckBox)
        ruleActivity.runOnUiThread {
            formField.setSelectables(
                arrayListOf(
                    Selectable("Item 1", true),
                    Selectable("Item 2", false),
                    Selectable("Item 3", true),
                    Selectable("Item 4", false)
                )
            )
        }

        formField.setIsRequired(false)

        // When
        val result = formField.getValue()
        onView(withSubstring("Item 2"))
            .perform(ViewActions.click())
        onView(withSubstring("Item 3"))
            .perform(ViewActions.click())

        // Then
        Assert.assertEquals(
            arrayListOf(
                Selectable("Item 1", true),
                Selectable("Item 2", true),
                Selectable("Item 3", false),
                Selectable("Item 4", false)
            ), result
        )
    }

    @SmallTest
    @Test
    fun shouldReturnEmptyListWithNoSelectables() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilCheckBox)

        formField.setIsRequired(false)
        // When
        val result = formField.getValue()

        // Then
        Assert.assertEquals(
            ArrayList<Selectable>(), result
        )
    }

    @SmallTest
    @Test
    fun shouldBeAbleToGetSelectedValues() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilCheckBox)
        ruleActivity.runOnUiThread {
            formField.setSelectables(
                arrayListOf(
                    Selectable("Item 1", true),
                    Selectable("Item 2", false),
                    Selectable("Item 3", true),
                    Selectable("Item 4", false)
                )
            )
        }
        var result: List<Selectable> = arrayListOf()
        formField?.setValueChangeListener(object : ValueChangeListener<List<Selectable>> {
            override fun onValueChange(value: List<Selectable>) {
                result = value
            }
        })

        // When
        onView(withText("Item 1")).perform(ViewActions.click())

        // Then
        Assert.assertEquals(
            arrayListOf(
                Selectable("Item 1", false),
                Selectable("Item 2", false),
                Selectable("Item 3", true),
                Selectable("Item 4", false)
            ), result
        )
    }

    @SmallTest
    @Test
    fun shouldBeValidatedOnValueChanged() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<CheckboxFormField>(R.id.tilCheckBox)
        formField.setIsRequired(true)

        ruleActivity.runOnUiThread {
            formField.setSelectables(
                arrayListOf(
                    Selectable("Item 1", true),
                    Selectable("Item 2", false),
                    Selectable("Item 3", false),
                    Selectable("Item 4", false)
                )
            )
        }

        // When
        onView(withText("Item 1")).perform(ViewActions.click())

        // Then
        onView(withId(R.id.tilCheckBox))
            .check(
                matches(
                    hasTextInputLayoutErrorText(
                        String.format(
                            MESSAGE_FORMAT_ERROR,
                            "Custom check"
                        )
                    )
                )
            )
    }
}
