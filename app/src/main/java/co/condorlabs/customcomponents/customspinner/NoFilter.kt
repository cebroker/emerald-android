package co.condorlabs.customcomponents.customspinner

import android.widget.Filter

/**
 * @author Oscar Gallon on 2019-05-14.
 */
class NoFilter(
    private val filterListeners: FilterListeners,
    private val data: List<SpinnerData>
) : Filter() {

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val result = FilterResults()
        result.values = data
        result.count = data.size
        return result
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        filterListeners.notifyDataSetChanged()
    }
}
