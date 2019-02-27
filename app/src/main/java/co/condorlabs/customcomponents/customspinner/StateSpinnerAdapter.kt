package co.condorlabs.customcomponents.customspinner

import android.content.Context
import android.widget.ArrayAdapter

/**
 * @author Oscar Gallon on 2/26/19.
 */
class StateSpinnerAdapter(
    context: Context, resourceId: Int,
    private val mStates: ArrayList<String> = ArrayList()
) : ArrayAdapter<String>(context, resourceId, mStates) {


    fun addStates(states: List<String>) {
        mStates.addAll(states)
        notifyDataSetChanged()
    }

    fun clearStates() {
        mStates.clear()
        notifyDataSetChanged()
    }
}
