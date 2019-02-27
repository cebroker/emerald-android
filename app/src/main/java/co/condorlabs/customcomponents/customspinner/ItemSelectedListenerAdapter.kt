package co.condorlabs.customcomponents.customspinner

import android.view.View
import android.widget.AdapterView

/**
 * @author Oscar Gallon on 2/26/19.
 */
interface ItemSelectedListenerAdapter: AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }
}
