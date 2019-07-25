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

import android.content.Intent
import com.google.android.material.textfield.TextInputLayout
import androidx.test.rule.ActivityTestRule
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule

/**
 * @author Oscar Gallon on 2/25/19.
 */
abstract class MockActivityTest2 {

    @Rule
    @JvmField
    val ruleActivity = ActivityTestRule(BaseEditSaveStateActivity::class.java, true, false)

    fun restartActivity() {
        if (ruleActivity.activity != null) {
            ruleActivity.finishActivity()
        }
        ruleActivity.launchActivity(Intent())
    }

    fun showErrorInInputLayout(textInputLayout: TextInputLayout, error: String) {
        ruleActivity.runOnUiThread {
            textInputLayout.error = error
        }
    }

    fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View> {
        return object : TypeSafeMatcher<View>() {

            override fun matchesSafely(view: View): Boolean {
                if (view !is TextInputLayout) {
                    return false
                }

                val error = view.error ?: return false

                val hint = error.toString()

                return expectedErrorText == hint
            }

            override fun describeTo(description: Description) {}
        }
    }
}
