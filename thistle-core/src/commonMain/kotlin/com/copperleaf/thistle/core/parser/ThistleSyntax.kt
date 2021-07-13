package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.tag.TagBuilder
import com.copperleaf.thistle.core.node.ThistleInterpolateNode
import com.copperleaf.thistle.core.node.ThistleTagStartNode
import com.copperleaf.thistle.core.renderer.ThistleRenderContext

@ExperimentalStdlibApi
interface ThistleSyntax {

    fun tagStart(tagName: String): Parser<ThistleTagStartNode>

    fun tagEnd(tagName: String): Parser<Node>

    fun interpolate(): Parser<ThistleInterpolateNode>

    companion object {
        fun <RenderContext : ThistleRenderContext, TagRendererResult : Any> builder(
            defaults: ThistleSyntaxBuilder.Defaults<RenderContext, TagRendererResult>,
            block: ThistleSyntaxBuilder<RenderContext, TagRendererResult>.() -> Unit = {}
        ): Pair<TagBuilder<*>, List<ThistleTagBuilder<RenderContext, TagRendererResult>>> {
            return ThistleSyntaxBuilder<RenderContext, TagRendererResult>()
                .apply {
                    block()
                    from(defaults)
                }
                .build()
        }
    }
}
