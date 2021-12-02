package com.copperleaf.thistle.android.ui.main

import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.parser.ThistleTagFactory

class OnClickDisplayInnerContent : ThistleTagFactory<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(renderContext.uiContext, renderContext.content, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
