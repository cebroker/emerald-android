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
        editText?.setIsRequired(true)

        val result = editText?.isValid()

        Assert.assertEquals(
            ValidationResult(false, String.format(VALIDATE_EMPTY_ERROR, "Enter some text")),
            result
        )
    }

    @SmallTest
    @Test
    fun shouldFormatCurrency() {
        onView(editTextRef).perform(ViewActions.typeText("1"))
        Assert.assertEquals("$0.01", editText?.text())
        onView(editTextRef).perform(ViewActions.typeText("00"))
        Assert.assertEquals("$1.00", editText?.text())
        onView(editTextRef).perform(ViewActions.typeText("000"))
        Assert.assertEquals("$1,000.00", editText?.text())
        onView(editTextRef).perform(ViewActions.typeText("000"))
        Assert.assertEquals("$1,000,000.00", editText?.text())
        onView(editTextRef).perform(ViewActions.typeText("50"))
        Assert.assertEquals("$100,000,000.50", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldFormatCurrency2Decimals() {
        onView(editTextRef).perform(ViewActions.typeText("5"))
        Assert.assertEquals("$0.05", editText?.text())
        onView(editTextRef).perform(ViewActions.typeText("8"))
        Assert.assertEquals("$0.58", editText?.text())
        onView(editTextRef).perform(ViewActions.typeText("0"))
        Assert.assertEquals("$5.80", editText?.text())
        onView(editTextRef).perform(ViewActions.typeText("0"))
        Assert.assertEquals("$58.00", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldAllowToWriteZeroAsLastDecimal() {
        onView(editTextRef).perform(ViewActions.typeText("1.40"))
        Assert.assertEquals("$1.40", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldAllowMax1_000_000_000_000() {
        onView(editTextRef).perform(ViewActions.typeText("100000000000"))
        Assert.assertEquals("$1,000,000,000.00", editText?.text())

        onView(editTextRef).perform(ViewActions.typeText("00"))
        Assert.assertEquals("$100,000,000,000.00", editText?.text())

        onView(editTextRef).perform(ViewActions.typeText("0"))
        Assert.assertEquals("$1,000,000,000,000.00", editText?.text())

        onView(editTextRef).perform(ViewActions.typeText("10000000000001"))
        Assert.assertEquals("$1,000,000,000,000.00", editText?.text())

        onView(editTextRef).perform(ViewActions.typeText("."))
        Assert.assertEquals("$1,000,000,000,000.00", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldNotAllowPasteNumberBiggerThanMax() {
        onView(editTextRef).perform(ViewActions.replaceText("100000000000001"))
        Assert.assertEquals("$1,000,000,000,000.00", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldFormatDeleting() {
        onView(editTextRef).perform(ViewActions.typeText("2,333.05"))
        Assert.assertEquals("$2,333.05", editText?.text())

        onView(editTextRef).perform(pressKey(KeyEvent.KEYCODE_DEL))
        Assert.assertEquals("$233.30", editText?.text())

        onView(editTextRef).perform(pressKey(KeyEvent.KEYCODE_DEL))
        Assert.assertEquals("$23.33", editText?.text())

        onView(editTextRef).perform(pressKey(KeyEvent.KEYCODE_DEL))
        Assert.assertEquals("$2.33", editText?.text())

        onView(editTextRef).perform(pressKey(KeyEvent.KEYCODE_DEL))
        Assert.assertEquals("$0.23", editText?.text())

        onView(editTextRef).perform(pressKey(KeyEvent.KEYCODE_DEL))
        Assert.assertEquals("$0.02", editText?.text())

        onView(editTextRef).perform(pressKey(KeyEvent.KEYCODE_DEL))
        Assert.assertEquals("$0.00", editText?.text())
    }

    @SmallTest
    @Test
    fun shouldShowEmptyOnStart() {
        Assert.assertEquals(EMPTY, editText?.text())
    }

    @SmallTest
    @Test
    fun shouldShowDollarSymbolWhenType() {
        val view = onView(editTextRef)

        view.perform(ViewActions.typeText("123"))

        view.check(matches(ViewMatchers.withSubstring("$")))
    }

    @SmallTest
    @Test
    fun shouldShowDollarSymbolOnDelete() {
        val view = onView(editTextRef)

        view.perform(ViewActions.typeText("1"))
            .perform(click())
            .perform(pressKey(KeyEvent.KEYCODE_DEL))

        view.check(matches(ViewMatchers.withSubstring("$")))

        onView(editTextRef).perform(pressKey(KeyEvent.KEYCODE_DEL))
        view.check(matches(ViewMatchers.withSubstring("$")))
    }
}
