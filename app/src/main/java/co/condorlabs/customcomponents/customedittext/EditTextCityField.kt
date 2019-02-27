package co.condorlabs.customcomponents.customedittext

import android.content.Context
import android.util.AttributeSet
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.formfield.ValidationResult
import co.condorlabs.customcomponents.helper.EMPTY
import co.condorlabs.customcomponents.helper.VALIDATE_CITY_ERROR
import co.condorlabs.customcomponents.helper.ZERO

/**
 * @author Oscar Gallon on 2/25/19.
 */
class EditTextCityField(context: Context, attrs: AttributeSet) : BaseEditTextFormField(context, attrs) {

    private var mStateName: String? = null
    private var mCities: List<String>? = null


    override fun getErrorValidateResult(): ValidationResult {
        return ValidationResult(false, "$VALIDATE_CITY_ERROR ${mStateName ?: EMPTY}")
    }

    override fun isValid(): ValidationResult {
        val emptyValidation = super.isValid()

        if (!emptyValidation.isValid) {
            return emptyValidation
        }

        if (mCities?.filter {
                it.toLowerCase() == editText?.text?.toString()?.toLowerCase() ?: EMPTY
            }?.count() ?: ZERO <= ZERO) {
            return getErrorValidateResult()
        }

        return emptyValidation
    }

    override fun setup() {
        super.setup()
        mEditText?.id = R.id.etCity
    }


    fun setCities(cities: List<String>) {
        mCities = cities
    }

    fun setStateName(state: String) {
        mStateName = state
    }
}
