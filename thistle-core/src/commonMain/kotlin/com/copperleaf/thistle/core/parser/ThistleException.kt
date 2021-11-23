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
