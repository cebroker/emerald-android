package co.condorlabs.customcomponents.helper

import android.text.TextUtils
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

fun String?.isNumeric(): Boolean {
    this?.let {
        return TextUtils.isDigitsOnly(this)
    }

    return false
}

fun String.toDollarAmount(): String {

    return try {
        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        formatter.roundingMode = RoundingMode.UNNECESSARY
        formatter.minimumFractionDigits = ZERO
        formatter.format(this.toFloat())
    } catch (exception: Exception) {
        this
    }
}
