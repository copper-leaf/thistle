package com.copperleaf.thistle.android.renderer

import android.content.Context
import android.text.Spanned
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
class AndroidThistleRenderer(
    val uiContext: Context,
    tags: List<ThistleTagBuilder<AndroidThistleRenderContext, Any>>
) : ThistleRenderer<AndroidThistleRenderContext, Any, Spanned>(tags) {

    override fun render(rootNode: Node, context: Map<String, Any>): Spanned {
        return AndroidThistleTagStringBuilder(uiContext)
            .apply { renderToBuilder(rootNode, context) }
            .toSpanned()
    }

    @Suppress("UNCHECKED_CAST")
    private fun AndroidThistleTagStringBuilder.renderToBuilder(node: Node, context: Map<String, Any>) {
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
                        // get the info parsed from the opening tag
                        val tagName = tagNode.tagName
                        val tagArgs = tagNode.tagArgs.getValueMap(context)
                        val span: ThistleTag<AndroidThistleRenderContext, Any> = tags
                            .first { it.kudzuTagBuilder.name == tagName }
                            .tag

                        // sub-parse to get the tag content
                        val renderContext = pushTag(
                            context = context,
                            args = tagArgs,
                        ) {
                            (node.content as ManyNode<Node>).nodeList.forEach {
                                renderToBuilder(it, context)
                            }
                        }

                        // render the actual tag, which may introspect its own rendered content, if needed
                        val rendered = span(renderContext)

                        // push the tag into the string builder
                        setTag(renderContext, rendered)
                    }
                    else -> error("unknown content in tagNode: $tagNode")
                }
            }
            else -> error("unknown node: $node")
        }
    }
}
