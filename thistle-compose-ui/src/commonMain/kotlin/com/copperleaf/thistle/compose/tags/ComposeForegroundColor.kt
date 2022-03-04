package com.copperleaf.thistle.compose.tags

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.compose.util.ComposeSpanWrapper
import com.copperleaf.thistle.compose.util.color
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory

public class ComposeForegroundColor(
    private val hardcodedColor: Color? = null
) : ThistleTagFactory<ComposeThistleRenderContext, ComposeSpanWrapper> {
    override fun invoke(renderContext: ComposeThistleRenderContext): ComposeSpanWrapper {
        return checkArgs(renderContext) {
            val color: Color by color(hardcodedColor)

            ComposeSpanWrapper(
                SpanStyle(
                    color = color
                )
            )
        }
    }
}
