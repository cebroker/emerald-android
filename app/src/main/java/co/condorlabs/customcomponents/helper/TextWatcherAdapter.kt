package co.condorlabs.customcomponents.helper

import android.text.Editable
import android.text.TextWatcher

/**
 * @author Oscar Gallon on 2/25/19.
 */
open class TextWatcherAdapter: TextWatcher{
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

}
