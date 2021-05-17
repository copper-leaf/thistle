package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.parser.tag.TagBuilder
import com.copperleaf.kudzu.parser.tag.TagParser

@ExperimentalStdlibApi
class ThistleParser(
    val interpolate: TagBuilder<*>,
    val tags: List<ThistleTagBuilder>,
) {
    constructor(block: ThistleSyntaxBuilder.() -> Unit = {}) : this(ThistleSyntax.builder(block))
    private constructor(built: Pair<TagBuilder<*>, List<ThistleTagBuilder>>) : this(built.first, built.second)

    val parser = TagParser(tags = tags.map { it.kudzuTagBuilder } + interpolate)
}
