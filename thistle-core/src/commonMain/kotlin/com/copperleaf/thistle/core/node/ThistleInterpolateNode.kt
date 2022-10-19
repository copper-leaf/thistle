package com.copperleaf.thistle.core.node

import com.copperleaf.kudzu.node.NodeContext
import com.copperleaf.kudzu.node.TerminalNode

public class ThistleInterpolateNode(
    public val key: String,
    context: NodeContext
) : TerminalNode(context) {

    override val text: String
        get() = key

    public fun getValue(context: Map<String, Any>): Any {
        check(context.containsKey(key)) {
            "Error: Context must contain value for key '$key'"
        }

        return context[key]!!
    }
}
