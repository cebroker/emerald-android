package co.condorlabs.customcomponents.loadingfragment

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import co.condorlabs.customcomponents.R

/**
 * @author Oscar Gallon on 2019-05-03.
 */
class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), UpdatableLoadingItem {

    private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
    private val ivIndicator = itemView.findViewById<ImageView>(R.id.ivIndicator)
    private val style = StyleSpan(Typeface.BOLD)
    private var currentStatus: Status = Status.Pending

    fun bind(loadingItem: LoadingItem) {
        tvTitle?.text = loadingItem.title
        putStyleToText(tvTitle)
        renderStatus()
    }

    fun getStatus() = currentStatus

    override fun updateItemStatus(status: Status) {
        currentStatus = status
        renderStatus()
    }

    private fun renderStatus() {
        ivIndicator?.apply {

            when (currentStatus) {
                Status.Pending -> ContextCompat.getDrawable(context, R.drawable.ic_circle)!!
                Status.Loaded -> animatedChange(
                    this,
                    ContextCompat.getDrawable(context, R.drawable.ic_checked)!!
                )
                Status.Error -> animatedChange(
                    this,
                    ContextCompat.getDrawable(context, R.drawable.ic_error_mark)!!
                )
            }
        }
    }

    private fun putStyleToText(textView: TextView) {
        val text = textView.text
        val builder = SpannableStringBuilder(text)

        val firstSpacePosition = text.indexOf(" ")
        builder.setSpan(
            style,
            0,
            if (firstSpacePosition != -1) firstSpacePosition else text.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )

        textView.text = builder
    }

    private fun animatedChange(imageView: ImageView, newImage: Drawable) {
        val context = imageView.context
        val exitAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        val enterAnimation = AnimationUtils.loadAnimation(context, R.anim.zoom_to_full)
        exitAnimation.setAnimationListener(object : AnimationAdapter() {
            override fun onAnimationEnd(animation: Animation?) {
                super.onAnimationEnd(animation)
                imageView.setImageDrawable(newImage)
                enterAnimation.setAnimationListener(object : AnimationAdapter() {})
                imageView.startAnimation(enterAnimation)
            }
        })

        imageView.startAnimation(exitAnimation)
    }
}
