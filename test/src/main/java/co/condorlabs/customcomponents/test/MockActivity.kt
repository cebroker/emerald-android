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

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.condorlabs.customcomponents.models.CameraConfig
import co.condorlabs.customcomponents.simplecamerax.CameraActivity

/**
 * @author Oscar Gallon on 2/21/19.
 */
class MockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)

        startActivity(Intent(this, CameraActivity::class.java).putExtras(Bundle().apply {
            putParcelable(CameraActivity.CAMERA_CONFIG_OBJ_PARAM, CameraConfig("Title", "Description", "Cancel", "Crop", null))
        }))
    }

    companion object {
        var layout: Int = R.layout.activity_mock
    }
}
