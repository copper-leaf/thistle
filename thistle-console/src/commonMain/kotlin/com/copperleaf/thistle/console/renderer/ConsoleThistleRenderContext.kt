package com.copperleaf.thistle.console.renderer

import com.copperleaf.thistle.core.renderer.ThistleRenderContext

public class ConsoleThistleRenderContext(
    override val context: Map<String, Any>,
    override val args: Map<String, Any>,
    public val content: String
) : ThistleRenderContext
