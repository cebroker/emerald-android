package co.condorlabs.customcomponents.formfield

interface FormField {

    var mIsRequired: Boolean

    fun isValid(): ValidationResult

    fun showError(message: String)

    fun clearError()

    fun setup()
}
