package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.parser.tag.TagBuilder
import com.copperleaf.thistle.core.renderer.ThistleRenderContext

@ExperimentalStdlibApi
data class ThistleTagConfiguration<RenderContext : ThistleRenderContext, Tag : Any>(
    val kudzuTagBuilder: TagBuilder<*, *>,
    val tagFactory: ThistleTagFactory<RenderContext, Tag>
)
