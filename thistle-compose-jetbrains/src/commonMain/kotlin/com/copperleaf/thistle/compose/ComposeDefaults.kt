package com.copperleaf.thistle.compose

import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderContext
import com.copperleaf.thistle.compose.renderer.ComposeThistleRenderer
import com.copperleaf.thistle.compose.tags.Bold
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
            tag("b") { Bold() }
        }
    }
}
