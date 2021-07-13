package com.copperleaf.thistle.android.tags

import android.text.style.UnderlineSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.parser.ThistleTag

class Underline : ThistleTag<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return UnderlineSpan()
    }
}
