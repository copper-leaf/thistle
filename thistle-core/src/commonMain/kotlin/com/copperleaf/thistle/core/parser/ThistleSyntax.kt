package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.tag.TagNameNode
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.tag.TagBuilder
import com.copperleaf.thistle.core.node.ThistleInterpolateNode
import com.copperleaf.thistle.core.node.ThistleValueMapNode
import com.copperleaf.thistle.core.renderer.ThistleRenderContext

@ExperimentalStdlibApi
interface ThistleSyntax {

    fun tagStart(): Parser<TagNameNode<ThistleValueMapNode>>

    fun tagEnd(): Parser<TagNameNode<Node>>

    fun interpolate(): Parser<ThistleInterpolateNode>

    companion object {
        fun <RenderContext : ThistleRenderContext, TagRendererResult : Any> builder(
            defaults: ThistleSyntaxBuilder.Defaults<RenderContext, TagRendererResult>,
            block: ThistleSyntaxBuilder<RenderContext, TagRendererResult>.() -> Unit = {}
        ): Pair<TagBuilder<*, *>, Map<String, ThistleTagBuilder<RenderContext, TagRendererResult>>> {
            return ThistleSyntaxBuilder<RenderContext, TagRendererResult>()
                .apply {
                    block()
                    from(defaults)
                }
                .build()
        }
    }
}
