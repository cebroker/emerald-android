package co.condorlabs.customcomponents

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.widget.Spinner
import co.condorlabs.customcomponents.customspinner.StateListener
import co.condorlabs.customcomponents.customspinner.StateSpinnerFormField
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.MESSAGE_FORMAT_ERROR
import org.hamcrest.CoreMatchers.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test


/**
 * @author Oscar Gallon on 2/26/19.
 */
class StateSpinnerFormFieldTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_statespinnerformfield_test
    }

    @Test
    fun shouldDisplayLabel() {
        restartActivity()

        //Given
        val view = Espresso.onView(withText("State"))

        //When
        view.perform(click())

        //Then
        view.check(matches(isDisplayed()))
    }

    @Test
    fun shouldFindLabelById() {
        restartActivity()

        //Given
        val view = Espresso.onView(withId(R.id.tvLabel))

        //When
        view.perform(click())

        //Then
        view.check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplaySpinner() {
        restartActivity()

        //Given
        val view = Espresso.onView(withId(R.id.spState))

        //When

        //Then
        view.check(matches(isDisplayed()))

    }

    @Test
    fun shouldShowStatesOnSpinner() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<StateSpinnerFormField>(R.id.tlState)
        val view = Espresso.onView(withId(R.id.spState))

        //When
        ruleActivity.runOnUiThread {
            formField.setStates(arrayListOf("Antioquia", "Cundinamarca", "Atlantico"))
        }
        view.perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Cundinamarca"))).perform(click())

        //Then

        Espresso.onView(withText("Cundinamarca")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowStatesSorted() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<StateSpinnerFormField>(R.id.tlState)
        val spinner = ruleActivity.activity.findViewById<Spinner>(R.id.spState)
        val view = Espresso.onView(withId(R.id.spState))

        //When
        ruleActivity.runOnUiThread {
            formField.setStates(arrayListOf("Antioquia", "Cundinamarca", "Atlantico"))
        }
        view.perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Cundinamarca"))).perform(click())

        //Then
        Espresso.onView(withText("Cundinamarca")).check(matches(isDisplayed()))
        Assert.assertEquals(2, spinner.selectedItemPosition)
    }

    @Test
    fun shouldShowErrorIfNotElementIsSelected() {
        restartActivity()

        //Given
        val formField = ruleActivity.activity.findViewById<StateSpinnerFormField>(R.id.tlState)
        val error = String.format(MESSAGE_FORMAT_ERROR, "State")

        //When
        ruleActivity.runOnUiThread {
            formField.setStates(arrayListOf("Antioquia", "Cundinamarca", "Atlantico"))
        }
        val result = formField.isValid()
        showErrorInInputLayout(formField, result.error)

        //Then
        Assert.assertEquals(
            ValidationResult(false, error),
            result
        )

        Assert.assertEquals(error, formField.error)
    }

    @Test
    fun shouldCallListenerOnItemSelected() {
        restartActivity()

        //Given
        var selectedState = ""
        val formField = ruleActivity.activity.findViewById<StateSpinnerFormField>(R.id.tlState)
        val listener = object : StateListener {
            override fun onStateSetListener(state: String) {
                selectedState = state

            }
        }
        val view = Espresso.onView(withId(R.id.spState))

        //When
        ruleActivity.runOnUiThread {
            formField.setStates(arrayListOf("Antioquia", "Cundinamarca", "Atlantico"))
        }
        formField.setOnStateSetListener(listener)
        view.perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Atlantico"))).perform(click())
        Assert.assertEquals("Atlantico", selectedState)
    }
}
