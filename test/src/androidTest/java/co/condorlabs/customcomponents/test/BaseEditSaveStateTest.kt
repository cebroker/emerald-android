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

        val formField1 = ruleActivity.activity.findViewById<BaseEditTextFormField>(R.id.firstField)
        val realEditText1 = formField1.editText!!
        val view1 = Espresso.onView(ViewMatchers.withId(realEditText1.id))
        view1.perform(ViewActions.typeText("12345"))

        val formField2 = (ruleActivity.activity.findViewById<View>(R.id.thirdField) as? EditTextDateField)
        val realEditText2 = formField2!!.editText!!
        val view2 = Espresso.onView(ViewMatchers.withId(realEditText2.id))
        view2.perform(ViewActions.typeText("12/01/2019"))

        val formField3 = (ruleActivity.activity.findViewById<View>(R.id.fourthField) as? EditTextEmailField)
        val realEditText3 = formField3!!.editText!!
        val view3 = Espresso.onView(ViewMatchers.withId(realEditText3.id))
        view3.perform(ViewActions.typeText("test@test.com"))

        val formField4 = (ruleActivity.activity.findViewById<View>(R.id.sixField) as? EditTextPhoneField)
        val realEditText4 = formField4!!.editText!!
        val view4 = Espresso.onView(ViewMatchers.withId(realEditText4.id))
        view4.perform(ViewActions.typeText("1234567890"))

        //When

        val formField5 = (ruleActivity.activity.findViewById<View>(R.id.btnGo) as? CustomButton)
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(formField5!!.id)).perform(ViewActions.click())
        Espresso.pressBack()

        //Then

        Assert.assertEquals("12345", realEditText1.text.toString())
        Assert.assertEquals("12/01/2019", realEditText2.text.toString())
        Assert.assertEquals("test@test.com", realEditText3.text.toString())
        Assert.assertEquals("123-456-7890", realEditText4.text.toString())
    }

}
