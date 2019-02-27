/*
 * Copyright 2019 CondorLabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.condorlabs.customcomponents.customspinner

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.helper.EMPTY

/**
 * @author Oscar Gallon on 2/26/19.
 */
class StateSpinnerFormField(context: Context, private val mAttrs: AttributeSet) :
    BaseSpinnerFormField(context, mAttrs), ItemSelectedListenerAdapter {

    override var mIsRequired: Boolean = false

    private var mStateListener: StateListener? = null

    override fun setup() {
        super.setup()
        mSpinner?.id = R.id.spState
        mSpinner?.adapter = StateSpinnerAdapter(context, android.R.layout.simple_spinner_dropdown_item)
        mSpinner?.onItemSelectedListener = this
    }

    fun setStates(states: List<String>) {
        (mSpinner?.adapter as? StateSpinnerAdapter)?.addStates(states.sorted())
    }

    fun setOnStateSetListener(stateListener: StateListener) {
        mStateListener = stateListener
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        super.onItemSelected(parent, view, position, id)
        mStateListener?.onStateSetListener((mSpinner?.getItemAtPosition(position) as? String) ?: EMPTY)
    }
}
