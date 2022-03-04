package com.copperleaf.thistle.core.renderer

import com.copperleaf.thistle.core.ThistleTagMap
import com.copperleaf.thistle.core.node.ThistleRootNode

@ExperimentalStdlibApi
@Suppress("UNCHECKED_CAST")
public abstract class ThistleRenderer<RenderContext : ThistleRenderContext, Tag : Any, StyledText : Any>(
    protected val tags: ThistleTagMap<RenderContext, Tag, StyledText>
) {
    public abstract fun render(
        rootNode: ThistleRootNode,
        context: Map<String, Any> = emptyMap()
    ): StyledText
}
