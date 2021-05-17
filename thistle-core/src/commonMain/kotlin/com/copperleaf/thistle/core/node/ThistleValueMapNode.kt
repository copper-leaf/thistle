package com.copperleaf.thistle.core.node

import com.copperleaf.kudzu.node.NodeContext
import com.copperleaf.kudzu.node.TerminalNode

@ExperimentalStdlibApi
class ThistleValueMapNode(
    val valueMap: Map<String, ThistleValueNode>,
    context: NodeContext
) : TerminalNode(context) {
    override val text: String = valueMap.toString()

    fun getValueMap(context: Map<String, Any>): Map<String, Any> {
        return valueMap.mapValues { (_, valueNode) -> valueNode.getValue(context) }
    }
}
