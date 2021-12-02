package com.copperleaf.thistle.compose.tags

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.compose.util.ComposeSpanWrapper
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory

class ComposeLink(
    private val color: Color = Color.Unspecified,
    private val handler: (String) -> Unit
) : ThistleTagFactory<ComposeThistleRenderContext, ComposeSpanWrapper> {
    override fun invoke(renderContext: ComposeThistleRenderContext): ComposeSpanWrapper {
        return checkArgs(renderContext) {
            ComposeSpanWrapper(
                spanStyle = SpanStyle(
                    color = color,
                    textDecoration = TextDecoration.Underline
                ),
                clickHandler = { handler(renderContext.content) }
            )
        }
    }
}
