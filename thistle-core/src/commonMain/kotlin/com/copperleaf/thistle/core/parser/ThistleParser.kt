package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.kudzu.parser.mapped.FlatMappedParser
import com.copperleaf.kudzu.parser.tag.TagParser
import com.copperleaf.thistle.core.ThistleTagMap
import com.copperleaf.thistle.core.checkParsedCorrectly
import com.copperleaf.thistle.core.node.ThistleRootNode
import com.copperleaf.thistle.core.renderer.ThistleRenderContext
import com.copperleaf.thistle.core.renderer.ThistleRenderer

@ExperimentalStdlibApi
class ThistleParser<
        RenderContext : ThistleRenderContext,
        TagRendererResult : Any,
        ResultType : Any
        > internal constructor(
    private val tags: ThistleTagMap<RenderContext, TagRendererResult, ResultType>,
    private val parser: Parser<ThistleRootNode>,
    private val rendererFactory: (ThistleTagMap<RenderContext, TagRendererResult, ResultType>) -> ThistleRenderer<RenderContext, TagRendererResult, ResultType>,
) : Parser<ThistleRootNode> by parser {

    internal val tagNames: Set<String> = tags.keys

    fun parse(parserContext: ParserContext): ThistleRootNode {
        return checkParsedCorrectly(
            this,
            parserContext,
            parser.parse(parserContext)
        )
    }

    fun newRenderer(): ThistleRenderer<RenderContext, TagRendererResult, ResultType> = rendererFactory(tags)

    companion object {
        operator fun <RenderContext : ThistleRenderContext, TagRendererResult : Any, ResultType : Any> invoke(
            defaults: ThistleSyntaxBuilder.Defaults<RenderContext, TagRendererResult, ResultType>,
            block: ThistleSyntaxBuilder<RenderContext, TagRendererResult, ResultType>.() -> Unit = {}
        ): ThistleParser<RenderContext, TagRendererResult, ResultType> {
            val (interpolate, tags) = ThistleSyntaxBuilder<RenderContext, TagRendererResult, ResultType>()
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
