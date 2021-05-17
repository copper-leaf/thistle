package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.tag.TagBuilder
import com.copperleaf.thistle.core.node.ThistleInterpolateNode
import com.copperleaf.thistle.core.node.ThistleTagStartNode

@ExperimentalStdlibApi
interface ThistleSyntax {

    fun tagStart(tagName: String): Parser<ThistleTagStartNode>

    fun tagEnd(tagName: String): Parser<Node>

    fun interpolate(): Parser<ThistleInterpolateNode>

    companion object {
        fun <TagRendererType : Any> builder(
            defaults: ThistleSyntaxBuilder.Defaults<TagRendererType>,
            block: ThistleSyntaxBuilder<TagRendererType>.() -> Unit = {}
        ): Pair<TagBuilder<*>, List<ThistleTagBuilder<TagRendererType>>> {
            return ThistleSyntaxBuilder<TagRendererType>()
                .apply {
                    block()
                    from(defaults)
                }
                .build()
        }
    }
}
