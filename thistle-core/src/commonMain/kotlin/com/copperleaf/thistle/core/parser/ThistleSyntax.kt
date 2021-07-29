package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.tag.TagNameNode
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.thistle.core.node.ThistleInterpolateNode
import com.copperleaf.thistle.core.node.ThistleValueMapNode

@ExperimentalStdlibApi
interface ThistleSyntax {

    fun tagStart(): Parser<TagNameNode<ThistleValueMapNode>>

    fun tagEnd(): Parser<TagNameNode<Node>>

    fun interpolate(): Parser<ThistleInterpolateNode>
}
