package com.copperleaf.thistle.node

import com.copperleaf.kudzu.node.NodeContext
import com.copperleaf.kudzu.node.TerminalNode

@ExperimentalStdlibApi
class ThistleTagStartNode(
    val tagName: String,
    val tagArgs: ThistleValueMapNode,
    context: NodeContext
) : TerminalNode(context) {
    override val text: String
        get() = if (tagArgs.valueMap.isEmpty()) {
            tagName
        } else {
            "$tagName${tagArgs.valueMap})"
        }
}
