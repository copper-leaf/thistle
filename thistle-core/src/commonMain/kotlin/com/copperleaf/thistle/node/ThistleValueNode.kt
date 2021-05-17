package com.copperleaf.thistle.node

import com.copperleaf.kudzu.node.NodeContext
import com.copperleaf.kudzu.node.TerminalNode

@ExperimentalStdlibApi
sealed class ThistleValueNode(
    context: NodeContext
) : TerminalNode(context) {

    abstract fun getValue(context: Map<String, Any>): Any

    class StaticValue(
        val value: Any,
        context: NodeContext
    ) : ThistleValueNode(context) {
        override val text: String
            get() = value.toString()

        override fun getValue(context: Map<String, Any>): Any {
            return value
        }
    }

    class ContextValue(
        override val text: String,
        val valueFn: (Map<String, Any>) -> Any,
        context: NodeContext
    ) : ThistleValueNode(context) {
        override fun getValue(context: Map<String, Any>): Any {
            return valueFn(context)
        }
    }
}
