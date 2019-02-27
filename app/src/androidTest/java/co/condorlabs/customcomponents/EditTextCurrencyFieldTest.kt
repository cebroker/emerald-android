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
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import co.condorlabs.customcomponents.customedittext.EditTextCurrencyField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.VALIDATE_EMPTY_ERROR
import org.junit.*

class EditTextCurrencyFieldTest : MockActivityTest() {


    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_currencyedittextfield_test
    }

    @Test
    fun shouldShowAndErrorWithEmptyCurrency() {
        restartActivity()

        //Given
        val view = (ruleActivity.activity.findViewById<View>(R.id.tlCurrency) as? EditTextCurrencyField)
        view?.setIsRequired(true)

        //When
        val result = view?.isValid()

        //Then
        Assert.assertEquals(
            ValidationResult(false, VALIDATE_EMPTY_ERROR),
            result
        )
    }

    @Test
    fun shouldFormatCurrency() {
        restartActivity()

        val view = Espresso.onView(ViewMatchers.withId(R.id.etCurrency))

        //When
        view.perform(ViewActions.typeText("22222"))

        //Then
        ViewMatchers.withText("$22,222").matches(view)
    }
}
