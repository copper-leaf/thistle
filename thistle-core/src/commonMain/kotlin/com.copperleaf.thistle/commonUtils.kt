package com.copperleaf.thistle

import com.copperleaf.thistle.parser.ThistleTag
import com.copperleaf.thistle.renderer.ThistleTagsArgs

inline fun ThistleTag.checkArgs(
    args: Map<String, Any>,
    crossinline block: ThistleTagsArgs.() -> Any
): Any {
    val validator = ThistleTagsArgs(this, args)
    val result = validator.block()
    validator.checkNoMoreArgs()
    return result
}
