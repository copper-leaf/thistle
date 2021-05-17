package com.copperleaf.thistle.core.renderer

interface ThistleTagStringBuilder<TagRendererType : Any> {
    fun append(text: String)
    fun pushTag(tag: TagRendererType, tagContentBuilder: ThistleTagStringBuilder<TagRendererType>.() -> Unit)
}
