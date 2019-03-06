package co.condorlabs.customcomponents.test

import android.view.View
import android.view.ViewGroup
import co.condorlabs.customcomponents.generalvalidator.ValidableView
import co.condorlabs.customcomponents.helper.ONE
import org.junit.Assert
import org.junit.Test

class ValidableViewTest : MockActivityTest() {


    @Test
    fun shouldInitFormField() {
        MockActivity.layout = R.layout.activity_general_validate_test
        restartActivity()

        //Give
        val viewGroup = (ruleActivity.activity?.findViewById<View>(R.id.llContent)?.let { it } as? ViewGroup)
            ?: throw RuntimeException()
        val view = ruleActivity.activity as? ValidableView

        //When
        view?.addCustomComponents(viewGroup)
        val formField = view?.mFormFields

        //Then
        Assert.assertTrue(formField?.size ?: 0 > 0)
    }

    @Test
    fun shouldNotBeValid() {
        MockActivity.layout = R.layout.activity_general_validate_test
        restartActivity()

        //Give
        val viewGroup = (ruleActivity.activity?.findViewById<View>(R.id.llContent)?.let { it } as? ViewGroup)
            ?: throw RuntimeException()
        val view = ruleActivity.activity as? ValidableView

        //When
        view?.addCustomComponents(viewGroup)
        var isViewValid = false
        ruleActivity.activity?.runOnUiThread {
            isViewValid = view?.areComponentsValid() ?: false
        }

        //Then
        Assert.assertFalse(isViewValid)
    }

    @Test
    fun shouldCountOnlyCustomViews() {
        MockActivity.layout = R.layout.activity_general_validate_test
        restartActivity()

        //Give
        val viewGroup = (ruleActivity.activity?.findViewById<View>(R.id.llContent)?.let { it } as? ViewGroup)
            ?: throw RuntimeException()
        val view = ruleActivity.activity as? ValidableView

        //When
        view?.addCustomComponents(viewGroup)

        //Then
        Assert.assertEquals(view?.mFormFields?.size, ONE)
    }

}