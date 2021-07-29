package com.copperleaf.thistle.core.renderer

import com.copperleaf.thistle.core.ThistleTagMap
import com.copperleaf.thistle.core.node.ThistleRootNode

@ExperimentalStdlibApi
@Suppress("UNCHECKED_CAST")
abstract class ThistleRenderer<RenderContext : ThistleRenderContext, TagRendererResult : Any, ResultType : Any>(
    protected val tags: ThistleTagMap<RenderContext, TagRendererResult, ResultType>
) {
    abstract fun render(
        rootNode: ThistleRootNode,
        context: Map<String, Any> = emptyMap()
    ): ResultType
}
