package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.parser.tag.TagBuilder
import com.copperleaf.kudzu.parser.tag.TagParser

@ExperimentalStdlibApi
class ThistleParser<TagRendererType : Any>(
    val interpolate: TagBuilder<*>,
    val tags: List<ThistleTagBuilder<TagRendererType>>,
) {
    constructor(
        defaults: ThistleSyntaxBuilder.Defaults<TagRendererType>,
        block: ThistleSyntaxBuilder<TagRendererType>.() -> Unit = {}
    ) : this(ThistleSyntax.builder(defaults, block))
    private constructor(
        built: Pair<TagBuilder<*>, List<ThistleTagBuilder<TagRendererType>>>
    ) : this(built.first, built.second)

    val parser = TagParser(tags = tags.map { it.kudzuTagBuilder } + interpolate)
}
