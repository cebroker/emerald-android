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

import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.filters.SmallTest
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.MESSAGE_FORMAT_ERROR
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.customradiogroup.RadioGroupFormField
import co.condorlabs.customcomponents.formfield.Selectable
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.test.util.withTintColorInRadioButtons
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RadioGroupFieldTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_baseradiogroup_test
    }

    @SmallTest
    @Test
    fun shouldShowMessageIfNoSelected() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)
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
            ValidationResult(false, String.format(MESSAGE_FORMAT_ERROR, "Custom radio group")), result
        )
    }

    @SmallTest
    @Test
    fun shouldShowMessageIfNoSelected1() {
        MockActivity.layout = R.layout.activity_baseradiogroup_is_required_test
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup1)
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
            ValidationResult(false, String.format(MESSAGE_FORMAT_ERROR, "Custom radio group1")), result
        )
    }

    @SmallTest
    @Test
    fun shouldShowMessageInLabelIfNoSelected() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)
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
        formField?.let {
            showErrorInInputLayout(it, it.isValid().error)
        }

        // Then

        ViewMatchers.hasErrorText(String.format(MESSAGE_FORMAT_ERROR, "Custom radio group"))
            .matches(formField.getChildAt(0))
    }

    @SmallTest
    @Test
    fun shouldValidateIfSelected() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)
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
        formField.setIsRequired(true)

        // When
        Espresso.onView(ViewMatchers.withSubstring("Item 3"))
            .perform(ViewActions.click())
        val result = formField.isValid()

        // Then
        Assert.assertEquals(
            ValidationResult(true, EMPTY), result
        )
    }

    @SmallTest
    @Test
    fun shouldDisplayTitle() {
        restartActivity()

        // Given
        val view = Espresso.onView(ViewMatchers.withText("Custom radio group"))

        // When
        view.perform(ViewActions.click())

        // Then
        view.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @SmallTest
    @Test
    fun shouldBeInitWithSelectableOption() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)
        ruleActivity.runOnUiThread {
            formField.setSelectables(
                arrayListOf(
                    Selectable("Item 1", false),
                    Selectable("Item 2", false),
                    Selectable("Item 3", false),
                    Selectable("Item 4", true)
                )
            )
        }
        formField.setIsRequired(true)

        // When
        val result = formField.isValid()

        // Then
        Assert.assertEquals(
            ValidationResult(true, EMPTY), result
        )
    }

    @SmallTest
    @Test
    fun shouldReturnEmptyWithoutSelection() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)
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
        val result = formField.getValue()

        // Then
        Assert.assertEquals(
            "", result
        )
    }

    @SmallTest
    @Test
    fun shouldReturnValueSelected() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)
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
        Espresso.onView(ViewMatchers.withSubstring("Item 2"))
            .perform(ViewActions.click())
        val result = formField.getValue()

        // Then
        Assert.assertEquals(
            "Item 2", result
        )
    }

    @SmallTest
    @Test
    fun shouldBeAbleToGetSelectedValues() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)
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

        var result = ""

        formField?.setValueChangeListener(object : ValueChangeListener<String> {
            override fun onValueChange(value: String) {
                result = value
            }
        })

        // When
        Espresso.onView(ViewMatchers.withSubstring("Item 3"))
            .perform(ViewActions.click())

        // Then
        Assert.assertEquals(
            "Item 3", result
        )
    }

    @SmallTest
    @Test
    fun shouldBeValidatedOnValueChange() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)
        val radioGroup = ruleActivity.activity.findViewById<RadioGroup>(R.id.rgBase)
        formField.setIsRequired(true)
        ruleActivity.runOnUiThread {
            formField.setSelectables(
                arrayListOf(
                    Selectable("Item 1", false),
                    Selectable("Item 2", true),
                    Selectable("Item 3", false),
                    Selectable("Item 4", false)
                )
            )
        }

        // When
        ruleActivity.runOnUiThread {
            radioGroup.clearCheck()
        }

        // Then
        Espresso.onView(withId(R.id.tlRadioGroup))
            .check(
                matches(
                    hasTextInputLayoutErrorText(
                        String.format(MESSAGE_FORMAT_ERROR, "Custom radio group")
                    )
                )
            )
    }

    @SmallTest
    @Test
    fun shouldGetTitle() {
        restartActivity()
        // Given
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)

        // When
        val result = formField.getTitle()

        // Then
        Assert.assertEquals("Custom radio group", result)
    }

    @LargeTest
    @Test
    fun shouldChangeStyleOnDisable() {
        // Given
        restartActivity()
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)

        // When
        ruleActivity.runOnUiThread {
            formField.setSelectables(
                arrayListOf(
                    Selectable("Item 1", false),
                    Selectable("Item 2", true),
                    Selectable("Item 3", false),
                    Selectable("Item 4", false)
                )
            )
            formField.setEnable(false)
        }

        // Then
        onView(withId(R.id.tlRadioGroup)).check(
            matches(
                withTintColorInRadioButtons(
                    ContextCompat.getColor(
                        ruleActivity.activity,
                        R.color.gray_color_with_alpha
                    )
                )
            )
        )
    }

    @LargeTest
    @Test
    fun shouldChangeStyleOnEnabled() {
        // Given
        restartActivity()
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)

        // When
        ruleActivity.runOnUiThread {
            formField.setSelectables(
                arrayListOf(
                    Selectable("Item 1", false),
                    Selectable("Item 2", true),
                    Selectable("Item 3", false),
                    Selectable("Item 4", false)
                )
            )
        }

        // Then
        onView(withId(R.id.tlRadioGroup)).check(
            matches(
                withTintColorInRadioButtons(
                    ContextCompat.getColor(
                        ruleActivity.activity,
                        R.color.credentials_button_swipe_text_color
                    )
                )
            )
        )
    }

    @LargeTest
    @Test
    fun shouldMarkAnOptionAsSelected() {
        // Given
        restartActivity()
        val formField = ruleActivity.activity.findViewById<RadioGroupFormField>(R.id.tlRadioGroup)

        // When
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

        ruleActivity.runOnUiThread {
            formField.setSelectedItem("Item 3")
        }

        // Then
        Assert.assertEquals("Item 3", formField.getValue())
    }
}
