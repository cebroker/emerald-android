package co.condorlabs.customcomponents.loadingfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.condorlabs.customcomponents.R

/**
 * @author Oscar Gallon on 2019-05-03.
 */
class LoadingAdapter(private val items: List<LoadingItem>) :
    RecyclerView.Adapter<LoadingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingViewHolder {
        return LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false))
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: LoadingViewHolder, position: Int) {
        holder.bind(items[position])
    }
}
