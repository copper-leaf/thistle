package com.copperleaf.thistle.compose.renderer

import com.copperleaf.thistle.core.renderer.ThistleRenderContext

public class ComposeThistleRenderContext(
    override val context: Map<String, Any>,
    override val args: Map<String, Any>,
    public val content: String
) : ThistleRenderContext
