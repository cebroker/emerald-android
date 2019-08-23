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
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.appcompat.app.AppCompatActivity
import co.condorlabs.customcomponents.fileselectorview.FileSelectorClickListener
import co.condorlabs.customcomponents.fileselectorview.FileSelectorOption
import co.condorlabs.customcomponents.fileselectorview.FileSelectorValue
import kotlinx.android.synthetic.main.activity_mock.*
import java.io.*
import java.util.*

/**
 * @author Oscar Gallon on 2/21/19.
 */
class MockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        myFileSelector.setFileSelectorClickListener(object : FileSelectorClickListener {
            override fun onOptionSelected(fileSelectorOption: FileSelectorOption) {
                openFilePicker()
            }
        })
    }

    private fun openFilePicker() {
        startActivityForResult(
            Intent().apply {
                type = "*/*"
                action = Intent.ACTION_GET_CONTENT
            }, 0
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.data?.let { setImageInSelector(it) }
    }

    private fun setImageInSelector(uri: Uri) {
        myFileSelector?.let {
            it.setFileValue(
                FileSelectorValue.FileValue(
                    loadFile(uri)
                )
            )
        }
    }

    private fun loadFile(uri: Uri): File {
        val filename = getFilenameWithoutSpacesFrom(uri)
        if (filename != null) {
            return File(getPathFromInputStreamUri(uri, filename!!))
        } else {
            throw Exception("file is null :(")
        }
    }

    private fun getFilenameWithoutSpacesFrom(uri: Uri): String? {
        var filename: String? = null
        val cursor = contentResolver.query(
            uri, null, null, null, null
        )
        cursor?.let {
            if (it.moveToFirst()) {
                filename =
                    it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
            it.close()
        }
        return filename?.replace(" ", "")
    }

    private fun getPathFromInputStreamUri(uri: Uri, filename: String? = null): String {
        var inputStream: InputStream? = null
        var filePath: String? = null

        if (uri.authority != null) {
            try {
                inputStream = contentResolver.openInputStream(uri)
                val photoFile = createTemporalFileFrom(inputStream, filename)

                photoFile?.let {
                    filePath = it.path
                }
            } catch (e: FileNotFoundException) {
                throw FileNotFoundException()
            } catch (e: IOException) {
                throw IOException()
            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        filePath?.let { return it }
        throw Exception("NotSupportedUriException")
    }

    private fun createTemporalFileFrom(inputStream: InputStream?, filename: String?): File? {
        var targetFile: File? = null

        if (inputStream != null) {
            var read: Int
            val buffer = ByteArray(8 * 1024)

            targetFile = createTemporalFile(filename)
            val outputStream = FileOutputStream(targetFile)

            read = inputStream.read(buffer)
            while ((read) != -1) {
                outputStream.write(buffer, 0, read)
                read = inputStream.read(buffer)
            }
            outputStream.flush()

            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return targetFile
    }

    private fun createTemporalFile(name: String? = null): File {
        return if (name != null) {
            File(externalCacheDir, name)
        } else {
            File(externalCacheDir, Date().time.toString())
        }
    }

    companion object {
        var layout: Int = R.layout.activity_mock
    }
}
