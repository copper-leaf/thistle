package com.copperleaf.thistle.android.tags

import android.text.style.ClickableSpan
import android.view.View
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.parser.ThistleTagFactory

class Link(
    val handler: (widget: View) -> Unit
) : ThistleTagFactory<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                handler(widget)
            }
        }
    }
}
