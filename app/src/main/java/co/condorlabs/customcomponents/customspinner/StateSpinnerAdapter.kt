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
import android.widget.ArrayAdapter

/**
 * @author Oscar Gallon on 2/26/19.
 */
class StateSpinnerAdapter(
    context: Context, resourceId: Int,
    private val mStates: ArrayList<String> = ArrayList()
) : ArrayAdapter<String>(context, resourceId, mStates) {


    fun addStates(states: List<String>) {
        mStates.addAll(states)
        notifyDataSetChanged()
    }

    fun clearStates() {
        mStates.clear()
        notifyDataSetChanged()
    }
}
