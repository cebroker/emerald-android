package co.condorlabs.customcomponents.customsignature

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import co.condorlabs.customcomponents.R
import co.condorlabs.customcomponents.custombutton.CustomButton
import kotlinx.android.synthetic.main.signature_dialog.*

class SignatureDialog : DialogFragment() {

    private var onSignatureDoneListener: OnSignatureDoneListener? = null
    private var signatureView: SignatureView? = null
    private var btnDone: CustomButton? = null
    private var btnClear: CustomButton? = null
    private var ivClose: ImageView? = null

    fun setListener(onSignatureDoneListener: OnSignatureDoneListener) {
        this.onSignatureDoneListener = onSignatureDoneListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signature_dialog, container, false).apply {
            signatureView = findViewById(R.id.signatureView)
            btnDone = findViewById(R.id.btnDoneSigning)
            btnDone?.isEnabled = false
            btnClear = findViewById(R.id.btnClearSignature)
            ivClose = findViewById(R.id.ivClose)
            this@SignatureDialog.initComponents()
        }
    }

    private fun initComponents() {
        signatureView?.setOnActionMoveListener(object : OnActionMoveListener {
            override fun onActionMove() {
                btnDoneSigning?.isEnabled = true
            }
        })
        btnClear?.setOnClickListener {
            signatureView?.clearCanvas()
            btnDone?.isEnabled = false
        }
        btnDone?.setOnClickListener { performConfirmAction() }
        ivClose?.setOnClickListener { dialog?.cancel() }
    }

    private fun performConfirmAction() {
        signatureView?.let {
            it.stopDrawingLineAndText()
            val bitmap = getBitmapFromView(it)
            onSignatureDoneListener?.onSignatureDone(bitmap)
        }
        dialog?.cancel()
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null)
            bgDrawable.draw(canvas)
        else
            canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
    }
}
