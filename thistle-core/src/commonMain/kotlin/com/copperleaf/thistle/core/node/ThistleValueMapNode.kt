package com.copperleaf.thistle.core.node

import com.copperleaf.kudzu.node.NodeContext
import com.copperleaf.kudzu.node.TerminalNode

public class ThistleValueMapNode(
    public val valueMap: Map<String, ThistleValueNode>,
    context: NodeContext
) : TerminalNode(context) {
    override val text: String = valueMap.toString()

    public fun getValueMap(context: Map<String, Any>): Map<String, Any> {
        return valueMap.mapValues { (_, valueNode) -> valueNode.getValue(context) }
    }
}
