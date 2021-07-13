package com.copperleaf.thistle.app.ui.main

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTag

class ForegroundColorFromString : ThistleTag<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return checkArgs(renderContext) {
            val color: String by string()

            ForegroundColorSpan(Color.parseColor(color))
        }
    }
}
