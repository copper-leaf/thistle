package com.copperleaf.thistle.compose

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderer
import com.copperleaf.thistle.compose.tags.ComposeBackgroundColor
import com.copperleaf.thistle.compose.tags.ComposeBaselineShift
import com.copperleaf.thistle.compose.tags.ComposeFontFamily
import com.copperleaf.thistle.compose.tags.ComposeForegroundColor
import com.copperleaf.thistle.compose.tags.ComposeStyle
import com.copperleaf.thistle.compose.tags.ComposeTextDecoration
import com.copperleaf.thistle.compose.tags.ComposeUrl
import com.copperleaf.thistle.compose.util.ComposeRichText
import com.copperleaf.thistle.compose.util.ComposeSpanWrapper
import com.copperleaf.thistle.core.ThistleRendererFactory
import com.copperleaf.thistle.core.parser.ThistleSyntaxBuilder

@ExperimentalStdlibApi
object ComposeDefaults : ThistleSyntaxBuilder.Defaults<
    ComposeThistleRenderContext,
    ComposeSpanWrapper,
    ComposeRichText> {

    override fun rendererFactory(): ThistleRendererFactory<
        ComposeThistleRenderContext,
        ComposeSpanWrapper,
        ComposeRichText> {
        return ThistleRendererFactory { ComposeThistleRenderer(it) }
    }

    /**
     * Adds the default set of Compose tags to the [ThistleSyntaxBuilder].
     */
    override fun applyToBuilder(
        builder: ThistleSyntaxBuilder<ComposeThistleRenderContext, ComposeSpanWrapper, ComposeRichText>
    ) {
        with(builder) {
            tag("foreground") { ComposeForegroundColor() }
            tag("background") { ComposeBackgroundColor() }

            tag("style") { ComposeStyle() }

            tag("typeface") { ComposeFontFamily() }
            tag("monospace") { ComposeFontFamily(FontFamily.Monospace) }
            tag("sans") { ComposeFontFamily(FontFamily.SansSerif) }
            tag("serif") { ComposeFontFamily(FontFamily.Serif) }

            tag("strikethrough") { ComposeTextDecoration(TextDecoration.LineThrough) }

            tag("subscript") { ComposeBaselineShift(BaselineShift.Subscript) }
            tag("superscript") { ComposeBaselineShift(BaselineShift.Superscript) }

            tag("url") { ComposeUrl() { } }
//            tag("icon") { ComposeIcon() }

            tag("b") { ComposeStyle(hardcodedWeight = FontWeight.Bold, hardcodedStyle = FontStyle.Normal) }
            tag("i") { ComposeStyle(hardcodedWeight = FontWeight.Normal, hardcodedStyle = FontStyle.Italic) }
            tag("u") { ComposeTextDecoration(TextDecoration.Underline) }
        }
    }
}
