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
    abstract fun render(rootNode: Node): T

    fun ThistleTagStringBuilder.renderToBuilder(node: Node) {
        when (node) {
            is TextNode -> {
                append(node.text)
            }
            is ManyNode<*> -> {
                node.nodeList.forEach {
                    renderToBuilder(it)
                }
            }
            is TagNode<*, *> -> {
                val openingTag = node.opening as ThistleTagStartNode
                val tagName = openingTag.tagName
                val tagArgs = openingTag.tagArgs
                val span = tags.first { it.kudzuTagBuilder.name == tagName }.tag

                pushTag(span(tagArgs)) {
                    (node.content as ManyNode<Node>).nodeList.forEach {
                        renderToBuilder(it)
                    }
                }
            }
            else -> error("unknown node: $node")
        }
    }
}
