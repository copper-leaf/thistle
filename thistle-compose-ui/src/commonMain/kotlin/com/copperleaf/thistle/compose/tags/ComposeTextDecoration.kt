package com.copperleaf.thistle.compose.tags

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.compose.util.ComposeSpanWrapper
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.enum

class ComposeTextDecoration(
    private val hardcodedTextDecoration: TextDecoration? = null,
) : ThistleTagFactory<ComposeThistleRenderContext, ComposeSpanWrapper> {
    override fun invoke(renderContext: ComposeThistleRenderContext): ComposeSpanWrapper {
        return checkArgs(renderContext) {
            val type: TextDecoration by enum(hardcodedTextDecoration) {
                mapOf(
                    "strikethrough" to TextDecoration.LineThrough,
                    "underline" to TextDecoration.LineThrough,
                )
            }

            ComposeSpanWrapper(
                SpanStyle(
                    textDecoration = type,
                )
            )
        }
    }
}
