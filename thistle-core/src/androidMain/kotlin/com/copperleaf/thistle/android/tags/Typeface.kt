package com.copperleaf.thistle.android.tags

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.style.TypefaceSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTag

class Typeface(
    private val hardcodedTypeface: Typeface? = null
) : ThistleTag<AndroidThistleRenderContext, Any> {

    @SuppressLint("NewApi")
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return checkArgs(renderContext) {
            val typeface: Typeface by enum(hardcodedTypeface) {
                mapOf(
                    "monospace" to Typeface.MONOSPACE,
                    "sans" to Typeface.SANS_SERIF,
                    "serif" to Typeface.SERIF,
                )
            }

            TypefaceSpan(typeface)
        }
    }
}
