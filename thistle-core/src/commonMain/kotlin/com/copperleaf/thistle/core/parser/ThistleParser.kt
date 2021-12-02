package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.kudzu.parser.mapped.FlatMappedParser
import com.copperleaf.kudzu.parser.tag.TagParser
import com.copperleaf.thistle.core.ThistleRendererFactory
import com.copperleaf.thistle.core.ThistleTagMap
import com.copperleaf.thistle.core.checkParsedCorrectly
import com.copperleaf.thistle.core.node.ThistleRootNode
import com.copperleaf.thistle.core.renderer.ThistleRenderContext
import com.copperleaf.thistle.core.renderer.ThistleRenderer

@ExperimentalStdlibApi
class ThistleParser<
    RenderContext : ThistleRenderContext,
    Tag : Any,
    StyledText : Any
    > internal constructor(
    private val tags: ThistleTagMap<RenderContext, Tag, StyledText>,
    private val parser: Parser<ThistleRootNode>,
    private val rendererFactory: ThistleRendererFactory<RenderContext, Tag, StyledText>,
) : Parser<ThistleRootNode> by parser {

    internal val tagNames: Set<String> = tags.keys

    fun parse(parserContext: ParserContext): ThistleRootNode {
        return checkParsedCorrectly(
            this,
            parserContext,
            parser.parse(parserContext)
        )
    }

    fun newRenderer(): ThistleRenderer<RenderContext, Tag, StyledText> = rendererFactory.newRenderer(tags)

    companion object {
        operator fun <RenderContext : ThistleRenderContext, Tag : Any, StyledText : Any> invoke(
            defaults: ThistleSyntaxBuilder.Defaults<RenderContext, Tag, StyledText>,
            block: ThistleSyntaxBuilder<RenderContext, Tag, StyledText>.() -> Unit = {}
        ): ThistleParser<RenderContext, Tag, StyledText> {
            val (interpolate, tags) = ThistleSyntaxBuilder<RenderContext, Tag, StyledText>()
                .apply {
                    block()
                    from(defaults)
                }
                .build()

            val parser = FlatMappedParser(
                TagParser(tags = tags.values.map { it.kudzuTagBuilder } + interpolate)
            ) {
                ThistleRootNode(it.nodeList, it.context)
            }

            return ThistleParser(
                tags = tags.mapValues { it.value.tagFactory },
                parser = parser,
                rendererFactory = defaults.rendererFactory()
            )
        }
    }
}
