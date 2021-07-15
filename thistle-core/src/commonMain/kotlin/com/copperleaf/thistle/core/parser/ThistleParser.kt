package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.kudzu.parser.mapped.FlatMappedParser
import com.copperleaf.kudzu.parser.tag.TagBuilder
import com.copperleaf.kudzu.parser.tag.TagParser
import com.copperleaf.thistle.core.checkParsedCorrectly
import com.copperleaf.thistle.core.node.ThistleRootNode
import com.copperleaf.thistle.core.renderer.ThistleRenderContext

@ExperimentalStdlibApi
class ThistleParser<RenderContext : ThistleRenderContext, TagRendererResult : Any> internal constructor(
    interpolate: TagBuilder<*, *>,
    tags: Map<String, ThistleTagBuilder<RenderContext, TagRendererResult>>,
) {

    constructor(
        defaults: ThistleSyntaxBuilder.Defaults<RenderContext, TagRendererResult>,
        block: ThistleSyntaxBuilder<RenderContext, TagRendererResult>.() -> Unit = {}
    ) : this(ThistleSyntax.builder(defaults, block))

    private constructor(
        built: Pair<TagBuilder<*, *>, Map<String, ThistleTagBuilder<RenderContext, TagRendererResult>>>
    ) : this(built.first, built.second)

    internal val tagNames = tags.keys
    internal val tagFactories = tags.mapValues { it.value.tag }
    internal val parser = FlatMappedParser(
        TagParser(tags = tags.values.map { it.kudzuTagBuilder } + interpolate)
    ) {
        ThistleRootNode(it.nodeList, it.context)
    }

    fun parse(parserContext: ParserContext): ThistleRootNode {
        return checkParsedCorrectly(
            this,
            parserContext,
            parser.parse(parserContext)
        )
    }
}
