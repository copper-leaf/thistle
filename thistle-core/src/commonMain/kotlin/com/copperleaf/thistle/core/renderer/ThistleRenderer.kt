package com.copperleaf.thistle.core.renderer

import com.copperleaf.kudzu.node.Node
import com.copperleaf.thistle.core.parser.ThistleTagBuilder

@ExperimentalStdlibApi
@Suppress("UNCHECKED_CAST")
abstract class ThistleRenderer<RenderContext : ThistleRenderContext, TagRendererResult : Any, ResultType : Any>(
    protected val tags: List<ThistleTagBuilder<RenderContext, TagRendererResult>>
) {
    abstract fun render(
        rootNode: Node,
        context: Map<String, Any> = emptyMap()
    ): ResultType
}
