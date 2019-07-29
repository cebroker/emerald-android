package co.condorlabs.customcomponents.test.savestate.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.condorlabs.customcomponents.test.R

class InitialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_initial, container, false)

    companion object {
        fun newInstance() =
            InitialFragment()
    }
}
