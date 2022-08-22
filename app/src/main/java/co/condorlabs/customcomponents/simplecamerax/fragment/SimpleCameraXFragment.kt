package co.condorlabs.customcomponents.simplecamerax.fragment

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import co.condorlabs.customcomponents.databinding.FragmentSimpleCameraXBinding
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SimpleCameraXFragment : Fragment() {

    private var _viewBinding: FragmentSimpleCameraXBinding? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private var onCameraXListener: OnCameraXListener? = null

    private val viewBinding get() = _viewBinding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onCameraXListener = activity as? OnCameraXListener
            ?: throw ClassCastException("$context must implement OnCameraXListener")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSimpleCameraXBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    fun takePhoto(filePath: String? = null) {

        val imageCapture = imageCapture ?: return

        if (filePath.isNullOrEmpty()) {
            imageCapture.takePicture(
                ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)
                        onCameraXListener?.onError(exception, exception.message ?: "No error message", exception.cause)
                    }

                    override fun onCaptureSuccess(image: ImageProxy) {
                        super.onCaptureSuccess(image)
                        val buffer = image.planes.first().buffer
                        val bytes = ByteArray(buffer.capacity())
                        buffer.get(bytes)
                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)?.let {
                            onCameraXListener?.onImageCaptured(it)
                        }
                        image.close()
                    }
                }
            )
        } else {
            val fileTemp = File(filePath)
            if (!fileTemp.exists()) {
                fileTemp.mkdirs()
            }
            val name = "img_${System.currentTimeMillis()}.jpg"

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, filePath)
                }
            }

            val outputOptions = ImageCapture.OutputFileOptions
                .Builder(
                    requireContext().contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                .build()

            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                        onCameraXListener?.onError(exc, exc.message ?: "No error message", exc.cause)
                    }

                    override fun
                            onImageSaved(output: ImageCapture.OutputFileResults) {
                        val msg = "Photo capture succeeded: ${output.savedUri}"

                        if (Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver,
                                output.savedUri
                            )
                            onCameraXListener?.onImageSaved(bitmap)
                        } else {
                            output.savedUri?.let {
                                val source = ImageDecoder.createSource(requireContext().contentResolver, it)
                                val bitmap = ImageDecoder.decodeBitmap(source)
                                onCameraXListener?.onImageSaved(bitmap)
                            }
                        }
                        Log.d(TAG, msg)
                    }
                }
            )
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


    interface OnCameraXListener {

        fun onImageCaptured(bitmap: Bitmap)

        fun onImageSaved(bitmap: Bitmap)

        fun onError(
            imageCaptureError: ImageCaptureException,
            message: String,
            cause: Throwable?
        )
    }
}

private const val TAG = "CameraXApp"
