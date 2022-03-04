package com.copperleaf.thistle.android.tags

import android.text.style.UnderlineSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.parser.ThistleTagFactory

public class AndroidUnderline : ThistleTagFactory<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return UnderlineSpan()
    }
}
