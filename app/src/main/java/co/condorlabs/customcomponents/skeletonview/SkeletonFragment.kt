package co.condorlabs.customcomponents.skeletonview


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.condorlabs.customcomponents.R
import com.facebook.shimmer.ShimmerFrameLayout


class SkeletonFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_skeleton, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view as? ShimmerFrameLayout)?.startShimmer()
    }
}
