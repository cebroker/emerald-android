package co.condorlabs.customcomponents

import android.content.Intent
import android.support.design.widget.TextInputLayout
import android.support.test.rule.ActivityTestRule
import org.junit.Rule

/**
 * @author Oscar Gallon on 2/25/19.
 */
abstract class MockActivityTest {

    @Rule
    @JvmField
    val ruleActivity = ActivityTestRule(MockActivity::class.java, true, false)

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
}
