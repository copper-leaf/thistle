package com.copperleaf.thistle.parser

import com.copperleaf.kudzu.parser.tag.TagParser

@ExperimentalStdlibApi
class ThistleParser(
    val tags: List<ThistleTagBuilder>
) {
    constructor(block: ThistleSyntaxBuilder.() -> Unit = {}) : this(ThistleSyntax.builder(block))

    val parser = TagParser(tags = tags.map { it.kudzuTagBuilder })
}
