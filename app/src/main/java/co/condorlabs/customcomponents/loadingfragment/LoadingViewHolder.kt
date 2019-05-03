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

    fun bind(loadingItem: LoadingItem) {
        tvTitle?.text = loadingItem.title
        putStyleToText(tvTitle)
    }

    override fun updateItemStatus(status: Status) {
        ivIndicator?.apply {
            animatedChange(
                this, when (status) {
                    Status.Pending -> ContextCompat.getDrawable(context, R.drawable.ic_circle)!!
                    else -> ContextCompat.getDrawable(context, R.drawable.ic_checked)!!
                }
            )
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

    private fun animatedChange(v: ImageView, new_image: Drawable) {
        val anim_out = AnimationUtils.loadAnimation(v.context, android.R.anim.fade_out)
        val anim_in = AnimationUtils.loadAnimation(v.context, android.R.anim.fade_in)
        anim_out.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                v.setImageDrawable(new_image)
                anim_in.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationRepeat(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {}
                })
                v.startAnimation(anim_in)
            }
        })
        v.startAnimation(anim_out)
    }
}
