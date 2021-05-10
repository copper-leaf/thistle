package com.copperleaf.thistle.renderer

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.many.ManyNode
import com.copperleaf.kudzu.node.tag.TagNode
import com.copperleaf.kudzu.node.text.TextNode
import com.copperleaf.thistle.node.ThistleTagStartNode
import com.copperleaf.thistle.parser.ThistleTagBuilder

@ExperimentalStdlibApi
@Suppress("UNCHECKED_CAST")
abstract class ThistleRenderer<T : Any>(
    protected val tags: List<ThistleTagBuilder>
) {
    abstract fun render(
        rootNode: Node,
        context: Map<String, Any> = emptyMap()
    ): T

    fun ThistleTagStringBuilder.renderToBuilder(node: Node, context: Map<String, Any>) {
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
                val openingTag = node.opening as ThistleTagStartNode
                val tagName = openingTag.tagName
                val tagArgs = openingTag.tagArgs.getValueMap(context)
                val span = tags.first { it.kudzuTagBuilder.name == tagName }.tag

                pushTag(span(context, tagArgs)) {
                    (node.content as ManyNode<Node>).nodeList.forEach {
                        renderToBuilder(it, context)
                    }
                }
            }
            else -> error("unknown node: $node")
        }
    }
}
