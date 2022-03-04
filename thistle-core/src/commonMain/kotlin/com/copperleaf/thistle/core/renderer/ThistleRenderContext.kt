package com.copperleaf.thistle.core.renderer

public interface ThistleRenderContext {
    /**
     * Values passed in to the parser by the end-user.
     */
    public val context: Map<String, Any>

    /**
     * Values parsed directly from the tag
     */
    public val args: Map<String, Any>
}
