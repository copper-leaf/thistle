package com.copperleaf.thistle.android.tags

import android.text.style.SuperscriptSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.parser.ThistleTag

class Superscript : ThistleTag<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return SuperscriptSpan()
    }
}
