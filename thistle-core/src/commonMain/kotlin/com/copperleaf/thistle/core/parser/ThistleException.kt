package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.node.NodeContext

sealed class ThistleException(message: String) : Exception(message)

class ThistleUnknownTagException(
    val input: String,
    val position: NodeContext,
    val unknownTagName: String,
    val validTagNames: Set<String>,
) : ThistleException(
    createErrorMessage(
        input,
        position,
        unknownTagName,
        validTagNames,
    )
) {

    companion object {
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

class ThistleMissingContextValueException(
    val nodeContext: NodeContext,
    val key: String,
    val context: Map<String, Any>,
) : ThistleException(
    createErrorMessage(
        nodeContext,
        key,
        context
    )
) {

    companion object {
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
