package com.copperleaf.thistle.parser

import com.copperleaf.kudzu.parser.tag.TagBuilder

@ExperimentalStdlibApi
data class ThistleTagBuilder(
    val kudzuTagBuilder: TagBuilder<*>,
    val tag: ThistleTag
)
