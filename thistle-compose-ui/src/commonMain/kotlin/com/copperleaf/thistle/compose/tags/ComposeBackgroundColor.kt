package com.copperleaf.thistle.compose.tags

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import com.copperleaf.thistle.compose.ComposeSpanWrapper
import com.copperleaf.thistle.compose.color
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory

class ComposeBackgroundColor(
    private val hardcodedColor: Color? = null
) : ThistleTagFactory<ComposeThistleRenderContext, ComposeSpanWrapper> {
    override fun invoke(renderContext: ComposeThistleRenderContext): ComposeSpanWrapper {
        return checkArgs(renderContext) {
            val color: Color by color(hardcodedColor)

            ComposeSpanWrapper(
                SpanStyle(
                    background = color
                )
            )
        }
    }
}
