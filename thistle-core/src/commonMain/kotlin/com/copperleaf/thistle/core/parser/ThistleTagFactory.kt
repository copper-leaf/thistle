package com.copperleaf.thistle.core.parser

import com.copperleaf.thistle.core.renderer.ThistleRenderContext

fun interface ThistleTagFactory<RenderContext : ThistleRenderContext, TagRendererResult : Any> {
    operator fun invoke(renderContext: RenderContext): TagRendererResult
}
