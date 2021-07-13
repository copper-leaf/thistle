package com.copperleaf.thistle.console.renderer

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.many.ManyNode
import com.copperleaf.kudzu.node.tag.TagNode
import com.copperleaf.kudzu.node.text.TextNode
import com.copperleaf.thistle.core.node.ThistleInterpolateNode
import com.copperleaf.thistle.core.node.ThistleTagStartNode
import com.copperleaf.thistle.core.parser.ThistleTag
import com.copperleaf.thistle.core.parser.ThistleTagBuilder
import com.copperleaf.thistle.core.renderer.ThistleRenderer

@ExperimentalStdlibApi
class ConsoleThistleRenderer(
    tags: List<ThistleTagBuilder<ConsoleThistleRenderContext, AnsiEscapeCode>>
) : ThistleRenderer<ConsoleThistleRenderContext, AnsiEscapeCode, String>(tags) {

    override fun render(rootNode: Node, context: Map<String, Any>): String {
        return ConsoleThistleTagStringBuilder()
            .apply { renderToBuilder(rootNode, context) }
            .toString()
    }

    @Suppress("UNCHECKED_CAST")
    private fun ConsoleThistleTagStringBuilder.renderToBuilder(node: Node, context: Map<String, Any>) {
        when (node) {
            is TextNode -> {
                append(node.text)
            }
            is ManyNode<*> -> {
                node.nodeList.forEach {
                    renderToBuilder(it, context)
                }
            }
            is TagNode<*, *> -> {
                val tagNode = node.opening
                when (tagNode) {
                    is ThistleInterpolateNode -> {
                        val interpolatedValue = tagNode.getValue(context)
                        append(interpolatedValue.toString())
                    }
                    is ThistleTagStartNode -> {
                        val tagName = tagNode.tagName
                        val tagArgs = tagNode.tagArgs.getValueMap(context)
                        val span: ThistleTag<ConsoleThistleRenderContext, AnsiEscapeCode> = tags
                            .first { it.kudzuTagBuilder.name == tagName }
                            .tag

                        val renderContext = ConsoleThistleRenderContext(context = context, args = tagArgs)

                        pushTag(span(renderContext)) {
                            (node.content as ManyNode<Node>).nodeList.forEach {
                                renderToBuilder(it, context)
                            }
                        }
                    }
                    else -> error("unknown content in tagNode: $tagNode")
                }
            }
            else -> error("unknown node: $node")
        }
    }
}
