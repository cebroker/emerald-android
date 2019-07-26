package co.condorlabs.customcomponents.test

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import co.condorlabs.customcomponents.custombutton.CustomButton
import co.condorlabs.customcomponents.customedittext.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Oscar Tigreros on 25,July,2019
 */
@RunWith(AndroidJUnit4::class)
class BaseEditSaveStateTest : MockActivityTest2() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_base_edit_save_state
    }

    @SmallTest
    @Test
    fun shouldSaveAndRestoreState() {
        restartActivity()

        //Given

        val formField = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.one)
        val realEditText = formField.editText!!
        val editText = Espresso.onView(ViewMatchers.withId(realEditText.id))
        editText.perform(ViewActions.typeText("12345"))

        val formField3 = (ruleActivity.activity.findViewById<View>(R.id.four) as? EditTextDateField)
        val realEditText3 = formField3!!.editText!!
        val view = Espresso.onView(ViewMatchers.withId(realEditText3.id))
        view.perform(ViewActions.typeText("12/01/2019"))

        val formField4 = (ruleActivity.activity.findViewById<View>(R.id.five) as? EditTextEmailField)
        val realEditText4 = formField4!!.editText!!
        val view2 = Espresso.onView(ViewMatchers.withId(realEditText4.id))
        view2.perform(ViewActions.typeText("test@test.com"))

        val formField5 = (ruleActivity.activity.findViewById<View>(R.id.six) as? EditTextPhoneField)
        val realEditText5 = formField5!!.editText!!
        val view3 = Espresso.onView(ViewMatchers.withId(realEditText5.id))
        view3.perform(ViewActions.typeText("1234567890"))

        //When

        val formField6 = (ruleActivity.activity.findViewById<View>(R.id.btnGo) as? CustomButton)
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(formField6!!.id)).perform(ViewActions.click())
        Espresso.pressBack()

        //Then

        Assert.assertEquals("12345", realEditText.text.toString())
        Assert.assertEquals("12/01/2019", realEditText3.text.toString())
        Assert.assertEquals("test@test.com", realEditText4.text.toString())
        Assert.assertEquals("123-456-7890", realEditText5.text.toString())
    }

}
