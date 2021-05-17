package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.parser.tag.TagBuilder

@ExperimentalStdlibApi
data class ThistleTagBuilder<TagRendererType : Any>(
    val kudzuTagBuilder: TagBuilder<*>,
    val tag: ThistleTag<TagRendererType>
)
