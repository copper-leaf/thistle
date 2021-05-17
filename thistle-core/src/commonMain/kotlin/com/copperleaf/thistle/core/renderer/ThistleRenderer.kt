package com.copperleaf.thistle.core.renderer

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.many.ManyNode
import com.copperleaf.kudzu.node.tag.TagNode
import com.copperleaf.kudzu.node.text.TextNode
import com.copperleaf.thistle.core.node.ThistleInterpolateNode
import com.copperleaf.thistle.core.node.ThistleTagStartNode
import com.copperleaf.thistle.core.parser.ThistleTag
import com.copperleaf.thistle.core.parser.ThistleTagBuilder

@ExperimentalStdlibApi
@Suppress("UNCHECKED_CAST")
abstract class ThistleRenderer<TagRendererType : Any, ResultType : Any>(
    protected val tags: List<ThistleTagBuilder<TagRendererType>>
) {
    abstract fun render(
        rootNode: Node,
        context: Map<String, Any> = emptyMap()
    ): ResultType

    fun ThistleTagStringBuilder<TagRendererType>.renderToBuilder(node: Node, context: Map<String, Any>) {
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
                        val span: ThistleTag<TagRendererType> = tags.first { it.kudzuTagBuilder.name == tagName }.tag

                        pushTag(span(context, tagArgs)) {
                            (node.content as ManyNode<Node>).nodeList.forEach {
                                renderToBuilder(it, context)
                            }
                        }
                    }
                }
            }
            else -> error("unknown node: $node")
        }
    }
}
