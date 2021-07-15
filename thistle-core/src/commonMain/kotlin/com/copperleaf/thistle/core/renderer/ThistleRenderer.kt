package com.copperleaf.thistle.core.renderer

import com.copperleaf.thistle.core.node.ThistleRootNode
import com.copperleaf.thistle.core.parser.ThistleTag

@ExperimentalStdlibApi
@Suppress("UNCHECKED_CAST")
abstract class ThistleRenderer<RenderContext : ThistleRenderContext, TagRendererResult : Any, ResultType : Any>(
    protected val tags: Map<String, ThistleTag<RenderContext, TagRendererResult>>
) {
    abstract fun render(
        rootNode: ThistleRootNode,
        context: Map<String, Any> = emptyMap()
    ): ResultType
}
