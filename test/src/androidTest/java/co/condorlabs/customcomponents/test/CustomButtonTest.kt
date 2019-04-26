package co.condorlabs.customcomponents.test

import android.support.test.runner.AndroidJUnit4
import android.support.v4.content.ContextCompat
import co.condorlabs.customcomponents.custombutton.ColorGradientDrawable
import co.condorlabs.customcomponents.custombutton.CustomButton
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Oscar Gallon on 2019-04-26.
 */
@RunWith(AndroidJUnit4::class)
class CustomButtonTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_custom_button
    }

    @Test
    fun shouldBeDefaultButton() {
        restartActivity()

        // Given
        val expectedColor = ContextCompat.getColor(ruleActivity.activity, R.color.white)
        val button = ruleActivity.activity.findViewById<CustomButton>(R.id.btn)

        // When
        val backgroundColor = (button.background as? ColorGradientDrawable)?.getSolidColor()

        // Then
        Assert.assertEquals(expectedColor, backgroundColor)
    }

    @Test
    fun shouldBeSuccessButton() {
        restartActivity()

        // Given
        val expectedColor = ContextCompat.getColor(ruleActivity.activity, R.color.successColor)
        val button = ruleActivity.activity.findViewById<CustomButton>(R.id.btn)
        ruleActivity.runOnUiThread {
            button.setType("success")
        }

        // When
        val backgroundColor = (button.background as? ColorGradientDrawable)?.getSolidColor()

        // Then
        Assert.assertEquals(expectedColor, backgroundColor)
    }

    @Test
    fun shouldBeDangerButton() {
        restartActivity()

        // Given
        val expectedColor = ContextCompat.getColor(ruleActivity.activity, R.color.dangerColor)
        val button = ruleActivity.activity.findViewById<CustomButton>(R.id.btn)
        ruleActivity.runOnUiThread {
            button.setType("danger")
        }

        // When
        val backgroundColor = (button.background as? ColorGradientDrawable)?.getSolidColor()

        // Then
        Assert.assertEquals(expectedColor, backgroundColor)
    }

    @Test
    fun shouldBePrimaryButton() {
        restartActivity()

        // Given
        val expectedColor = ContextCompat.getColor(ruleActivity.activity, R.color.primaryColor)
        val button = ruleActivity.activity.findViewById<CustomButton>(R.id.btn)
        ruleActivity.runOnUiThread {
            button.setType("primary")
        }

        // When
        val backgroundColor = (button.background as? ColorGradientDrawable)?.getSolidColor()

        // Then
        Assert.assertEquals(expectedColor, backgroundColor)
    }

    @Test
    fun shouldBeInfoButton() {
        restartActivity()

        // Given
        val expectedColor = ContextCompat.getColor(ruleActivity.activity, R.color.infoColor)
        val button = ruleActivity.activity.findViewById<CustomButton>(R.id.btn)
        ruleActivity.runOnUiThread {
            button.setType("info")
        }

        // When
        val backgroundColor = (button.background as? ColorGradientDrawable)?.getSolidColor()

        // Then
        Assert.assertEquals(expectedColor, backgroundColor)
    }

    @Test
    fun shouldBeWarningButton() {
        restartActivity()

        // Given
        val expectedColor = ContextCompat.getColor(ruleActivity.activity, R.color.warningColor)
        val button = ruleActivity.activity.findViewById<CustomButton>(R.id.btn)
        ruleActivity.runOnUiThread {
            button.setType("warning")
        }

        // When
        val backgroundColor = (button.background as? ColorGradientDrawable)?.getSolidColor()

        // Then
        Assert.assertEquals(expectedColor, backgroundColor)
    }
}
