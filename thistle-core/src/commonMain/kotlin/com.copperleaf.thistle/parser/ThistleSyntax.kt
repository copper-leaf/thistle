package com.copperleaf.thistle.parser

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.thistle.node.ThistleTagStartNode

@ExperimentalStdlibApi
interface ThistleSyntax {

    fun tagStart(tagName: String): Parser<ThistleTagStartNode>

    fun tagEnd(tagName: String): Parser<Node>

    companion object {
        fun builder(block: ThistleSyntaxBuilder.() -> Unit = {}): List<ThistleTagBuilder> {
            return ThistleSyntaxBuilder().apply(block).build()
        }
    }
}
