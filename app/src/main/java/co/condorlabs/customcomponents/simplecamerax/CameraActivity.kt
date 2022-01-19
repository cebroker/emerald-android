package co.condorlabs.customcomponents.simplecamerax

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.constraintlayout.widget.ConstraintSet
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.models.CameraConfig
import co.condorlabs.customcomponents.simplecamerax.fragment.SimpleCameraXFragment
import kotlinx.android.synthetic.main.activity_camera.barrier
import kotlinx.android.synthetic.main.activity_camera.btnCancelPhoto
import kotlinx.android.synthetic.main.activity_camera.btnCropPhoto
import kotlinx.android.synthetic.main.activity_camera.cameraTitle
import kotlinx.android.synthetic.main.activity_camera.capturePhotoDescription
import kotlinx.android.synthetic.main.activity_camera.clActivityCameraParent
import kotlinx.android.synthetic.main.activity_camera.clButtonsContainer
import kotlinx.android.synthetic.main.activity_camera.fabCaptureButton
import kotlinx.android.synthetic.main.activity_camera.ibCancelPhoto
import kotlinx.android.synthetic.main.activity_camera.photoCaptured
import kotlinx.android.synthetic.main.activity_camera.simpleCameraXFragment
import kotlinx.android.synthetic.main.activity_camera.vRectangle

class CameraActivity : AppCompatActivity(), SimpleCameraXFragment.OnCameraXListener {

    private lateinit var cameraFragment: SimpleCameraXFragment
    private lateinit var cameraConfig: CameraConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_camera)

        cameraFragment = simpleCameraXFragment as SimpleCameraXFragment
        cameraConfig = intent.getParcelableExtra(CAMERA_CONFIG_OBJ_PARAM)
            ?: throw NoSuchElementException("CameraConfig object was not provided")
        setupScreenProperties()
    }

    private fun setupScreenProperties() {
        with(cameraConfig) {
            if (fitToScreen) {
                setFitToScreen()
            }
            titleText?.let {
                cameraTitle?.run {
                    visibility = View.VISIBLE
                    text = it
                }
            }
            descriptionText?.let {
                capturePhotoDescription?.run {
                    visibility = View.VISIBLE
                    text = it
                }
            }
            cancelButtonText?.let { btnCancelPhoto?.text = it }
            cropButtonText?.let { btnCropPhoto?.text = it }
            photoCaptured?.setKeepAspectRatio(keepAspectRatio)
        }
        ibCancelPhoto?.setOnClickListener { finish() }
    }

    private fun setFitToScreen() {
        clActivityCameraParent?.let {
            ConstraintSet().apply {
                clone(it)
                connect(vRectangle.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(vRectangle.id, ConstraintSet.TOP, barrier.id, ConstraintSet.TOP)
                connect(vRectangle.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(vRectangle.id, ConstraintSet.BOTTOM, clButtonsContainer.id, ConstraintSet.TOP)
                applyTo(it)
            }
        }
    }

    override fun fragmentTextureViewLoaded() {
        initCameraX()
        btnCancelPhoto?.setOnClickListener { resetLayout() }
    }

    private fun initCameraX() {
        cameraFragment.startCamera()
        fabCaptureButton?.setOnClickListener {
            (fabCaptureButton?.drawable as? Animatable)?.start()
            //cameraFragment.takePhoto(cameraConfig.savePhotoPath)
        }
    }

    override fun onImageCaptured(bitmap: Bitmap) {
        showImage(bitmap)
    }

    override fun onImageSaved(bitmap: Bitmap) {
        showImage(bitmap)
    }

    @SuppressLint("RestrictedApi")
    private fun showImage(bitmap: Bitmap) {
        if (bitmap.width > bitmap.height) {
            val rotatedBitmap = rotateImage(bitmap)
            photoCaptured?.setImageBitmap(rotatedBitmap)
        } else {
            photoCaptured?.setImageBitmap(bitmap)
        }

        btnCropPhoto?.run {
            visibility = View.VISIBLE
            setOnClickListener {
                photoCaptured?.cropImage()?.let { bitmapResult ->
                    CameraBitmapCache.setBitmap(bitmapResult)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
        btnCancelPhoto?.visibility = View.VISIBLE
        fabCaptureButton?.visibility = View.INVISIBLE
        if (cameraConfig.descriptionText != null) {
            capturePhotoDescription?.visibility = View.INVISIBLE
        }
    }

    private fun rotateImage(bitmap: Bitmap): Bitmap {
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        val matrix = Matrix()
        matrix.postRotate(90f)
        val scaledBitmap = Bitmap.createScaledBitmap(
            bitmap,
            bitmapWidth,
            bitmapHeight,
            true
        )
        return Bitmap.createBitmap(
            scaledBitmap,
            0,
            0,
            scaledBitmap.width,
            scaledBitmap.height,
            matrix,
            true
        )
    }

    override fun onError(
        imageCaptureError: ImageCapture.ImageCaptureError,
        message: String,
        cause: Throwable?
    ) {
        Toast.makeText(this, getString(R.string.error_taking_photo_message), Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        if (fabCaptureButton?.visibility == View.INVISIBLE) {
            resetLayout()
        } else {
            super.onBackPressed()
        }
    }

    private fun resetLayout() {
        photoCaptured?.setImageBitmap(null)
        btnCancelPhoto?.visibility = View.INVISIBLE
        btnCropPhoto?.visibility = View.INVISIBLE
        cameraConfig.descriptionText?.let {
            capturePhotoDescription?.run {
                visibility = View.VISIBLE
                text = it
            }
        }
        fabCaptureButton?.show()
        photoCaptured?.run {
            parentDimens = true
            setCropActivated(false)
            invalidate()
        }
    }

    companion object {
        const val CAMERA_CONFIG_OBJ_PARAM = "camera_config_obj_param"
    }
}
