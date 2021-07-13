package com.copperleaf.thistle.android.tags

import android.text.style.ForegroundColorSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTag

class ForegroundColor(
    private val hardcodedColor: Int? = null
) : ThistleTag<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return checkArgs(renderContext) {
            val color: Int by int(hardcodedColor)

            ForegroundColorSpan(color)
        }
    }
}
