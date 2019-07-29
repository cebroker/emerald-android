package co.condorlabs.customcomponents.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.condorlabs.customcomponents.test.savestate.fragments.InitialFragment
import kotlinx.android.synthetic.main.activity_base_edit_save_state.*

/**
 * Created by Oscar Tigreros on 25,July,2019
 */
class BaseEditSaveStateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameContent, InitialFragment.newInstance(), "first")
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commitAllowingStateLoss()

        btnGo.setOnClickListener {
            val fragmentTransaction2 = supportFragmentManager.beginTransaction()
            fragmentTransaction2.replace(R.id.frameContent, SecondFragment.newInstance(), "second")
            fragmentTransaction2.addToBackStack(null)
            fragmentTransaction2.commitAllowingStateLoss()
        }
    }

    companion object {
        var layout: Int = R.layout.activity_base_edit_save_state
    }
}
