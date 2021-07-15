package com.copperleaf.thistle.android.tags

import android.text.style.URLSpan
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory

class Url(
    private val hardcodedUrl: String? = null
) : ThistleTagFactory<AndroidThistleRenderContext, Any> {
    override fun invoke(renderContext: AndroidThistleRenderContext): Any {
        return checkArgs(renderContext) {
            val url: String by string(hardcodedUrl)

            URLSpan(url)
        }
    }
}
