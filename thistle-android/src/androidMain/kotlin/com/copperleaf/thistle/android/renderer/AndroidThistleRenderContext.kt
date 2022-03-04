package com.copperleaf.thistle.android.renderer

import android.content.Context
import com.copperleaf.thistle.core.renderer.ThistleRenderContext

public class AndroidThistleRenderContext(
    val uiContext: Context,

    internal val startIndex: Int,
    internal val endIndex: Int,

    override val context: Map<String, Any>,
    override val args: Map<String, Any>,
    val content: String
) : ThistleRenderContext
