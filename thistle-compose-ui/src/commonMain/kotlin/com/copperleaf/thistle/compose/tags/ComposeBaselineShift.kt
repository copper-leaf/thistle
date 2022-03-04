package com.copperleaf.thistle.compose.tags

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.compose.util.ComposeSpanWrapper
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.enum

public class ComposeBaselineShift(
    private val hardcodedBaselineShift: BaselineShift? = null,
    private val fontSize: TextUnit = 10.sp,
) : ThistleTagFactory<ComposeThistleRenderContext, ComposeSpanWrapper> {
    override fun invoke(renderContext: ComposeThistleRenderContext): ComposeSpanWrapper {
        return checkArgs(renderContext) {
            val type: BaselineShift by enum(hardcodedBaselineShift) {
                mapOf(
                    "none" to BaselineShift.None,
                    "subscript" to BaselineShift.Subscript,
                    "superscript" to BaselineShift.Superscript,
                )
            }

            ComposeSpanWrapper(
                SpanStyle(
                    baselineShift = type,
                    fontSize = fontSize,
                )
            )
        }
    }
}
