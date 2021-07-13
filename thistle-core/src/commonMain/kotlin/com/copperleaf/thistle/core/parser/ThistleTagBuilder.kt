package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.parser.tag.TagBuilder
import com.copperleaf.thistle.core.renderer.ThistleRenderContext

@ExperimentalStdlibApi
data class ThistleTagBuilder<RenderContext : ThistleRenderContext, TagRendererResult : Any>(
    val kudzuTagBuilder: TagBuilder<*>,
    val tag: ThistleTag<RenderContext, TagRendererResult>
)
