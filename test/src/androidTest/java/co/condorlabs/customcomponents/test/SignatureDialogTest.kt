package co.condorlabs.customcomponents.test

import android.graphics.Bitmap
import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import co.condorlabs.customcomponents.THREE_SECONDS
import co.condorlabs.customcomponents.customsignature.SignatureDialog
import junit.framework.Assert.assertNotNull
import org.junit.Assert
import org.junit.Test

class SignatureDialogTest {

    @Test
    fun shouldOpenDialog() {
        with(launchFragment<SignatureDialog>()) {
            var signatureDialog: SignatureDialog? = null
            onFragment { fragment ->
                signatureDialog = fragment
            }
            Assert.assertNotNull(signatureDialog?.dialog)
        }
    }

    @Test
    fun shouldCloseDialogByClickingCloseButton() {
        with(launchFragment<SignatureDialog>()) {
            var signatureDialog: SignatureDialog? = null
            onFragment { fragment ->
                signatureDialog = fragment
            }
            onView(withId(R.id.ivClose)).perform(click())
            Assert.assertEquals(signatureDialog?.dialog, null)
        }
    }

    @Test
    fun shouldShowOkButton() {
        launchFragment<SignatureDialog>()
        onView(withId(R.id.btnDoneSigning)).check(matches(withText("Done signing")))
    }

    @Test
    fun shouldReturnBitmap() {
        with(launchFragment<SignatureDialog>()) {
            var bitmapResult: Bitmap? = null
            val listener = object : SignatureDialog.OnDoneSignatureListener {
                override fun onDoneSignature(bitmap: Bitmap) {
                    bitmapResult = bitmap
                }
            }
            onFragment {
                it.setOnSignatureDoneListener(listener)
            }
            onView(withId(R.id.signatureView)).perform(swipeDown())
            onView(withId(R.id.btnDoneSigning)).perform(click())
            Thread.sleep(THREE_SECONDS)
            assertNotNull(bitmapResult)
        }
    }

    @Test
    fun shouldDisableOkButtonIfSignIsEmpty() {
        with(launchFragment<SignatureDialog>()) {
            var signatureDialog: SignatureDialog? = null
            onFragment { fragment ->
                signatureDialog = fragment
            }
            onView(withId(R.id.btnDoneSigning)).perform(click())
            onView(withId(R.id.btnDoneSigning)).check(matches(withText("Done signing")))
            onView(withId(R.id.btnClearSignature)).perform(click())
            onView(withId(R.id.signatureView)).perform(swipeDown())
            onView(withId(R.id.btnDoneSigning)).perform(click())
            Assert.assertEquals(signatureDialog?.dialog, null)
        }
    }
}
