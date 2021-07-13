package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.parser.tag.TagBuilder
import com.copperleaf.kudzu.parser.tag.TagParser
import com.copperleaf.thistle.core.renderer.ThistleRenderContext

@ExperimentalStdlibApi
class ThistleParser<RenderContext : ThistleRenderContext, TagRendererResult : Any>(
    val interpolate: TagBuilder<*>,
    val tags: List<ThistleTagBuilder<RenderContext, TagRendererResult>>,
) {
    constructor(
        defaults: ThistleSyntaxBuilder.Defaults<RenderContext, TagRendererResult>,
        block: ThistleSyntaxBuilder<RenderContext, TagRendererResult>.() -> Unit = {}
    ) : this(ThistleSyntax.builder(defaults, block))
    private constructor(
        built: Pair<TagBuilder<*>, List<ThistleTagBuilder<RenderContext, TagRendererResult>>>
    ) : this(built.first, built.second)

    val parser = TagParser(tags = tags.map { it.kudzuTagBuilder } + interpolate)
}
