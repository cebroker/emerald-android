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

import android.view.KeyEvent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import co.condorlabs.customcomponents.DOLLAR_SYMBOL
import co.condorlabs.customcomponents.VALIDATE_EMPTY_ERROR
import co.condorlabs.customcomponents.customedittext.EditTextCurrencyField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.test.util.text
import org.hamcrest.Matcher
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditTextCurrencyFieldTest : MockActivityTest() {

    private var editText: EditTextCurrencyField? = null

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_currencyedittextfield_test
        restartActivity()
        editText = ruleActivity.activity.findViewById<View>(R.id.tlCurrency) as? EditTextCurrencyField
    }

    @SmallTest
    @Test
    fun shouldShowAndErrorWithEmptyCurrency() {
        // Given
        editText?.setIsRequired(true)

        // When
        val result = editText?.isValid()

        // Then
        Assert.assertEquals(
            ValidationResult(false, String.format(VALIDATE_EMPTY_ERROR, "Enter some text")),
            result
        )
    }

    @SmallTest
    @Test
    fun shouldFormatCurrency() {
        //Given
        val formField = ruleActivity.activity.findViewById<EditTextCurrencyField>(R.id.tlCurrency)
        val view = onView(ViewMatchers.withId(formField!!.editText!!.id))
        // When
        view.perform(ViewActions.typeText("1"))
        // Then
        Assert.assertEquals("$1", editText?.text())

        // When
        view.perform(ViewActions.typeText("1"))
        // Then
        Assert.assertEquals("$11", editText?.text())

        // When
        view.perform(ViewActions.typeText("2"))
        // Then
        Assert.assertEquals("$112", editText?.text())

        // When
        view.perform(ViewActions.typeText("2"))
        // Then
        Assert.assertEquals("$1,122", editText?.text())

        // When
        view.perform(ViewActions.typeText("33"))
        // Then
        Assert.assertEquals("$112,233", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldFormatCurrencyWithMax2Decimals() {
        //Given
        val formField = ruleActivity.activity.findViewById<EditTextCurrencyField>(R.id.tlCurrency)
        val view = onView(ViewMatchers.withId(formField!!.editText!!.id))
        // When
        view.perform(ViewActions.typeText("0"))
        // Then
        Assert.assertEquals("$0", editText?.text())

        // When
        view.perform(ViewActions.typeText("."))
        // Then
        Assert.assertEquals("$0.", editText?.text())

        // When
        view.perform(ViewActions.typeText("0"))
        // Then
        Assert.assertEquals("$0.0", editText?.text())

        // When
        view.perform(ViewActions.typeText("5"))
        // Then
        Assert.assertEquals("$0.05", editText?.text())

        // When
        view.perform(ViewActions.typeText("8"))
        // Then
        Assert.assertEquals("$0.05", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldAllowToWriteZeroAsLastDecimal() {
        //Given
        val formField = ruleActivity.activity.findViewById<EditTextCurrencyField>(R.id.tlCurrency)
        val view = onView(ViewMatchers.withId(formField!!.editText!!.id))
        // When
        view.perform(ViewActions.typeText("1.40"))
        // Then
        Assert.assertEquals("$1.40", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldAllowMax1_000_000() {
        //Given
        val formField = ruleActivity.activity.findViewById<EditTextCurrencyField>(R.id.tlCurrency)
        val view = onView(ViewMatchers.withId(formField!!.editText!!.id))

        // When
        view.perform(ViewActions.typeText("1000000000001"))
        // Then
        Assert.assertEquals("$100,000,000,000", editText?.text())

        // When
        view.perform(ViewActions.typeText("0"))
        // Then
        Assert.assertEquals("$1,000,000,000,000", editText?.text())

        // When
        view.perform(ViewActions.typeText("0"))
        // Then
        Assert.assertEquals("$1,000,000,000,000", editText?.text())

        // When
        view.perform(ViewActions.typeText("10000000000001"))
        // Then
        Assert.assertEquals("$1,000,000,000,000", editText?.text())

        // When
        view.perform(ViewActions.typeText("."))
        // Then
        Assert.assertEquals("$1,000,000,000,000", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldNotAllowPasteNumberBiggerThanMax() {
        //Given
        val formField = ruleActivity.activity.findViewById<EditTextCurrencyField>(R.id.tlCurrency)
        val view = onView(ViewMatchers.withId(formField!!.editText!!.id))
        // When
        view.perform(ViewActions.replaceText("1000000000001"))
        // Then
        Assert.assertEquals(DOLLAR_SYMBOL, editText?.text())
    }

    @SmallTest
    @Test
    fun shouldDeleteDecimalPart() {
        //Given
        val formField = ruleActivity.activity.findViewById<EditTextCurrencyField>(R.id.tlCurrency)
        val view = onView(ViewMatchers.withId(formField!!.editText!!.id))
        // When
        view.perform(ViewActions.typeText("2,333.05"))
        // Then
        Assert.assertEquals("$2,333.05", editText?.text())

        // When
        view.perform(pressKey(KeyEvent.KEYCODE_DEL))
        // Then
        Assert.assertEquals("$2,333.0", editText?.text())

        // When
        view.perform(pressKey(KeyEvent.KEYCODE_DEL))
        // Then
        Assert.assertEquals("$2,333.", editText?.text())

        // When
        view.perform(pressKey(KeyEvent.KEYCODE_DEL))
        // Then
        Assert.assertEquals("$2,333", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldFormatDeleting() {
        //Given
        val formField = ruleActivity.activity.findViewById<EditTextCurrencyField>(R.id.tlCurrency)
        val view = onView(ViewMatchers.withId(formField!!.editText!!.id))
        // Given
        view.perform(ViewActions.typeText("111,222,333"))

        // When
        view.perform(pressKey(KeyEvent.KEYCODE_DEL))
        // Then
        Assert.assertEquals("$11,122,233", editText?.text())

        // When
        view.perform(pressKey(KeyEvent.KEYCODE_DEL))
        // Then
        Assert.assertEquals("$1,112,223", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldShowDollarSymbolOnStart() {
        // Then
        Assert.assertEquals(DOLLAR_SYMBOL, editText?.text())
    }

    @SmallTest
    @Test
    fun shouldShowDollarSymbolWhenType() {
        // Given
        val formField = ruleActivity.activity.findViewById<EditTextCurrencyField>(R.id.tlCurrency)
        val view = onView(ViewMatchers.withId(formField!!.editText!!.id))

        // When
        view.perform(ViewActions.typeText("123"))

        // Then
        view.check(matches(ViewMatchers.withSubstring("$")))
    }

    @SmallTest
    @Test
    fun shouldShowDollarSymbolOnDelete() {
        // Given
        val formField = ruleActivity.activity.findViewById<EditTextCurrencyField>(R.id.tlCurrency)
        val view = onView(ViewMatchers.withId(formField!!.editText!!.id))

        // When
        view.perform(ViewActions.typeText("1"))
            .perform(click())
            .perform(pressKey(KeyEvent.KEYCODE_DEL))

        // Then
        view.check(matches(ViewMatchers.withSubstring("$")))
    }
}
