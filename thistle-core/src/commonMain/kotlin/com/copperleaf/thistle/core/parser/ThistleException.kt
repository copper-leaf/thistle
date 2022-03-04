package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.node.NodeContext

public sealed class ThistleException(message: String) : Exception(message)

public class ThistleUnknownTagException(
    public val input: String,
    public val position: NodeContext,
    public val unknownTagName: String,
    public val validTagNames: Set<String>,
) : ThistleException(
    createErrorMessage(
        input,
        position,
        unknownTagName,
        validTagNames,
    )
) {

    public companion object {
        @Suppress("UNUSED_PARAMETER")
        private fun createErrorMessage(
            input: String,
            position: NodeContext,
            unknownTagName: String,
            validTagNames: Set<String>,
        ): String {
            return "Unknown tag: $unknownTagName. Valid tag names: $validTagNames"
        }
    }
}

public class ThistleMissingContextValueException(
    public val nodeContext: NodeContext,
    public val key: String,
    public val context: Map<String, Any>,
) : ThistleException(
    createErrorMessage(
        nodeContext,
        key,
        context
    )
) {

    public companion object {
        @Suppress("UNUSED_PARAMETER")
        private fun createErrorMessage(
            nodeContext: NodeContext,
            key: String,
            context: Map<String, Any>,
        ): String {
            return "At $nodeContext: Context must contain value for key '$key'"
        }
    }
}
