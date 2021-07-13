package com.copperleaf.thistle.core.renderer

interface ThistleRenderContext {
    /**
     * Values passed in to the parser by the end-user.
     */
    val context: Map<String, Any>

    /**
     * Values parsed directly from the tag
     */
    val args: Map<String, Any>
}
