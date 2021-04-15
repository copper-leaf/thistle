package com.copperleaf.thistle.node

import com.copperleaf.kudzu.node.NodeContext
import com.copperleaf.kudzu.node.TerminalNode

@ExperimentalStdlibApi
class ThistleTagStartNode(
    val tagName: String,
    val tagArgs: Map<String, Any>,
    context: NodeContext
) : TerminalNode(context) {
    override val text: String
        get() = if (tagArgs.isEmpty()) {
            tagName
        } else {
            "$tagName$tagArgs)"
        }
}
