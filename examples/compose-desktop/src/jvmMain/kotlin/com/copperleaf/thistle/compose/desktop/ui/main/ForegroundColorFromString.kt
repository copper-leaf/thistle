package com.copperleaf.thistle.compose.desktop.ui.main

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import com.copperleaf.thistle.compose.ComposeSpanWrapper
import com.copperleaf.thistle.compose.parse
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.string

class ForegroundColorFromString : ThistleTagFactory<ComposeThistleRenderContext, ComposeSpanWrapper> {
    override fun invoke(renderContext: ComposeThistleRenderContext): ComposeSpanWrapper {
        return checkArgs(renderContext) {
            val color: String by string()

            ComposeSpanWrapper(
                SpanStyle(
                    color = Color.parse(color)
                )
            )
        }
    }
}
