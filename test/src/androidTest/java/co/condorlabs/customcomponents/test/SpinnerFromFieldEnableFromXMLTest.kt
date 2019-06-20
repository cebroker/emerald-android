package co.condorlabs.customcomponents.test

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.SmallTest
import co.condorlabs.customcomponents.customspinner.SpinnerFormField
import co.condorlabs.customcomponents.test.util.isDisable
import org.junit.Before
import org.junit.Test

/**
 * Created by Oscar Tigreros on 20,June,2019
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

class SpinnerFromFieldEnableFromXMLTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_spinner_enable_from_xml
    }

    @SmallTest
    @Test
    fun shouldBeDisable(){
        restartActivity()

        //Given
        val spinner =   ruleActivity.activity.findViewById<SpinnerFormField>(R.id.tlState)

        //Then
        Espresso.onView(ViewMatchers.withId(R.id.tlState)).check(matches(isDisable()))
    }
}
