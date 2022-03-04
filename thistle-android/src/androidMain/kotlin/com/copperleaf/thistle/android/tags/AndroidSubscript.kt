package com.copperleaf.thistle.android.tags

import android.text.style.SubscriptSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.parser.ThistleTagFactory

public class AndroidSubscript : ThistleTagFactory<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return SubscriptSpan()
    }
}
