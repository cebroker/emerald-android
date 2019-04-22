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

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.runner.AndroidJUnit4
import android.view.View
import co.condorlabs.customcomponents.customedittext.EditTextCurrencyField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.VALIDATE_EMPTY_ERROR
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
        editText = ruleActivity.activity.findViewById<View>(R.id.tlCurrency) as? EditTextCurrencyField
    }

    @Test
    fun shouldShowAndErrorWithEmptyCurrency() {

        //Given
        editText?.setIsRequired(true)

        //When
        val result = editText?.isValid()

        //Then
        Assert.assertEquals(
            ValidationResult(false, String.format(VALIDATE_EMPTY_ERROR,"Enter some text")),
            result
        )
    }

    @Test
    fun shouldFormatCurrency() {

        onView(editTextRef).perform(ViewActions.typeText("1"))
        //Then
        Assert.assertEquals("$1", editText?.text())

        onView(editTextRef).perform(ViewActions.typeText("1"))
        //Then
        Assert.assertEquals("$11", editText?.text())

        //When
        onView(editTextRef).perform(ViewActions.typeText("2"))
        //Then
        Assert.assertEquals("$112", editText?.text())

        //When
        onView(editTextRef).perform(ViewActions.typeText("2"))
        //Then
        Assert.assertEquals("$1,122", editText?.text())

        //When
        onView(editTextRef).perform(ViewActions.typeText("33"))
        //Then
        Assert.assertEquals("$112,233", editText?.text())
    }

    @Test
    fun shouldFormatCurrencyWithMax2Decimals() {

        //When
        onView(editTextRef).perform(ViewActions.typeText("0"))
        //Then
        Assert.assertEquals("$0", editText?.text())

        //When
        onView(editTextRef).perform(ViewActions.typeText("."))
        //Then
        Assert.assertEquals("$0.", editText?.text())

        //When
        onView(editTextRef).perform(ViewActions.typeText("0"))
        //Then
        Assert.assertEquals("$0.0", editText?.text())

        //When
        onView(editTextRef).perform(ViewActions.typeText("5"))
        //Then
        Assert.assertEquals("$0.05", editText?.text())

        //When
        onView(editTextRef).perform(ViewActions.typeText("8"))
        //Then
        Assert.assertEquals("$0.05", editText?.text())
    }

    @Test
    fun shouldAllowMax1_000_000() {

        //When
        onView(editTextRef).perform(ViewActions.typeText("1000001"))
        //Then
        Assert.assertEquals("$100,000", editText?.text())

        //When
        onView(editTextRef).perform(ViewActions.typeText("0"))
        //Then
        Assert.assertEquals("$1,000,000", editText?.text())

        //When
        onView(editTextRef).perform(ViewActions.typeText("0"))
        //Then
        Assert.assertEquals("$1,000,000", editText?.text())

        //When
        onView(editTextRef).perform(ViewActions.typeText("10000001"))
        //Then
        Assert.assertEquals("$1,000,000", editText?.text())

        //When
        onView(editTextRef).perform(ViewActions.typeText("."))
        //Then
        Assert.assertEquals("$1,000,000", editText?.text())
    }

    @Test
    fun shouldNotAllowPasteNumberBiggerThanMax() {

        //When
        onView(editTextRef).perform(ViewActions.replaceText("1000001"))
        //Then
        Assert.assertEquals("", editText?.text())
    }
}
