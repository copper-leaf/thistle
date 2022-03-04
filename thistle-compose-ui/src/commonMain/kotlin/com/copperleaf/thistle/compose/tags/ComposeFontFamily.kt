package com.copperleaf.thistle.compose.tags

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.compose.util.ComposeSpanWrapper
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.enum

public class ComposeFontFamily(
    private val hardcodedFontFamily: FontFamily? = null,
) : ThistleTagFactory<ComposeThistleRenderContext, ComposeSpanWrapper> {
    override fun invoke(renderContext: ComposeThistleRenderContext): ComposeSpanWrapper {
        return checkArgs(renderContext) {
            val typeface: FontFamily by enum(hardcodedFontFamily) {
                mapOf(
                    "monospace" to FontFamily.Monospace,
                    "sans" to FontFamily.SansSerif,
                    "serif" to FontFamily.Serif,
                    "cursive" to FontFamily.Cursive,
                    "default" to FontFamily.Default,
                )
            }

            ComposeSpanWrapper(
                SpanStyle(
                    fontFamily = typeface,
                )
            )
        }
    }
}
