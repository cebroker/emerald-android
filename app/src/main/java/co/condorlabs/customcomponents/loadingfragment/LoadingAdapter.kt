package co.condorlabs.customcomponents.loadingfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.condorlabs.customcomponents.R
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Oscar Gallon on 2019-05-03.
 */
class LoadingAdapter(private val items: ArrayList<LoadingItem> = ArrayList()) :
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

    fun add(loadingItems: List<LoadingItem>){
        items.addAll(loadingItems)
         notifyDataSetChanged()
    }

    fun getItemPosition(uuid: UUID): Int{
        return items.indexOfFirst {
            it.id == uuid
        }
    }
}
