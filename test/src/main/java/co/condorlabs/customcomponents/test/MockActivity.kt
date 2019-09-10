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

package co.condorlabs.customcomponents.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.condorlabs.customcomponents.customspinner.SpinnerData
import kotlinx.android.synthetic.main.activity_mock.*
import android.widget.ArrayAdapter



/**
 * @author Oscar Gallon on 2/21/19.
 */
class MockActivity : AppCompatActivity() {

    private val COUNTRIES = arrayOf("Belgium", "France", "Italy", "Germany", "Spain")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)

        val data = SpinnerData("1", "Antioquia")
        val data1 = SpinnerData("2", "Cundinamarca")
        val data2 = SpinnerData("3", "Atlantico")
        val spinnerDataList = arrayListOf(data, data1, data2)
        tlState.setData(spinnerDataList)

        val adapter = ArrayAdapter(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            COUNTRIES
        )
        countries_list.setAdapter(adapter)
        countries_list.setOnClickListener {
            val a = 0
        }
        countries_list.threshold = 0
    }

    companion object {
        var layout: Int = R.layout.activity_mock

    }
}
