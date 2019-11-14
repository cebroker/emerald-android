package co.condorlabs.customcomponents.test.simplecamerax

import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import co.condorlabs.customcomponents.custombutton.CustomButton
import co.condorlabs.customcomponents.simplecamerax.WalletCameraActivity
import co.condorlabs.customcomponents.test.MockActivity
import co.condorlabs.customcomponents.test.R
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author Alexis Duque on 2019-11-14.
 * @company Condor Labs.
 * @email eduque@condorlabs.io.
 */
class WalletCameraActivityTest {

    @Rule
    @JvmField
    val ruleActivity = ActivityTestRule(WalletCameraActivity::class.java, true, false)

    @Before
    fun grantPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().uiAutomation.executeShellCommand("pm grant " + getTargetContext().packageName + " android.permission.CAMERA")
        }
    }

    @Before
    fun setup() {
        ruleActivity.launchActivity(Intent())
    }

    @Test
    @LargeTest
    fun shouldDisplayDefaultScreen() {
        // Given
        val tvTitle = ruleActivity.activity.findViewById<TextView>(R.id.cameraTitle)
        val tvDescription = ruleActivity.activity.findViewById<TextView>(R.id.capturePhotoDescription)
        val cancelButton = ruleActivity.activity.findViewById<CustomButton>(R.id.cancelPhoto)
        val cropButton = ruleActivity.activity.findViewById<CustomButton>(R.id.cropPhoto)
        val captureButton = ruleActivity.activity.findViewById<AppCompatImageButton>(R.id.captureButton)

        // When
        val titleText = tvTitle.text
        val descriptionText = tvDescription.text
        val btnCancelVisibility = cancelButton.visibility
        val btnCropCancelVisibility = cropButton.visibility
        val btnCaptureVisibility = captureButton.visibility
        val descriptionVisibility = tvDescription.visibility

        // Then
        Assert.assertEquals("Front", titleText)
        Assert.assertEquals("Touch to take photo", descriptionText)
        Assert.assertEquals(View.INVISIBLE, btnCancelVisibility)
        Assert.assertEquals(View.INVISIBLE, btnCropCancelVisibility)
        Assert.assertEquals(View.VISIBLE, btnCaptureVisibility)
        Assert.assertEquals(View.VISIBLE, descriptionVisibility)
    }

    @Test
    @LargeTest
    fun shouldTakeAPhoto() {
        // Given
        val tvTitle = ruleActivity.activity.findViewById<TextView>(R.id.cameraTitle)
        val tvDescription = ruleActivity.activity.findViewById<TextView>(R.id.capturePhotoDescription)
        val cancelButton = ruleActivity.activity.findViewById<CustomButton>(R.id.cancelPhoto)
        val cropButton = ruleActivity.activity.findViewById<CustomButton>(R.id.cropPhoto)
        val captureButton = ruleActivity.activity.findViewById<AppCompatImageButton>(R.id.captureButton)

        // When
        ruleActivity.runOnUiThread {
            captureButton.performClick()
        }
        Thread.sleep(2000)

        // Then
        Assert.assertEquals("Retake", cancelButton.text)
        Assert.assertEquals("Use photo", cropButton.text)
        Assert.assertEquals("Front", tvTitle.text)
        Assert.assertEquals(View.VISIBLE, cancelButton.visibility)
        Assert.assertEquals(View.VISIBLE, cropButton.visibility)
        Assert.assertEquals(View.INVISIBLE, captureButton.visibility)
        Assert.assertEquals(View.INVISIBLE, tvDescription.visibility)
    }

    @Test
    @LargeTest
    fun shouldTakeAPhotoAndCancel() {
        // Given
        val tvTitle = ruleActivity.activity.findViewById<TextView>(R.id.cameraTitle)
        val tvDescription = ruleActivity.activity.findViewById<TextView>(R.id.capturePhotoDescription)
        val cancelButton = ruleActivity.activity.findViewById<CustomButton>(R.id.cancelPhoto)
        val cropButton = ruleActivity.activity.findViewById<CustomButton>(R.id.cropPhoto)
        val captureButton = ruleActivity.activity.findViewById<AppCompatImageButton>(R.id.captureButton)
        ruleActivity.runOnUiThread { captureButton.performClick() }
        Thread.sleep(2000)

        // When
        ruleActivity.runOnUiThread {
            cancelButton.performClick()
        }

        // Then
        Assert.assertEquals("Front", tvTitle.text)
        Assert.assertEquals("Touch to take photo", tvDescription.text)
        Assert.assertEquals(View.VISIBLE, tvDescription.visibility)
        Assert.assertEquals(View.INVISIBLE, cancelButton.visibility)
        Assert.assertEquals(View.INVISIBLE, cropButton.visibility)
        Assert.assertEquals(View.VISIBLE, captureButton.visibility)
    }

    @Test
    @LargeTest
    fun shouldTakeAPhotoAndCrop() {
        // Given
        val cropButton = ruleActivity.activity.findViewById<CustomButton>(R.id.cropPhoto)
        val captureButton = ruleActivity.activity.findViewById<AppCompatImageButton>(R.id.captureButton)
        ruleActivity.runOnUiThread { captureButton.performClick() }
        Thread.sleep(2000)

        // When
        ruleActivity.runOnUiThread {
            cropButton.performClick()
        }
    }
}
