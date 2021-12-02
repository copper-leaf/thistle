package com.copperleaf.thistle.compose.tags

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.compose.util.ComposeSpanWrapper
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.enum

class ComposeStyle(
    private val hardcodedWeight: FontWeight? = null,
    private val hardcodedStyle: FontStyle? = null,
) : ThistleTagFactory<ComposeThistleRenderContext, ComposeSpanWrapper> {
    override fun invoke(renderContext: ComposeThistleRenderContext): ComposeSpanWrapper {
        return checkArgs(renderContext) {
//            val fontWeight: FontWeight by enum(hardcodedWeight) {
//                mapOf(
//                    "thin" to FontWeight.Thin,
//                    "extraLight" to FontWeight.ExtraLight,
//                    "light" to FontWeight.Light,
//                    "normal" to FontWeight.Normal,
//                    "medium" to FontWeight.Medium,
//                    "semiBold" to FontWeight.SemiBold,
//                    "bold" to FontWeight.Bold,
//                    "extraBold" to FontWeight.ExtraBold,
//                    "black" to FontWeight.Black,
//                )
//            }
//
//            val style: FontStyle by enum(hardcodedStyle) {
//                mapOf(
//                    "normal" to FontStyle.Normal,
//                    "italic" to FontStyle.Italic,
//                )
//            }

            val style: Pair<FontWeight?, FontStyle?> by enum(hardcodedWeight to hardcodedStyle) {
                mapOf(
                    "bold" to (FontWeight.Bold to null),
                    "italic" to (null to FontStyle.Italic),
                )
            }

            ComposeSpanWrapper(
                SpanStyle(
                    fontWeight = style.first,
                    fontStyle = style.second,
                )
            )
        }
    }
}
