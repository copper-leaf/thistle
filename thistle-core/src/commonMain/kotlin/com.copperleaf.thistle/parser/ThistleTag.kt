package com.copperleaf.thistle.parser

fun interface ThistleTag {
    operator fun invoke(context: Map<String, Any>, args: Map<String, Any>): Any
}
