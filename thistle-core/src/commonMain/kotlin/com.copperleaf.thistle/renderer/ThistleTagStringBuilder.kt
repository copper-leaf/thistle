package com.copperleaf.thistle.renderer

interface ThistleTagStringBuilder {
    fun append(text: String)
    fun pushTag(tag: Any, tagContentBuilder: ThistleTagStringBuilder.() -> Unit)
}
