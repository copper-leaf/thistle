package com.copperleaf.thistle.parser

fun interface ThistleTag {
    operator fun invoke(args: Map<String, Any>): Any
}
