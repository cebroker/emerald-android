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

package co.condorlabs.customcomponents

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import co.condorlabs.customcomponents.formfield.FormField
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        btnRun.setOnClickListener {
          for (i in 0 until (llMain?.childCount ?: 0)){
              ((llMain as? ViewGroup)?.getChildAt(i) as? FormField)?.let {
                  it.clearError()
                  //Log.i("C", "${it.isValid()}")
                  val result = it.isValid()
                  if (!result.isValid){
                      it.showError( result.error)
                  }
              }

          }
        }
    }
}
