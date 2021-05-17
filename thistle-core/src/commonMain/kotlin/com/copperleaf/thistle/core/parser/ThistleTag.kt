package com.copperleaf.thistle.core.parser

fun interface ThistleTag<TagRendererType : Any> {
    operator fun invoke(context: Map<String, Any>, args: Map<String, Any>): TagRendererType
}
