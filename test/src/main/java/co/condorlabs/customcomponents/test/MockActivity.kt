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

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import co.condorlabs.customcomponents.customsignature.SignatureDialog
import se.warting.signaturepad.BuildConfig
import se.warting.signaturepad.SignaturePadAdapter
import se.warting.signaturepad.SignaturePadView

/**
 * @author Oscar Gallon on 2/21/19.
 */
private const val SIGNATURE_PAD_HEIGHT = 320
class MockActivity : AppCompatActivity(), SignatureDialog.OnDoneSignatureListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        openSignatureDialog()
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val mutableSvg = remember { mutableStateOf("") }
                    Column {
                        var signaturePadAdapter: SignaturePadAdapter? = null
                        val penColor = remember { mutableStateOf(Color.Black) }
                        Box(
                            modifier = Modifier
                                .height(SIGNATURE_PAD_HEIGHT.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            SignaturePadView(
                                penMinWidth = 1.dp,
                                penMaxWidth = 1.dp,
                                clearOnDoubleClick = true,
                                onReady = {
                                    signaturePadAdapter = it
                                },
                                penColor = penColor.value,
                                onSigned = {
                                    if (BuildConfig.DEBUG) {
                                        Log.d("ComposeActivity", "onSigned")
                                    }
                                },
                                onClear = {
                                    if (BuildConfig.DEBUG) {
                                        Log.d(
                                            "ComposeActivity",
                                            "onClear isEmpty:" + signaturePadAdapter?.isEmpty
                                        )
                                    }
                                },
                                onStartSigning = {
                                    if (BuildConfig.DEBUG) {
                                        Log.d("ComposeActivity", "onStartSigning")
                                    }
                                })
                            Text(modifier = Modifier.padding(top = 10.dp), text = "-------------------- sign here --------------------")
                        }
                        Row(modifier = Modifier.padding(top = 30.dp, start = 30.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(onClick = {
                                mutableSvg.value = signaturePadAdapter?.getSignatureSvg() ?: ""
                            }) {
                                Text("Save")
                            }

                            Button(onClick = {
                                mutableSvg.value = ""
                                signaturePadAdapter?.clear()
                            }) {
                                Text("Clear")
                            }

                            Button(onClick = {
                                penColor.value = Color.Red
                            }) {
                                Text("Red")
                            }

                            Button(onClick = {
                                penColor.value = Color.Black
                            }) {
                                Text("Black")
                            }
                        }

                        Text(text = "SVG: " + mutableSvg.value)
                    }
                }
            }
        }
    }

    companion object {
        var layout: Int = R.layout.activity_mock
    }

    @Composable
    private fun OpenComposeSignatureDialog() {
        var signaturePadAdapter: SignaturePadAdapter? = null

        SignaturePadView(onReady = {
            signaturePadAdapter = it
        })

        Button(onClick = {
            Log.d("", signaturePadAdapter?.getSignatureSvg() ?: "null")
        }) {
            Text("Save")
        }
    }


    private fun openSignatureDialog() {
        val signatureDialog = SignatureDialog()
        signatureDialog.setOnSignatureDoneListener(this)
        supportFragmentManager.beginTransaction()
            .add(signatureDialog, SignatureDialog::class.java.name)
            .commitAllowingStateLoss()
    }

    override fun onDoneSignature(bitmap: Bitmap) {
        Log.d("Signature TAG" , bitmap.toString())
    }
}
