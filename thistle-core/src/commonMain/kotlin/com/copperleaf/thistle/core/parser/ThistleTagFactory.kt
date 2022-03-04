package com.copperleaf.thistle.core.parser

import com.copperleaf.thistle.core.renderer.ThistleRenderContext

public fun interface ThistleTagFactory<RenderContext : ThistleRenderContext, Tag : Any> {
    public operator fun invoke(renderContext: RenderContext): Tag
}
