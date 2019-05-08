package co.condorlabs.customcomponents.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.condorlabs.customcomponents.*
import co.condorlabs.customcomponents.loadingfragment.LoadingFragment
import co.condorlabs.customcomponents.loadingfragment.LoadingItem
import kotlinx.android.synthetic.main.activity_mock.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author Oscar Gallon on 2019-05-06.
 */
class LoadingFragmentActivityTest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock)

        val initOptions = intent?.extras?.getInt(ARGUMENT_LOADING_ACTIVITY_TEST_INIT_OPTION)
            ?: throw RuntimeException("Init option must be specified")

        when (initOptions) {
            INIT_WITH_TITLE -> initWithTitle()
            INIT_WITH_ELEMENTS -> initWithElements()
            INIT_FOR_SUCCESS -> initForSuccess()
            INIT_FOR_ERROR -> initForError()
            INIT_FOR_SUCCESS_WITH_ACTION ->  initForSuccessWithAction()
            INIT_FOR_ERROR_WITH_ACTION -> initForErrorWitAction()
            INIT_FOR_SUCCES_WITHOUT_TRIGGER_ACTION -> initForSuccessWithoutTriggerAction()
        }
    }

    private fun initWithTitle() {
        supportFragmentManager?.beginTransaction()
            ?.replace(
                R.id.ll, LoadingFragment.newInstance(
                    "Some title", arrayListOf(
                        LoadingItem(""),
                        LoadingItem(""),
                        LoadingItem(""),
                        LoadingItem("")
                    ), "", "", "", ""
                )
            )
            ?.commitAllowingStateLoss()
    }

    private fun initWithElements() {
        supportFragmentManager?.beginTransaction()
            ?.replace(
                R.id.ll, LoadingFragment.newInstance(
                    "Some title", arrayListOf(
                        LoadingItem("Element 1"),
                        LoadingItem("Element 2"),
                        LoadingItem("Element 3"),
                        LoadingItem("Element 4")
                    ), "", "", "", ""
                )
            )
            ?.commitAllowingStateLoss()
    }

    private fun initForSuccess() {
        val fragment = LoadingFragment.newInstance(
            "Some title", arrayListOf(
                LoadingItem("Element 1"),
                LoadingItem("Element 2"),
                LoadingItem("Element 3"),
                LoadingItem("Element 4")
            ), "Done!", "Failing", "You have submitted everything thank you", "Error"
        )

        supportFragmentManager?.beginTransaction()
            ?.replace(
                R.id.ll, fragment
            )
            ?.commitAllowingStateLoss()

        ll?.postDelayed({
            CoroutineScope(Dispatchers.Main).launch {
                fragment.showSuccessStatus("Continue")
            }
        }, 2000)
    }

    private fun initForError() {
        val fragment = LoadingFragment.newInstance(
            "Some title", arrayListOf(
                LoadingItem("Element 1"),
                LoadingItem("Element 2"),
                LoadingItem("Element 3"),
                LoadingItem("Element 4")
            ), "Done!", "Ups!", "You have submitted everything thank you",
            "Something went wrong"
        )

        supportFragmentManager?.beginTransaction()
            ?.replace(
                R.id.ll, fragment
            )
            ?.commitAllowingStateLoss()

        ll?.postDelayed({
            CoroutineScope(Dispatchers.Main).launch {
                fragment.showErrorStatus("Try again")
            }
        }, 2000)
    }

    private fun initForSuccessWithAction(){
        val fragment = LoadingFragment.newInstance(
            "Some title", arrayListOf(
                LoadingItem("Element 1"),
                LoadingItem("Element 2"),
                LoadingItem("Element 3"),
                LoadingItem("Element 4")
            ), "Done!", "Ups!", "You have submitted everything thank you",
            "Something went wrong"
        )
        supportFragmentManager?.beginTransaction()
            ?.replace(
                R.id.ll, fragment
            )
            ?.commitAllowingStateLoss()
    }

    private fun initForErrorWitAction() {
        val fragment = LoadingFragment.newInstance(
            "Some title", arrayListOf(
                LoadingItem("Element 1"),
                LoadingItem("Element 2"),
                LoadingItem("Element 3"),
                LoadingItem("Element 4")
            ), "Done!", "Ups!", "You have submitted everything thank you",
            "Something went wrong"
        )

        supportFragmentManager?.beginTransaction()
            ?.replace(
                R.id.ll, fragment
            )
            ?.commitAllowingStateLoss()
    }

    private fun initForSuccessWithoutTriggerAction(){
        val fragment = LoadingFragment.newInstance(
            "Some title", arrayListOf(
                LoadingItem("Element 1"),
                LoadingItem("Element 2"),
                LoadingItem("Element 3"),
                LoadingItem("Element 4")
            ), "Done!", "Failing", "You have submitted everything thank you", "Error"
        )

        supportFragmentManager?.beginTransaction()
            ?.replace(
                R.id.ll, fragment
            )
            ?.commitAllowingStateLoss()
    }
}
