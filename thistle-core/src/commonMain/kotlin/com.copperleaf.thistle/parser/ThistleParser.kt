package com.copperleaf.thistle.parser

import com.copperleaf.kudzu.parser.tag.TagParser

@ExperimentalStdlibApi
class ThistleParser(
    val tags: List<ThistleTagBuilder>
) {
    val parser = TagParser(tags = tags.map { it.kudzuTagBuilder })
}
