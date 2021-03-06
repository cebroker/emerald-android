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
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.STATE_SPINNER_HINT_POSITION

/**
 * @author Oscar Gallon on 2/26/19.
 */
class SpinnerFormFieldAdapter(
    context: Context,
    resourceId: Int,
    private var hint: String = context.getString(R.string.spinner_default_hint),
    private val spinnerItems: ArrayList<SpinnerData> = ArrayList()
) : ArrayAdapter<SpinnerData>(context, resourceId, spinnerItems), FilterListeners {

    fun replaceStates(data: List<SpinnerData>) {
        spinnerItems.clear()
        spinnerItems.add(SpinnerData(hint, hint))
        spinnerItems.addAll(data)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getDropDownView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val optionTextView = (view as? TextView)?.let { it } ?: return view

        optionTextView.setTextColor(
            if (isHintPosition(position)) {
                Color.GRAY
            } else {
                Color.BLACK
            }
        )

        return optionTextView
    }

    override fun getFilter(): Filter {
        return NoFilter(this, spinnerItems)
    }

    fun getData(): List<SpinnerData> = spinnerItems

    fun isHintPosition(position: Int): Boolean = position == STATE_SPINNER_HINT_POSITION
}
