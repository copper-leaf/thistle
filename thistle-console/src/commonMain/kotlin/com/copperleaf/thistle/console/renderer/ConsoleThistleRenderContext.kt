package com.copperleaf.thistle.console.renderer

import com.copperleaf.thistle.core.renderer.ThistleRenderContext

class ConsoleThistleRenderContext(
    override val context: Map<String, Any>,
    override val args: Map<String, Any>,
    val content: String
) : ThistleRenderContext
