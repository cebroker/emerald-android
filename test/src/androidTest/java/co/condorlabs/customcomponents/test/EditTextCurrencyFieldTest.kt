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
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import co.condorlabs.customcomponents.EMPTY
import co.condorlabs.customcomponents.VALIDATE_EMPTY_ERROR
import co.condorlabs.customcomponents.customedittext.EditTextCurrencyField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.test.util.text
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditTextCurrencyFieldTest : MockActivityTest() {

    private var editTextRef = ViewMatchers.withId(R.id.etCurrency)
    private var editText: EditTextCurrencyField? = null

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_currencyedittextfield_test
        restartActivity()
        editText = ruleActivity.activity.findViewById(R.id.tlCurrency)
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
        onView(editTextRef).perform(ViewActions.typeText("1"))
        Assert.assertEquals("$1.00", editText?.text())

        onView(editTextRef).perform(ViewActions.typeText("1"))
        Assert.assertEquals("$11.00", editText?.text())

        onView(editTextRef).perform(ViewActions.typeText("2"))
        Assert.assertEquals("$112.00", editText?.text())

        onView(editTextRef).perform(ViewActions.typeText("2"))
        Assert.assertEquals("$1,122.00", editText?.text())

        onView(editTextRef).perform(ViewActions.typeText("33"))
        Assert.assertEquals("$112,233.00", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldFormatCurrencyWithMax2Decimals() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("0"))
        // Then
        Assert.assertEquals("$0", editText?.text())

        // When
        onView(editTextRef).perform(ViewActions.typeText("."))
        // Then
        Assert.assertEquals("$0.", editText?.text())

        // When
        onView(editTextRef).perform(ViewActions.typeText("0"))
        // Then
        Assert.assertEquals("$0.0", editText?.text())

        // When
        onView(editTextRef).perform(ViewActions.typeText("5"))
        // Then
        Assert.assertEquals("$0.05", editText?.text())

        // When
        onView(editTextRef).perform(ViewActions.typeText("8"))
        // Then
        Assert.assertEquals("$0.05", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldAllowToWriteZeroAsLastDecimal() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("1.40"))
        // Then
        Assert.assertEquals("$1.40", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldAllowMax1_000_000() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("1000000000001"))
        // Then
        Thread.sleep(5_000)
        Assert.assertEquals("$100,000,000,000.00", editText?.text())

        // When
        onView(editTextRef).perform(ViewActions.typeText("0"))
        // Then
        Thread.sleep(5_000)
        onView(editTextRef).perform(ViewActions.typeText("0"))
        Assert.assertEquals("$1,000,000,000,000.00", editText?.text())

        // When
        onView(editTextRef).perform(ViewActions.typeText("0"))
        // Then
        Assert.assertEquals("$1,000,000,000,000.00", editText?.text())

        // When
        onView(editTextRef).perform(ViewActions.typeText("10000000000001"))
        // Then
        Assert.assertEquals("$1,000,000,000,000.00", editText?.text())

        // When
        onView(editTextRef).perform(ViewActions.typeText("."))
        // Then
        Assert.assertEquals("$1,000,000,000,000.00", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldNotAllowPasteNumberBiggerThanMax() {
        // When
        onView(editTextRef).perform(ViewActions.replaceText("1000000000001"))
        // Then
        Assert.assertEquals("$1,000,000,000,000.00", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldDeleteDecimalPart() {
        // When
        onView(editTextRef).perform(ViewActions.typeText("2,333.05"))
        // Then
        Assert.assertEquals("$2,333.05", editText?.text())

        // When
        onView(editTextRef).perform(pressKey(KeyEvent.KEYCODE_DEL))
        // Then
        Assert.assertEquals("$2,333.0", editText?.text())

        // When
        onView(editTextRef).perform(pressKey(KeyEvent.KEYCODE_DEL))
        // Then
        Assert.assertEquals("$2,333.", editText?.text())

        // When
        onView(editTextRef).perform(pressKey(KeyEvent.KEYCODE_DEL))
        // Then
        Assert.assertEquals("$2,333", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldFormatDeleting() {
        // Given
        onView(editTextRef).perform(ViewActions.typeText("111,222,333"))

        // When
        onView(editTextRef).perform(pressKey(KeyEvent.KEYCODE_DEL))
        // Then
        Assert.assertEquals("$11,122,233", editText?.text())

        // When
        onView(editTextRef).perform(pressKey(KeyEvent.KEYCODE_DEL))
        // Then
        Assert.assertEquals("$1,112,223", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldShowEmptyOnStart() {
        // Then
        Assert.assertEquals(EMPTY, editText?.text())
    }

    @SmallTest
    @Test
    fun shouldShowDollarSymbolWhenType() {
        // Given
        val view = onView(editTextRef)

        // When
        view.perform(ViewActions.typeText("123"))

        // Then
        view.check(matches(ViewMatchers.withSubstring("$")))
    }

    @SmallTest
    @Test
    fun shouldShowDollarSymbolOnDelete() {
        // Given
        val view = onView(editTextRef)

        // When
        view.perform(ViewActions.typeText("1"))
            .perform(click())
            .perform(pressKey(KeyEvent.KEYCODE_DEL))

        // Then
        view.check(matches(ViewMatchers.withSubstring("$")))
    }
}
