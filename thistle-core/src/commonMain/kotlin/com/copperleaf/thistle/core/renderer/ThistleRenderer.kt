package com.copperleaf.thistle.core.renderer

import com.copperleaf.thistle.core.node.ThistleRootNode
import com.copperleaf.thistle.core.parser.ThistleTagFactory

@ExperimentalStdlibApi
@Suppress("UNCHECKED_CAST")
abstract class ThistleRenderer<RenderContext : ThistleRenderContext, TagRendererResult : Any, ResultType : Any>(
    protected val tags: Map<String, ThistleTagFactory<RenderContext, TagRendererResult>>
) {
    abstract fun render(
        rootNode: ThistleRootNode,
        context: Map<String, Any> = emptyMap()
    ): ResultType
}
