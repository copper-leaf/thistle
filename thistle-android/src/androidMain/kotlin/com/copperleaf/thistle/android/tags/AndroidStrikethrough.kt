package com.copperleaf.thistle.android.tags

import android.text.style.StrikethroughSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.parser.ThistleTagFactory

public class AndroidStrikethrough : ThistleTagFactory<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return StrikethroughSpan()
    }
}
