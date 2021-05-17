package com.copperleaf.thistle.core

import com.copperleaf.kudzu.node.mapped.ValueNode
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.mapped.FlatMappedParser
import com.copperleaf.thistle.core.node.ThistleValueNode
import com.copperleaf.thistle.core.parser.ThistleTag
import com.copperleaf.thistle.core.renderer.ThistleTagsArgs

inline fun <TagRendererType : Any> ThistleTag<TagRendererType>.checkArgs(
    args: Map<String, Any>,
    crossinline block: ThistleTagsArgs.() -> TagRendererType
): TagRendererType {
    val validator = ThistleTagsArgs(this, args)
    val result = validator.block()
    validator.checkNoMoreArgs()
    return result
}

@ExperimentalStdlibApi
fun <T : Any> Parser<ValueNode<T>>.asThistleValueParser(): Parser<ThistleValueNode> {
    return FlatMappedParser(
        this
    ) {
        ThistleValueNode.StaticValue(
            it.value,
            it.context
        )
    }
}
