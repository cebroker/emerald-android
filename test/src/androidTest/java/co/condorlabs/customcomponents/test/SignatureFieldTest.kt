package co.condorlabs.customcomponents.test

import org.junit.Before

class SignatureFieldTest : MockActivityTest() {

    @Before
    fun setup() {
        MockActivity.layout = R.layout.activity_signature_field_test
        restartActivity()
    }
}