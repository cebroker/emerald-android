package co.condorlabs.customcomponents.loadingfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.custombutton.CustomButton
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.delay

class LoadingFragment : Fragment(), LoadingItemsScreen {

    private var rvItems: RecyclerView? = null
    private var btnAction: CustomButton? = null
    private var ivICon: ImageView? = null
    private var lavSpinner: LottieAnimationView? = null
    private var llStatus: LinearLayoutCompat? = null
    private var tvStatusMessage: TextView? = null
    private var tvStatusTitle:TextView? = null
    private var tvTitle:TextView? = null
    private lateinit var successTitle: String
    private lateinit var errorTitle: String
    private lateinit var successMessage: String
    private lateinit var errorMessage: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView(view)

        val wrappedArguments = arguments ?: throw ArgumentsNotFoundException()

        val title = wrappedArguments.getString(ARGUMENT_TITLE) ?: throw ArgumentNotFoundException(
            ARGUMENT_TITLE
        )
        setTitle(title)

        val itemList =
            wrappedArguments.getParcelableArrayList<LoadingItem>(ARGUMENT_LOADING_ITEM_LIST)
                ?: throw ArgumentNotFoundException(ARGUMENT_LOADING_ITEM_LIST)

        if (itemList.size > LOADING_FRAGMENT_MAX_ELEMENTS) {
            throw LoadingFragmentListGreaterThatLimitException()
        }

        setList(itemList)

        successTitle =
            wrappedArguments.getString(ARGUMENT_SUCCESS_TITLE) ?: throw ArgumentNotFoundException(
                ARGUMENT_SUCCESS_TITLE
            )

        errorTitle =
            wrappedArguments.getString(ARGUMENT_ERROR_TITLE) ?: throw ArgumentNotFoundException(
                ARGUMENT_ERROR_TITLE
            )

        successMessage =
            wrappedArguments.getString(ARGUMENT_SUCCESS_MESSAGE) ?: throw ArgumentNotFoundException(
                ARGUMENT_SUCCESS_MESSAGE
            )

        errorMessage =
            wrappedArguments.getString(ARGUMENT_ERROR_MESSAGE) ?: throw ArgumentNotFoundException(
                ARGUMENT_ERROR_MESSAGE
            )
    }

    private fun setupView(view:View) {
        rvItems = view.findViewById(R.id.rvItems)
        btnAction = view.findViewById(R.id.btnAction)
        ivICon = view.findViewById(R.id.ivICon)
        lavSpinner = view.findViewById(R.id.lavSpinner)
        llStatus = view.findViewById(R.id.llStatus)
        tvStatusMessage = view.findViewById(R.id.tvStatusMessage)
        tvStatusTitle = view.findViewById(R.id.tvStatusTitle)
        tvTitle = view.findViewById(R.id.tvTitle)
    }

    override suspend fun updateItemsTilPosition(
        position: Int,
        status: Status,
        timeBetweenObjectAnimation: Long
    ) {
        val recyclerView = rvItems ?: throw RecyclerViewNotFoundException()
        val adapter =
            (rvItems?.adapter as? LoadingAdapter) ?: throw LoadingAdapterNotFoundException()
        val adapterSize = adapter.itemCount

        if (position > adapterSize) {
            throw PositionGreaterThatItemsSizeException()
        }

        for (i in LOADING_ADAPTER_FIRST_POSITION until position) {
            val viewHolder =
                (recyclerView.findViewHolderForAdapterPosition(i) as? LoadingViewHolder)
                    ?: throw ViewHolderNotFoundForPositionException(i)

            viewHolder.updateItemStatus(status)
            delay(timeBetweenObjectAnimation)
        }
    }

    override suspend fun showSuccessStatus(btnActionText: String, btnActionCallback: () -> Unit) {
        val wrappedContext = context ?: return
        makeStatusItemsVisible()
        btnAction?.setType(BUTTON_PRIMARY_TYPE)
        setActionButtonText(btnActionText)
        ivICon?.setImageDrawable(ContextCompat.getDrawable(wrappedContext, R.drawable.ic_success))
        setCompletionTitle(successTitle)
        setCompletionMessage(successMessage)
        btnAction?.setOnClickListener {
            btnActionCallback()
        }
    }

    override suspend fun showErrorStatus(btnActionText: String, btnActionCallback: () -> Unit) {
        val wrappedContext = context ?: return
        makeStatusItemsVisible()
        btnAction?.setType(BUTTON_DANGER_TYPE)
        setActionButtonText(btnActionText)
        ivICon?.setImageDrawable(ContextCompat.getDrawable(wrappedContext, R.drawable.ic_error))
        setCompletionTitle(errorTitle)
        setCompletionMessage(errorMessage)
        btnAction?.setOnClickListener {
            btnActionCallback()
        }
    }

    private fun makeStatusItemsVisible() {
        lavSpinner?.visibility = View.GONE
        llStatus?.visibility = View.VISIBLE
        btnAction?.visibility = View.VISIBLE
    }

    private fun setActionButtonText(text: String) {
        btnAction?.text = text
    }

    private fun setCompletionMessage(message: String) {
        tvStatusMessage?.text = message
    }

    private fun setCompletionTitle(title: String) {
        tvStatusTitle?.text = title
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
