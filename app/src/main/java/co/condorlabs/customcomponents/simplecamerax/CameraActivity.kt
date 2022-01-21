package co.condorlabs.customcomponents.simplecamerax

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.constraintlayout.widget.Barrier
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.custombutton.CustomButton
import co.condorlabs.customcomponents.imagecropview.AppCompatCropImageView
import co.condorlabs.customcomponents.models.CameraConfig
import co.condorlabs.customcomponents.simplecamerax.fragment.SimpleCameraXFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CameraActivity : AppCompatActivity(), SimpleCameraXFragment.OnCameraXListener {

    private var simpleCameraXFragment: Fragment? = null
    private lateinit var cameraFragment: SimpleCameraXFragment
    private lateinit var cameraConfig: CameraConfig
    private var btnCancelPhoto: CustomButton? = null
    private var btnCropPhoto: CustomButton? = null
    private var cameraTitle: TextView? = null
    private var capturePhotoDescription: TextView? = null
    private var clActivityCameraParent: ConstraintLayout? = null
    private var clButtonsContainer: ConstraintLayout? = null
    private var fabCaptureButton: FloatingActionButton? = null
    private var ibCancelPhoto: TextView? = null
    private var photoCaptured: AppCompatCropImageView? = null
    private var vRectangle: Barrier? = null
    private var barrier: Barrier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_camera)
        simpleCameraXFragment = supportFragmentManager.findFragmentById(R.id.simpleCameraXFragment)
        cameraFragment = simpleCameraXFragment as SimpleCameraXFragment
        cameraConfig = intent.getParcelableExtra(CAMERA_CONFIG_OBJ_PARAM)
            ?: throw NoSuchElementException("CameraConfig object was not provided")
        setupScreenProperties()
        setupView()
    }

    private fun setupView() {
        btnCancelPhoto = findViewById(R.id.btnCancelPhoto)
        btnCropPhoto = findViewById(R.id.btnCropPhoto)
        cameraTitle = findViewById(R.id.cameraTitle)
        capturePhotoDescription = findViewById(R.id.capturePhotoDescription)
        clActivityCameraParent = findViewById(R.id.clActivityCameraParent)
        clButtonsContainer = findViewById(R.id.clButtonsContainer)
        fabCaptureButton = findViewById(R.id.fabCaptureButton)
        ibCancelPhoto = findViewById(R.id.ibCancelPhoto)
        photoCaptured = findViewById(R.id.photoCaptured)
        vRectangle = findViewById(R.id.vRectangle)
        barrier = findViewById(R.id.barrier)
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
                connect(
                    vRectangle?.id?:0,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START
                )
                connect(vRectangle?.id?:0, ConstraintSet.TOP, barrier?.id?:0, ConstraintSet.TOP)
                connect(
                    vRectangle?.id?:0,
                    ConstraintSet.END,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.END
                )
                connect(
                    vRectangle?.id?:0,
                    ConstraintSet.BOTTOM,
                    clButtonsContainer?.id?:0,
                    ConstraintSet.TOP
                )
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
        Toast.makeText(this, getString(R.string.error_taking_photo_message), Toast.LENGTH_LONG)
            .show()
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
