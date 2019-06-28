package co.condorlabs.customcomponents.customedittext.textwatchers

import android.text.Editable
import co.condorlabs.customcomponents.customedittext.ValueChangeListener
import co.condorlabs.customcomponents.helper.TextWatcherAdapter

/**
 * @author Oscar Gallon on 2019-06-27.
 */
open class DefaultTextWatcher(
    private var valueChangeListener: ValueChangeListener<String>?
) : TextWatcherAdapter() {

    override fun afterTextChanged(s: Editable?) {
        super.afterTextChanged(s)
        val newText = s?.toString() ?: return
        valueChangeListener?.onValueChange(newText)
    }

    fun setValueChangeListener(valueChangeListener: ValueChangeListener<String>) {
        this.valueChangeListener = valueChangeListener
    }
}
