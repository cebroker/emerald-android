package co.condorlabs.customcomponents.loadingfragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import co.condorlabs.customcomponents.*
import kotlinx.android.synthetic.main.fragment_loading.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoadingFragment : Fragment(), LoadingItemsScreen {

    private lateinit var successTitle: String
    private lateinit var errorTitle: String
    private lateinit var successMessage: String
    private lateinit var errorMessage: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wrappedArguments = arguments ?: throw ArgumentsNotFoundException()

        val title = wrappedArguments.getString(ARGUMENT_TITLE) ?: throw ArgumentNotFoundException(ARGUMENT_TITLE)
        setTitle(title)

        val itemList = wrappedArguments.getParcelableArrayList<LoadingItem>(ARGUMENT_LOADING_ITEM_LIST)
            ?: throw ArgumentNotFoundException(ARGUMENT_LOADING_ITEM_LIST)

        if (itemList.size > LOADING_FRAGMENT_MAX_ELEMENTS) {
            throw LoadingFragmentListGreaterThatLimitException()
        }

        setList(itemList)

        successTitle = wrappedArguments.getString(ARGUMENT_SUCCESS_TITLE) ?: throw ArgumentNotFoundException(
            ARGUMENT_SUCCESS_TITLE
        )

        errorTitle =
            wrappedArguments.getString(ARGUMENT_ERROR_TITLE) ?: throw ArgumentNotFoundException(ARGUMENT_ERROR_TITLE)

        successMessage = wrappedArguments.getString(ARGUMENT_SUCCESS_MESSAGE) ?: throw ArgumentNotFoundException(
            ARGUMENT_SUCCESS_MESSAGE
        )

        errorMessage =
            wrappedArguments.getString(ARGUMENT_ERROR_MESSAGE) ?: throw ArgumentNotFoundException(
                ARGUMENT_ERROR_MESSAGE
            )
    }

    override fun updateItemsTilPosition(
        position: Int,
        status: Status,
        timeBetweenObjectAnimation: Long
    ) {
        val recyclerView = rvItems ?: throw RecyclerViewNotFoundException()
        val adapter = (rvItems?.adapter as? LoadingAdapter) ?: throw LoadingAdapterNotFoundException()
        val adapterSize = adapter.itemCount

        if (position > adapterSize) {
            throw PositionGreaterThatItemsSizeException()
        }

        CoroutineScope(Dispatchers.Main).launch {
            for (i in LOADING_ADAPTER_FIRST_POSITION until position) {
                val viewHolder =
                    (recyclerView.findViewHolderForAdapterPosition(i) as? LoadingViewHolder)
                        ?: throw ViewHolderNotFoundForPositionException(i)

                viewHolder.updateItemStatus(status)
                delay(timeBetweenObjectAnimation)
            }
        }
    }

    override fun showSuccessStatus(btnActionText: String, btnActionCallback: () -> Unit) {
        val wrappedContext = context ?: return
        lavSpinner?.visibility = View.GONE
        llStatus?.visibility = View.VISIBLE
        btnAction?.visibility = View.VISIBLE
        btnAction?.setType(BUTTON_PRIMARY_TYPE)
        setActionButtonText(btnActionText)
        ivICon?.setImageDrawable(ContextCompat.getDrawable(wrappedContext, R.drawable.ic_success))
        setCompletionTitle(successTitle)
        setCompletionMessage(successMessage)
        btnAction?.setOnClickListener {
            btnActionCallback()
        }
    }

    override fun showErrorStatus(btnActionText: String, btnActionCallback: () -> Unit) {
        val wrappedContext = context ?: return
        lavSpinner?.visibility = View.GONE
        llStatus?.visibility = View.VISIBLE
        btnAction?.visibility = View.VISIBLE
        btnAction?.setType(BUTTON_DANGER_TYPE)
        setActionButtonText(btnActionText)
        ivICon?.setImageDrawable(ContextCompat.getDrawable(wrappedContext, R.drawable.ic_error))
        setCompletionTitle(errorTitle)
        setCompletionMessage(errorMessage)
        btnAction?.setOnClickListener {
            btnActionCallback()
        }
    }

    private fun setActionButtonText(text: String) {
        btnAction?.text = text
    }

    private fun setCompletionMessage(message: String) {
        tvStatusMessage.text = message
    }

    private fun setCompletionTitle(title: String) {
        tvStatusTitle.text = title
    }

    private fun setTitle(title: String) {
        tvTitle?.text = title
    }

    private fun setList(items: List<LoadingItem>) {
        val wrappedContext = context ?: return
        rvItems?.setHasFixedSize(true)
        rvItems?.adapter = LoadingAdapter(items)
        rvItems?.layoutManager = LinearLayoutManager(wrappedContext)
    }

    companion object {
        fun newInstance(
            title: String,
            items: ArrayList<LoadingItem>,
            successTitle: String,
            errorTitle: String,
            successMessage: String,
            errorMessage: String
        ): LoadingFragment = LoadingFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_TITLE, title)
                putParcelableArrayList(ARGUMENT_LOADING_ITEM_LIST, items)
                putString(ARGUMENT_SUCCESS_TITLE, successTitle)
                putString(ARGUMENT_ERROR_TITLE, errorTitle)
                putString(ARGUMENT_SUCCESS_MESSAGE, successMessage)
                putString(ARGUMENT_ERROR_MESSAGE, errorMessage)
            }
        }
    }
}
