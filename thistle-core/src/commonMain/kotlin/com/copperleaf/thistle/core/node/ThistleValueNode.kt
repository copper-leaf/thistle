package com.copperleaf.thistle.core.node

import com.copperleaf.kudzu.node.NodeContext
import com.copperleaf.kudzu.node.TerminalNode

@ExperimentalStdlibApi
public sealed class ThistleValueNode(
    context: NodeContext
) : TerminalNode(context) {

    public abstract fun getValue(context: Map<String, Any>): Any

    public class StaticValue(
        public val value: Any,
        context: NodeContext
    ) : ThistleValueNode(context) {
        override val text: String
            get() = value.toString()

        override fun getValue(context: Map<String, Any>): Any {
            return value
        }
    }

    public class ContextValue(
        override val text: String,
        public val valueFn: (Map<String, Any>) -> Any,
        context: NodeContext
    ) : ThistleValueNode(context) {
        override fun getValue(context: Map<String, Any>): Any {
            return valueFn(context)
        }
    }
}
