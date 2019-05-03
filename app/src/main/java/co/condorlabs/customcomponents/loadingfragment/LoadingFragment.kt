package co.condorlabs.customcomponents.loadingfragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import co.condorlabs.customcomponents.R
import kotlinx.android.synthetic.main.fragment_loading.*
import java.util.*


class LoadingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle?.text = "Adding the following:"

        val wrappedContext = context ?: return
        rvItems?.setHasFixedSize(false)
        rvItems?.adapter = LoadingAdapter()
        rvItems?.layoutManager = LinearLayoutManager(wrappedContext)

        val uuid = UUID.randomUUID()

        (rvItems?.adapter as? LoadingAdapter)?.add(
            listOf(
                LoadingItem(title = "All provider demographic information"),
                LoadingItem(id = uuid, title = "2 medical licenses"),
                LoadingItem(title = "3 Education entries"),
                LoadingItem(title = "4 Employment history entries")
            )
        )

        rvItems?.postDelayed({

            rvItems?.apply {
                val position = (rvItems?.adapter as? LoadingAdapter)?.getItemPosition(uuid) ?: -1
                if (position < 0) {
                    return@apply
                }

                (findViewHolderForAdapterPosition(position) as? LoadingViewHolder)?.updateItemStatus(Status.Loaded)
            }
        }, 5000)

    }


    companion object {

        fun newInstance(): LoadingFragment = LoadingFragment()
    }
}
