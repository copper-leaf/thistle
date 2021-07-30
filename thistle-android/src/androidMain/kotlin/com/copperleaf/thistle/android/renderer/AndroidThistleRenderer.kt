package com.copperleaf.thistle.android.renderer

import android.content.Context
import android.text.Spanned
import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.many.ManyNode
import com.copperleaf.kudzu.node.tag.TagNode
import com.copperleaf.kudzu.node.text.TextNode
import com.copperleaf.thistle.core.node.ThistleInterpolateNode
import com.copperleaf.thistle.core.node.ThistleRootNode
import com.copperleaf.thistle.core.node.ThistleValueMapNode
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.ThistleRenderer

@ExperimentalStdlibApi
class AndroidThistleRenderer(
    val uiContext: Context,
    tags: Map<String, ThistleTagFactory<AndroidThistleRenderContext, Any>>
) : ThistleRenderer<AndroidThistleRenderContext, Any, Spanned>(tags) {

    override fun render(rootNode: ThistleRootNode, context: Map<String, Any>): Spanned {
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
                        append(interpolatedValue.toString())
                    }
                    is ThistleValueMapNode -> {
                        // get the info parsed from the opening tag
                        val tagArgs = openingTagNode.getValueMap(context)
                        val span: ThistleTagFactory<AndroidThistleRenderContext, Any> =
                            tags[tagName] ?: error("unknown tag: $tagName")

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
                    else -> error("unknown content in openingTagNode: $openingTagNode")
                }
            }
            else -> error("unknown node: $node")
        }
    }
}
