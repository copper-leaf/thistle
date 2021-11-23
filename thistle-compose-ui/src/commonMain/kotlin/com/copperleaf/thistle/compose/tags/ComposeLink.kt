package com.copperleaf.thistle.compose.tags

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.copperleaf.thistle.compose.ComposeSpanWrapper
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory

class ComposeLink(
    val color: Color? = null,
    private val handler: () -> Unit
) : ThistleTagFactory<ComposeThistleRenderContext, ComposeSpanWrapper> {
    override fun invoke(renderContext: ComposeThistleRenderContext): ComposeSpanWrapper {
        return checkArgs(renderContext) {
            if (color != null) {
                ComposeSpanWrapper(
                    spanStyle = SpanStyle(
                        color = color,
                        textDecoration = TextDecoration.Underline
                    ),
                    clickHandler = handler
                )
            } else {
                ComposeSpanWrapper(
                    spanStyle = SpanStyle(
                        textDecoration = TextDecoration.Underline
                    ),
                    clickHandler = handler
                )
            }
        }
    }
}
