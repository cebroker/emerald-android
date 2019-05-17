package co.condorlabs.customcomponents.skeletonview


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.condorlabs.customcomponents.ARGUMENT_SKELETON_TYPE
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.SkeletonFragmentNoArgumentsProvidedException
import com.facebook.shimmer.ShimmerFrameLayout

class SkeletonFragment : Fragment() {

    private val skeletonFactory = SkeletonFactory()

    private var layout: SkeletonLayout = R.layout.fragment_skeleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wrapperArguments = arguments ?: throw SkeletonFragmentNoArgumentsProvidedException()

        if (!wrapperArguments.containsKey(ARGUMENT_SKELETON_TYPE)) {
            throw SkeletonFragmentNoArgumentsProvidedException()
        }

        val skeletonFragmentType =
            wrapperArguments.getString(ARGUMENT_SKELETON_TYPE) ?: throw SkeletonFragmentNoArgumentsProvidedException()

        layout = skeletonFactory.getFragmentByType(skeletonFactory.getSkeletonTypeFromValue(skeletonFragmentType))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view as ShimmerFrameLayout).startShimmer()
    }

    companion object {

        fun newInstance(type: String): SkeletonFragment = SkeletonFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_SKELETON_TYPE, type)
            }
        }
    }
}
