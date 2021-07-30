package com.copperleaf.thistle.console.renderer

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.many.ManyNode
import com.copperleaf.kudzu.node.tag.TagNode
import com.copperleaf.kudzu.node.text.TextNode
import com.copperleaf.thistle.console.ansi.AnsiEscapeCode
import com.copperleaf.thistle.console.ansi.AnsiNodeScope
import com.copperleaf.thistle.console.ansi.buildAnsiString
import com.copperleaf.thistle.console.ansi.flatten
import com.copperleaf.thistle.console.ansi.renderToString
import com.copperleaf.thistle.core.ThistleTagMap
import com.copperleaf.thistle.core.node.ThistleInterpolateNode
import com.copperleaf.thistle.core.node.ThistleRootNode
import com.copperleaf.thistle.core.node.ThistleValueMapNode
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.ThistleRenderer

@ExperimentalStdlibApi
class ConsoleThistleRenderer(
    tags: ThistleTagMap<ConsoleThistleRenderContext, AnsiEscapeCode, String>
) : ThistleRenderer<ConsoleThistleRenderContext, AnsiEscapeCode, String>(tags) {

    override fun render(rootNode: ThistleRootNode, context: Map<String, Any>): String {
        return buildAnsiString {
            renderToBuilder(rootNode, context)
        }.flatten()
            .renderToString()
    }

    @Suppress("UNCHECKED_CAST")
    private fun AnsiNodeScope.renderToBuilder(node: Node, context: Map<String, Any>) {
        when (node) {
            is TextNode -> {
                appendText(node.text)
            }
            is ThistleRootNode -> {
                node.nodeList.forEach {
                    renderToBuilder(it, context)
                }
            }
            is ManyNode<*> -> {
                node.nodeList.forEach {
                    renderToBuilder(it, context)
                }
            }
            is TagNode<*, *, *> -> {
                val tagName = node.opening.tagName
                val openingTagNode = node.opening.wrapped
                when (openingTagNode) {
                    is ThistleInterpolateNode -> {
                        val interpolatedValue = openingTagNode.getValue(context)
                        appendText(interpolatedValue.toString())
                    }
                    is ThistleValueMapNode -> {
                        val tagArgs = openingTagNode.getValueMap(context)
                        println("tags.keys=${tags.keys}")
                        val span: ThistleTagFactory<ConsoleThistleRenderContext, AnsiEscapeCode> =
                            tags[tagName] ?: error("Unknown tag: $tagName. Valid tag names: ${tags.keys}")

                        appendChild({
                            val renderContext = ConsoleThistleRenderContext(
                                context = context,
                                args = tagArgs,
                                content = it
                            )
                            span(renderContext)
                        }) {
                            (node.content as ManyNode<Node>).nodeList.forEach {
                                renderToBuilder(it, context)
                            }
                        }
                    }
                    else -> error("unknown content in openingTagNode: $openingTagNode")
                }
            }
            else -> error("unknown node: $node")
        }
    }
}
