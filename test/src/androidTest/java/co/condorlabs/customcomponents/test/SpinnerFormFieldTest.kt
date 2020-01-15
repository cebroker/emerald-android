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
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.SmallTest
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.MESSAGE_FORMAT_ERROR
import co.condorlabs.customcomponents.customedittext.BaseEditTextFormField
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.customspinner.BaseSpinnerFormField
import co.condorlabs.customcomponents.customspinner.SpinnerData
import co.condorlabs.customcomponents.customspinner.SpinnerFormField
import co.condorlabs.customcomponents.customspinner.SpinnerFormFieldListener
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.test.util.isKeyboardClosed
import co.condorlabs.customcomponents.test.util.isKeyboardOpen
import co.condorlabs.customcomponents.test.util.isSpinnerEnable
import org.hamcrest.CoreMatchers.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Oscar Gallon on 2/26/19.
 */
class SpinnerFormFieldTest : MockActivityTest() {

    lateinit var data: SpinnerData
    lateinit var data1: SpinnerData
    lateinit var data2: SpinnerData
    private lateinit var spinnerDataList: ArrayList<SpinnerData>
    private lateinit var spinnerDataListNotSorted: ArrayList<SpinnerData>

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_statespinnerformfield_test
        initData()
    }

    private fun initData() {
        data = SpinnerData("1", "Antioquia")
        data1 = SpinnerData("2", "Cundinamarca")
        data2 = SpinnerData("3", "Atlantico")
        spinnerDataList = arrayListOf(data, data1, data2)
        spinnerDataListNotSorted = arrayListOf(data1, data, data2)
    }

    @SmallTest
    @Test
    fun shouldDisplayHint() {
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
        val base = ruleActivity.activity.findViewById<BaseSpinnerFormField>(R.id.tlState)
        val realEditText = base.textInputLayout!!.editText!!
        val view = Espresso.onView(withId(realEditText.id))

        // When

        // Then
        view.check(matches(isDisplayed()))
    }

    @SmallTest
    @Test
    fun shouldSelectAnItem() {
        MockActivity.layout = R.layout.activity_spinner_with_hint
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val realEditText = formField.textInputLayout!!.editText!!
        val view = Espresso.onView(withId(realEditText.id))

        // When
        ruleActivity.runOnUiThread {
            formField.setData(spinnerDataList)
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

        // When
        ruleActivity.runOnUiThread {
            formField.setData(spinnerDataList)
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
        val realEditText = formField.textInputLayout!!.editText!!
        val view = Espresso.onView(withId(realEditText.id))

        // When
        ruleActivity.runOnUiThread {
            formField.setData(spinnerDataList)
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
        val realEditText = formField.textInputLayout!!.editText!!
        val view = Espresso.onView(withId(realEditText.id))

        // When
        ruleActivity.runOnUiThread {
            formField.setData(spinnerDataList)
        }
        view.perform(click())
        onData(allOf(`is`(instanceOf(SpinnerData::class.java)), `is`(data1))).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        // Then
        Espresso.onView(withText("Cundinamarca")).check(matches(isDisplayed()))
        Assert.assertEquals(data1, (realEditText as AutoCompleteTextView).adapter.getItem(3))
    }

    @SmallTest
    @Test
    fun shouldShowStatesNotSorted() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val realEditText = formField.textInputLayout!!.editText!!
        val view = Espresso.onView(withId(realEditText.id))

        // When
        ruleActivity.runOnUiThread {
            formField.setData(spinnerDataListNotSorted, false)
        }
        view.perform(click())
        onData(allOf(`is`(instanceOf(SpinnerData::class.java)), `is`(data1))).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        // Then
        Espresso.onView(withText("Cundinamarca")).check(matches(isDisplayed()))
        Assert.assertEquals(data1, (realEditText as AutoCompleteTextView).adapter.getItem(1))
    }

    @SmallTest
    @Test
    fun shouldShowErrorIfNotElementIsSelected() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val error = String.format(MESSAGE_FORMAT_ERROR, "State")

        // When
        ruleActivity.runOnUiThread {
            formField.setIsRequired(true)
            formField.setData(spinnerDataList)
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

        // When
        ruleActivity.runOnUiThread {
            formField.setData(spinnerDataList)
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

        // When
        ruleActivity.runOnUiThread {
            formField.setData(spinnerDataList)
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
    fun shouldGetEmptyValueWithoutSelection() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        initData()
        // When
        ruleActivity.runOnUiThread {
            formField.setData(spinnerDataList)
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
        val realEditText = formField.textInputLayout!!.editText!!
        val view = Espresso.onView(withId(realEditText.id))

        // When
        ruleActivity.runOnUiThread {
            formField.setData(spinnerDataList)
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
    fun shouldBeAbleToSelectASpinnerElement() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val realEditText = formField.textInputLayout!!.editText!!
        val view = Espresso.onView(withId(realEditText.id))

        // When
        ruleActivity.runOnUiThread {
            formField.setData(spinnerDataList)
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

        var result: SpinnerData? = null
        formField?.setValueChangeListener(object : ValueChangeListener<SpinnerData?> {
            override fun onValueChange(value: SpinnerData?) {
                result = value
            }
        })

        // When
        ruleActivity.runOnUiThread {
            formField.setData(spinnerDataList)
            formField.setItemSelectedById(data2.id)
        }

        // Then
        Assert.assertEquals(data2, result)
    }

    @SmallTest
    @Test
    fun shouldBeDisable() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val realEditText = formField.textInputLayout!!.editText!!
        val view = Espresso.onView(withId(realEditText.id))

        // When
        ruleActivity.runOnUiThread {
            formField.setData(spinnerDataList)
        }
        view.perform(click())
        onData(
            allOf(
                `is`(instanceOf(SpinnerData::class.java)),
                `is`(data2)
            )
        ).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        ruleActivity.runOnUiThread {
            formField.setEnable(false)
        }

        // Then
        view.perform(click())
        Espresso.onView(withText("Atlantico")).check(matches(not(isEnabled())))
    }

    @SmallTest
    @Test
    fun shouldBeEnable() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val realEditText = formField.textInputLayout!!.editText!!
        val view = Espresso.onView(withId(realEditText.id))

        // When
        ruleActivity.runOnUiThread {
            formField.setData(spinnerDataList)
        }
        view.perform(click())
        onData(
            allOf(
                `is`(instanceOf(SpinnerData::class.java)),
                `is`(data2)
            )
        ).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        ruleActivity.runOnUiThread {
            formField.setEnable(false)
        }
        Espresso.onView(withText("Atlantico")).check(matches(not(isEnabled())))
        ruleActivity.runOnUiThread {
            formField.setEnable(true)
        }
        view.perform(click())
        onData(
            allOf(
                `is`(instanceOf(SpinnerData::class.java)),
                `is`(data1)
            )
        ).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        // Then
        Espresso.onView(withText("Cundinamarca")).check(matches(isEnabled()))
    }

    @SmallTest
    @Test
    fun shouldBeDisableFromXML() {
        // Given
        MockActivity.layout = R.layout.activity_spinner_disable

        // When
        restartActivity()

        // Then
        Espresso.onView(withId(R.id.tlState)).check(matches(not(isSpinnerEnable())))
    }

    @SmallTest
    @Test
    fun shouldBeEnableFromXML() {
        // Given
        MockActivity.layout = R.layout.activity_spinner_enable

        // When
        restartActivity()

        // Then
        Espresso.onView(withId(R.id.tlState)).check(matches(isSpinnerEnable()))
    }

    @SmallTest
    @Test
    fun shouldShowHint() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val realEditText = formField.textInputLayout!!.editText!!
        val view = Espresso.onView(withId(realEditText.id))

        // When
        ruleActivity.runOnUiThread {
            formField.setData(spinnerDataList)
            formField.setItemSelectedById(data2.id)
        }

        view.perform(click())
        onData(allOf(`is`(instanceOf(SpinnerData::class.java)), `is`(data2))).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        // Then
        Espresso.onView(withText("Atlantico")).check(matches(isDisplayed()))

        ruleActivity.runOnUiThread {
            formField.clearField()
        }

        // Then
        Assert.assertTrue(realEditText.text.toString() == EMPTY)
    }

    @SmallTest
    @Test
    fun shouldShowErrorIfDefaultItemIsSelectedAndFieldIsRequired() {
        restartActivity()

        // Given
        val error = String.format(MESSAGE_FORMAT_ERROR, "State")
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val textInputLayout = formField.textInputLayout ?: throw NullPointerException()
        val realEditText = formField.textInputLayout!!.editText!!
        val view = Espresso.onView(withId(realEditText.id))

        // When
        ruleActivity.runOnUiThread {
            formField.setIsRequired(true)
            formField.setData(spinnerDataList)
        }

        val result = formField.isValid()
        showErrorInInputLayout(textInputLayout, result.error)
        view.perform(click())
        onData(
            allOf(
                `is`(instanceOf(SpinnerData::class.java)), `is`(
                    SpinnerData(
                        formField.context.getString(co.condorlabs.customcomponents.R.string.spinner_default_hint),
                        formField.context.getString(co.condorlabs.customcomponents.R.string.spinner_default_hint)
                    )
                )
            )
        ).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        // Then
        Assert.assertEquals(
            ValidationResult(false, error),
            result
        )

        Assert.assertEquals(error, textInputLayout.error)
    }

    @SmallTest
    @Test
    fun shouldShowErrorIfAValidItemIsSelectedAndThenItsChangedToDefault() {
        restartActivity()

        // Given
        val error = String.format(MESSAGE_FORMAT_ERROR, "State")
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val textInputLayout = formField.textInputLayout ?: throw NullPointerException()
        val realEditText = formField.textInputLayout!!.editText!!
        val view = Espresso.onView(withId(realEditText.id))

        // When
        ruleActivity.runOnUiThread {
            formField.setIsRequired(true)
            formField.setData(spinnerDataList)
        }

        val result = formField.isValid()
        showErrorInInputLayout(textInputLayout, result.error)
        view.perform(click())
        onData(
            allOf(
                `is`(instanceOf(SpinnerData::class.java)), `is`(
                    data1
                )
            )
        ).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        view.perform(click())
        onData(
            allOf(
                `is`(instanceOf(SpinnerData::class.java)), `is`(
                    SpinnerData(
                        formField.context.getString(co.condorlabs.customcomponents.R.string.spinner_default_hint),
                        formField.context.getString(co.condorlabs.customcomponents.R.string.spinner_default_hint)
                    )
                )
            )
        ).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())

        // Then
        Assert.assertEquals(
            ValidationResult(false, error),
            result
        )

        Assert.assertEquals(error, textInputLayout.error)
    }

    @Test
    fun shouldCallSpinnerFormFieldListenerAfterSelectHint() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)
        val realEditText = formField.textInputLayout!!.editText!!
        val view = Espresso.onView(withId(realEditText.id))

        ruleActivity.runOnUiThread {
            formField.setSpinnerFormFieldListener(object : SpinnerFormFieldListener {
                override fun onFieldCleared() {
                    Assert.assertTrue(true)
                }
            })
            formField.setData(spinnerDataList)
        }

        // When
        view.perform(click())
        onData(
            allOf(
                `is`(instanceOf(SpinnerData::class.java)), `is`(
                    SpinnerData(
                        formField.context.getString(co.condorlabs.customcomponents.R.string.spinner_default_hint),
                        formField.context.getString(co.condorlabs.customcomponents.R.string.spinner_default_hint)
                    )
                )
            )
        ).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())
    }

    @Test
    fun shouldCallSpinnerFormFieldListenerWhenFieldWasClear() {
        restartActivity()

        // Given
        val formField = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)

        ruleActivity.runOnUiThread {
            formField.setSpinnerFormFieldListener(object : SpinnerFormFieldListener {
                override fun onFieldCleared() {
                    Assert.assertTrue(true)
                }
            })
            formField.setData(spinnerDataList)
        }

        // When
        ruleActivity.runOnUiThread {
            formField.clearField()
        }
    }

    @SmallTest
    @Test
    fun shouldHideKeyBoard() {
        MockActivity.layout = R.layout.activity_spinner_inside_form
        restartActivity()

        // Given
        val spinnerView = ruleActivity.activity.findViewById<SpinnerFormField>(R.id.spinner_view)
        ruleActivity.runOnUiThread {
            spinnerView.setData(spinnerDataList)
        }

        val editTextView = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.edittext_view)
        val typableView = Espresso.onView(withId(editTextView.textInputLayout!!.editText!!.id))
        typableView.perform(ViewActions.typeText("1"))

        Assert.assertTrue("The keyboard should be displayed", ruleActivity.activity.isKeyboardOpen())

        // When
        val clickableView = Espresso.onView(withId(R.id.spinner_view))
        clickableView.perform(click())

        // Then
        Assert.assertTrue("The keyboard should be hidden", ruleActivity.activity.isKeyboardClosed())
    }
}
