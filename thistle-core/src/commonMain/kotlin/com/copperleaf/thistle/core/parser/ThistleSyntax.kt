package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.tag.TagNameNode
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.thistle.core.node.ThistleInterpolateNode
import com.copperleaf.thistle.core.node.ThistleValueMapNode

public interface ThistleSyntax {

    public fun tagStart(): Parser<TagNameNode<ThistleValueMapNode>>

    public fun tagEnd(): Parser<TagNameNode<Node>>

    public fun interpolate(): Parser<ThistleInterpolateNode>
}
