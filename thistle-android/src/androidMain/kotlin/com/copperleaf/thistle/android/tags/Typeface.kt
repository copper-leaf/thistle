package com.copperleaf.thistle.android.tags

import android.graphics.Typeface
import android.os.Build
import android.text.style.TypefaceSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory

class Typeface(
    private val hardcodedTypeface: Typeface? = null
) : ThistleTagFactory<AndroidThistleRenderContext, Any> {

    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return checkArgs(renderContext) {
            val typeface: Typeface by enum(hardcodedTypeface) {
                mapOf(
                    "monospace" to Typeface.MONOSPACE,
                    "sans" to Typeface.SANS_SERIF,
                    "serif" to Typeface.SERIF,
                )
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                TypefaceSpan(typeface)
            } else {
                val typefaceString = when(typeface) {
                    Typeface.MONOSPACE -> "monospace"
                    Typeface.SANS_SERIF -> "sans"
                    Typeface.SERIF -> "serif"
                    else -> { error("") }
                }

                TypefaceSpan(typefaceString)
            }
        }
    }
}
