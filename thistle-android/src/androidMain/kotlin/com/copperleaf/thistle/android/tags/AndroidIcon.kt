package com.copperleaf.thistle.android.tags

import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory

public class AndroidIcon(
    private val hardcodedDrawable: Drawable? = null
) : ThistleTagFactory<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return checkArgs(renderContext) {
            val drawable: Drawable by parameter(hardcodedDrawable)

            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

            ImageSpan(drawable)
        }
    }
}
