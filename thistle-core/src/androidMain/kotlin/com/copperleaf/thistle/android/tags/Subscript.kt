package com.copperleaf.thistle.android.tags

import android.text.style.SubscriptSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.parser.ThistleTag

class Subscript : ThistleTag<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return SubscriptSpan()
    }
}
