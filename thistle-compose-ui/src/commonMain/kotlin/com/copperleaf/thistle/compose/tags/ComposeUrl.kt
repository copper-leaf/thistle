package com.copperleaf.thistle.compose.tags

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.compose.util.ComposeSpanWrapper
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.string

public class ComposeUrl(
    private val color: Color? = null,
    private val hardcodedUrl: String? = null,
    private val handler: (String) -> Unit
) : ThistleTagFactory<ComposeThistleRenderContext, ComposeSpanWrapper> {
    override fun invoke(renderContext: ComposeThistleRenderContext): ComposeSpanWrapper {
        return checkArgs(renderContext) {
            val url: String by string(hardcodedUrl)

            if (color != null) {
                ComposeSpanWrapper(
                    spanStyle = SpanStyle(
                        color = color,
                        textDecoration = TextDecoration.Underline
                    ),
                    clickHandler = { handler(url) }
                )
            } else {
                ComposeSpanWrapper(
                    spanStyle = SpanStyle(
                        textDecoration = TextDecoration.Underline
                    ),
                    clickHandler = { handler(url) }
                )
            }
        }
    }
}
