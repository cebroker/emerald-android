package co.condorlabs.customcomponents.test

/**
 * @author Oscar Gallon on 2019-05-22.
 */
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


import android.widget.AutoCompleteTextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.SmallTest
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.MESSAGE_FORMAT_ERROR
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.customspinner.SpinnerData
import co.condorlabs.customcomponents.customspinner.SpinnerFormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import org.hamcrest.CoreMatchers.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Oscar Gallon on 2/26/19.
 */
class SpinnerFormFieldTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_statespinnerformfield_test
    }

    @SmallTest
    @Test
    fun shouldDisplayLabel() {
        restartActivity()

        // Given
        val field = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)

        // When
        val hint = field?.textInputLayout?.hint

        // Then
        Assert.assertNotNull(hint)
    }

    @SmallTest
    @Test
    fun shouldDisplaySpinner() {
        restartActivity()

        // Given
        val view = Espresso.onView(withId(R.id.actvBase))

        // When

        // Then
        view.check(matches(isDisplayed()))
    }

    @SmallTest
    @Test
    fun shouldDisplayHint() {
        MockActivity.layout = R.layout.activity_spinner_with_hint
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val view = Espresso.onView(withId(R.id.actvBase))
        val data = SpinnerData("1", "Antioquia")
        val data1 = SpinnerData("2", "Cundinamarca")
        val data3 = SpinnerData("3", "Atlantico")

        // When
        ruleActivity.runOnUiThread {
            formField.setData(arrayListOf(data, data1, data3))
        }
        view.perform(click())
        onData(
            allOf(
                `is`(instanceOf(SpinnerData::class.java)),
                `is`(SpinnerData("2", "Cundinamarca"))
            )
        ).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        // Then

        Espresso.onView(withText("Cundinamarca")).check(matches(isDisplayed()))
    }

    @SmallTest
    @Test
    fun shouldClickOnSpinnerCustomView() {
        MockActivity.layout = R.layout.activity_spinner_with_hint
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val view = Espresso.onView(withId(R.id.tlState))
        val data = SpinnerData("1", "Antioquia")
        val data1 = SpinnerData("2", "Cundinamarca")
        val data3 = SpinnerData("3", "Atlantico")

        // When
        ruleActivity.runOnUiThread {
            formField.setData(arrayListOf(data, data1, data3))
        }
        view.perform(click())
        onData(
            allOf(
                `is`(instanceOf(SpinnerData::class.java)),
                `is`(SpinnerData("2", "Cundinamarca"))
            )
        ).inRoot(RootMatchers.isPlatformPopup()).perform(click())

        // Then
        Espresso.onView(withText("Cundinamarca")).check(matches(isDisplayed()))
    }

    @SmallTest
    @Test
    fun shouldShowStatesOnSpinner() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val view = Espresso.onView(withId(R.id.actvBase))
        val data = SpinnerData("1", "Antioquia")
        val data1 = SpinnerData("2", "Cundinamarca")
        val data3 = SpinnerData("3", "Atlantico")

        // When
        ruleActivity.runOnUiThread {
            formField.setData(arrayListOf(data, data1, data3))
        }
        view.perform(click())
        onData(allOf(`is`(instanceOf(SpinnerData::class.java)), `is`(data1))).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        // Then

        Espresso.onView(withText("Cundinamarca")).check(matches(isDisplayed()))
    }

    @SmallTest
    @Test
    fun shouldShowStatesSorted() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val spinner = ruleActivity.activity.findViewById<AutoCompleteTextView>(R.id.actvBase)
        val view = Espresso.onView(withId(R.id.actvBase))
        val data = SpinnerData("1", "Antioquia")
        val data1 = SpinnerData("2", "Cundinamarca")
        val data2 = SpinnerData("3", "Atlantico")

        // When
        ruleActivity.runOnUiThread {
            formField.setData(arrayListOf(data, data1, data2))
        }
        view.perform(click())
        onData(allOf(`is`(instanceOf(SpinnerData::class.java)), `is`(data1))).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        // Then
        Espresso.onView(withText("Cundinamarca")).check(matches(isDisplayed()))
        Assert.assertEquals(data1, spinner.adapter.getItem(3))
    }

    @SmallTest
    @Test
    fun shouldShowErrorIfNotElementIsSelected() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val error = String.format(MESSAGE_FORMAT_ERROR, "State")
        val data = SpinnerData("1", "Antioquia")
        val data1 = SpinnerData("2", "Cundinamarca")
        val data2 = SpinnerData("3", "Atlantico")

        // When
        ruleActivity.runOnUiThread {
            formField.setIsRequired(true)
            formField.setData(arrayListOf(data, data1, data2))
        }
        val result = formField.isValid()
        val textInputLayout = formField.textInputLayout ?: throw NullPointerException()
        showErrorInInputLayout(textInputLayout, result.error)

        // Then
        Assert.assertEquals(
            ValidationResult(false, error),
            result
        )

        Assert.assertEquals(error, textInputLayout.error)
    }

    @SmallTest
    @Test
    fun shouldShowErrorIfNotElementIsSelectedWithIsRequiredInXml() {
        MockActivity.layout = R.layout.activity_spinner_with_is_required
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val error = String.format(MESSAGE_FORMAT_ERROR, "State")
        val data = SpinnerData("1", "Antioquia")
        val data1 = SpinnerData("2", "Cundinamarca")
        val data2 = SpinnerData("3", "Atlantico")

        // When
        ruleActivity.runOnUiThread {
            formField.setData(arrayListOf(data, data1, data2))
        }
        val result = formField.isValid()
        val textInputLayout = formField.textInputLayout ?: throw NullPointerException()
        showErrorInInputLayout(textInputLayout, result.error)

        // Then
        Assert.assertEquals(
            ValidationResult(false, error),
            result
        )

        Assert.assertEquals(error, textInputLayout.error)
    }

    @SmallTest
    @Test
    fun shouldNotShowErrorIfNotElementIsSelected() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val data = SpinnerData("1", "Antioquia")
        val data1 = SpinnerData("2", "Cundinamarca")
        val data2 = SpinnerData("3", "Atlantico")

        // When
        ruleActivity.runOnUiThread {
            formField.setData(arrayListOf(data, data1, data2))
        }

        val result = formField.isValid()
        val textInputLayout = formField.textInputLayout ?: throw NullPointerException()
        showErrorInInputLayout(textInputLayout, result.error)

        // Then
        Assert.assertEquals(
            ValidationResult(true, EMPTY),
            result
        )
    }

    @SmallTest
    @Test
    fun shouldGetEmptyValueWithOutSelection() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val data = SpinnerData("1", "Antioquia")
        val data1 = SpinnerData("2", "Cundinamarca")
        val data2 = SpinnerData("3", "Atlantico")

        // When
        ruleActivity.runOnUiThread {
            formField.setData(arrayListOf(data, data1, data2))
        }
        val result = formField.getValue()

        // Then
        Assert.assertEquals(
            null,
            result
        )
    }

    @SmallTest
    @Test
    fun shouldGetValueWithSelection() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val view = Espresso.onView(withId(R.id.actvBase))
        val data = SpinnerData("1", "Antioquia")
        val data1 = SpinnerData("2", "Cundinamarca")
        val data2 = SpinnerData("3", "Atlantico")

        // When
        ruleActivity.runOnUiThread {
            formField.setData(arrayListOf(data, data1, data2))
        }
        view.perform(click())
        onData(
            allOf(
                `is`(instanceOf(SpinnerData::class.java)),
                `is`(data2)
            )
        ).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        val result = formField.getValue()

        // Then
        Assert.assertEquals(
            data2,
            result
        )
    }

    @SmallTest
    @Test
    fun shouldBeAbleToSelectAnSpinnerElement() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val view = Espresso.onView(withId(R.id.actvBase))
        val data = SpinnerData("1", "Antioquia")
        val data1 = SpinnerData("2", "Cundinamarca")
        val data2 = SpinnerData("3", "Atlantico")

        // When
        ruleActivity.runOnUiThread {
            formField.setData(arrayListOf(data, data1, data2))
            formField.setItemSelectedById(data2.id)
        }

        view.perform(click())
        onData(allOf(`is`(instanceOf(SpinnerData::class.java)), `is`(data2))).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        // Then
        Espresso.onView(withText("Atlantico")).check(matches(isDisplayed()))
    }

    @SmallTest
    @Test
    fun shouldBeAbleToGetValueSetWithValueSelectListener() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val view = Espresso.onView(withId(R.id.actvBase))
        val data = SpinnerData("1", "Antioquia")
        val data1 = SpinnerData("2", "Cundinamarca")
        val data2 = SpinnerData("3", "Atlantico")
        var result: SpinnerData? = null
        formField?.setValueChangeListener(object : ValueChangeListener<SpinnerData?> {
            override fun onValueChange(value: SpinnerData?) {
                result = value
            }
        })

        // When
        ruleActivity.runOnUiThread {
            formField.setData(arrayListOf(data, data1, data2))
            formField.setItemSelectedById(data2.id)
        }

        view.perform(click())
        onData(allOf(`is`(instanceOf(SpinnerData::class.java)), `is`(data2))).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        // Then
        Assert.assertEquals(data2, result)
    }
}
