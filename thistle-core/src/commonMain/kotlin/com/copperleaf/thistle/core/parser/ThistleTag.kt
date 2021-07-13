package com.copperleaf.thistle.core.parser

import com.copperleaf.thistle.core.renderer.ThistleRenderContext

fun interface ThistleTag<RenderContext : ThistleRenderContext, TagRendererResult : Any> {
    operator fun invoke(renderContext: RenderContext): TagRendererResult
}
