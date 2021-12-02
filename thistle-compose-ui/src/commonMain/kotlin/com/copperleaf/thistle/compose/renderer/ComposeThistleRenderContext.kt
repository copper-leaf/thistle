package com.copperleaf.thistle.compose.renderer

import com.copperleaf.thistle.core.renderer.ThistleRenderContext

class ComposeThistleRenderContext(
    override val context: Map<String, Any>,
    override val args: Map<String, Any>,
    val content: String
) : ThistleRenderContext
