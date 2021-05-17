package com.copperleaf.thistle.android.tags

import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTag

class Icon(
    private val hardcodedDrawable: Drawable? = null
) : ThistleTag<Any> {
    override fun invoke(context: Map<String, Any>, args: Map<String, Any>): Any {
        return checkArgs(args) {
            val drawable: Drawable by parameter(hardcodedDrawable)

            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

            ImageSpan(drawable)
        }
    }
}
