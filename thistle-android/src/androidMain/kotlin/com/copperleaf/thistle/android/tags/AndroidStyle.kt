package com.copperleaf.thistle.android.tags

import android.graphics.Typeface
import android.text.style.StyleSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.enum

public class AndroidStyle(
    private val hardcodedStyle: Int? = null
) : ThistleTagFactory<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return checkArgs(renderContext) {
            val style: Int by enum(hardcodedStyle) {
                mapOf(
                    "bold" to Typeface.BOLD,
                    "italic" to Typeface.ITALIC
                )
            }

            StyleSpan(style)
        }
    }
}
