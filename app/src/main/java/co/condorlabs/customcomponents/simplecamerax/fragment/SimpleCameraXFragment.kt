package co.condorlabs.customcomponents.simplecamerax.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Rational
import android.util.Size
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraX
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureConfig
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.fragment.app.Fragment
import co.condorlabs.customcomponents.databinding.FragmentSimpleCameraXBinding
import co.condorlabs.customcomponents.models.CameraTextureViewMetrics
import java.io.File
import java.util.concurrent.Executors

class SimpleCameraXFragment : Fragment(), TextureView.SurfaceTextureListener {

    private lateinit var binding: FragmentSimpleCameraXBinding
    private var imageCapture: ImageCapture? = null
    private var onCameraXListener: OnCameraXListener? = null
    private val executor = Executors.newSingleThreadExecutor()

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
        binding = FragmentSimpleCameraXBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cameraTextureView.surfaceTextureListener = this
    }

    fun startCamera() {
        CameraX.bindToLifecycle(this, binding.buildPreviewUseCase(), buildImageCaptureUseCase())
    }

    @SuppressLint("RestrictedApi")
    private fun FragmentSimpleCameraXBinding.buildPreviewUseCase(): Preview {
        return cameraTextureViewMetrics().let { metrics ->
            val preview = Preview(PreviewConfig.Builder().apply {
                setTargetAspectRatioCustom(metrics.aspectRatio)
                setTargetRotation(metrics.rotation)
                setTargetResolution(metrics.resolution)
                build()
            }.build())
            preview.setOnPreviewOutputUpdateListener {
                val parent = cameraTextureView.parent as ViewGroup
                parent.removeView(cameraTextureView)
                parent.addView(cameraTextureView, 0)
                cameraTextureView.surfaceTexture = it.surfaceTexture
                updateTransform()
            }
            preview
        }
    }

    @SuppressLint("RestrictedApi")
    private fun buildImageCaptureUseCase(): ImageCapture? {
        return binding.cameraTextureViewMetrics().let { metrics ->
            val captureConfig = ImageCaptureConfig.Builder()
                .setTargetAspectRatioCustom(metrics.aspectRatio)
                .setTargetRotation(metrics.rotation)
                .setTargetResolution(metrics.resolution)
                .setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
                .build()
            imageCapture = ImageCapture(captureConfig)
            imageCapture
        }
    }

    private fun FragmentSimpleCameraXBinding.cameraTextureViewMetrics(): CameraTextureViewMetrics {
        return cameraTextureView.let { textureView ->
            val metrics = DisplayMetrics().also { textureView.display.getRealMetrics(it) }
            val aspectRatio = Rational(metrics.widthPixels, metrics.heightPixels)
            val rotation = textureView.display.rotation
            val resolution = Size(metrics.widthPixels, metrics.heightPixels)
            CameraTextureViewMetrics(aspectRatio, rotation, resolution)
        }
    }

    fun takePhoto(filePath: String? = null) {
        activity?.let { activityParent ->
            if (filePath.isNullOrEmpty()) {
                imageCapture?.takePicture(
                    executor,
                    object : ImageCapture.OnImageCapturedListener() {
                        override fun onCaptureSuccess(image: ImageProxy?, rotationDegrees: Int) {
                            activityParent.runOnUiThread {
                                val imageCaptured = image?.image ?: throw NullPointerException()
                                val buffer = imageCaptured.planes.first().buffer
                                val bytes = ByteArray(buffer.capacity())
                                buffer.get(bytes)
                                BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)?.let {
                                    onCameraXListener?.onImageCaptured(it)
                                }
                                image.close()
                                imageCaptured.close()
                            }
                        }

                        override fun onError(
                            imageCaptureError: ImageCapture.ImageCaptureError,
                            message: String,
                            cause: Throwable?
                        ) {
                            onCameraXListener?.onError(imageCaptureError, message, cause)
                        }
                    })
            } else {
                val fileTemp = File(filePath)
                if (!fileTemp.exists()) {
                    fileTemp.mkdirs()
                }
                val fileFinal = File(fileTemp, "img_${System.currentTimeMillis()}.jpg")
                imageCapture?.takePicture(
                    fileFinal,
                    executor,
                    object : ImageCapture.OnImageSavedListener {
                        override fun onImageSaved(file: File) {
                            activityParent.runOnUiThread {
                                BitmapFactory.decodeFile(file.absolutePath)?.let {
                                    onCameraXListener?.onImageSaved(it)
                                }
                            }
                        }

                        override fun onError(
                            imageCaptureError: ImageCapture.ImageCaptureError,
                            message: String,
                            cause: Throwable?
                        ) {
                            activityParent.runOnUiThread {
                                onCameraXListener?.onError(imageCaptureError, message, cause)
                            }
                        }
                    })
            }
        }
    }

    private fun FragmentSimpleCameraXBinding.updateTransform() {
        val matrix = Matrix()
        val centerX = cameraTextureView.width / 2F
        val centerY = cameraTextureView.height / 2F
        val rotationDegrees = when (cameraTextureView.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)
        cameraTextureView.setTransform(matrix)
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {}

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {}

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        onCameraXListener?.fragmentTextureViewLoaded()
    }

    interface OnCameraXListener {

        fun fragmentTextureViewLoaded()

        fun onImageCaptured(bitmap: Bitmap)

        fun onImageSaved(bitmap: Bitmap)

        fun onError(
            imageCaptureError: ImageCapture.ImageCaptureError,
            message: String,
            cause: Throwable?
        )
    }
}
