package com.copperleaf.thistle.android.tags

import android.text.style.StrikethroughSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.parser.ThistleTag

class Strikethrough : ThistleTag<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return StrikethroughSpan()
    }
}
