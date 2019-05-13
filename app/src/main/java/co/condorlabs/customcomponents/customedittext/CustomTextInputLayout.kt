package co.condorlabs.customcomponents.customedittext

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import co.condorlabs.customcomponents.R
import com.google.android.material.textfield.TextInputLayout

/**
 * @author Oscar Gallon on 2019-05-13.
 */
class CustomTextInputLayout(context: Context, attributeSet: AttributeSet) :
    TextInputLayout(ContextThemeWrapper(context, R.style.TextFormFieldTheme), attributeSet)
