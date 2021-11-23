package com.copperleaf.thistle.compose.tags

import androidx.compose.foundation.text.InlineTextContent
import com.copperleaf.thistle.compose.ComposeSpanWrapper
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory

class ComposeInlineContent(
    private val content: InlineTextContent
) : ThistleTagFactory<ComposeThistleRenderContext, ComposeSpanWrapper> {
    override fun invoke(renderContext: ComposeThistleRenderContext): ComposeSpanWrapper {
        return checkArgs(renderContext) {
            ComposeSpanWrapper(
                inlineContent = content
            )
        }
    }
}
