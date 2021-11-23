package com.copperleaf.thistle.android.tags

import android.text.style.ForegroundColorSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.int

class AndroidForegroundColor(
    private val hardcodedColor: Int? = null
) : ThistleTagFactory<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return checkArgs(renderContext) {
            val color: Int by int(hardcodedColor)

            ForegroundColorSpan(color)
        }
    }
}
