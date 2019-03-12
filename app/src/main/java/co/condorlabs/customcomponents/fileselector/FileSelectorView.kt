package co.condorlabs.customcomponents.fileselector

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import co.condorlabs.customcomponents.R

class FileSelectorView : ConstraintLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {
        LayoutInflater.from(context).inflate(R.layout.file_selector_view, this)
    }
}
