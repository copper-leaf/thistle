package com.copperleaf.thistle.compose.renderer

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.many.ManyNode
import com.copperleaf.kudzu.node.tag.TagNode
import com.copperleaf.kudzu.node.text.TextNode
import com.copperleaf.thistle.compose.ComposeRichText
import com.copperleaf.thistle.compose.ComposeSpanWrapper
import com.copperleaf.thistle.core.ThistleTagMap
import com.copperleaf.thistle.core.node.ThistleInterpolateNode
import com.copperleaf.thistle.core.node.ThistleRootNode
import com.copperleaf.thistle.core.node.ThistleValueMapNode
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.ThistleRenderer

@ExperimentalStdlibApi
class ComposeThistleRenderer(
    tags: ThistleTagMap<ComposeThistleRenderContext, ComposeSpanWrapper, ComposeRichText>
) : ThistleRenderer<ComposeThistleRenderContext, ComposeSpanWrapper, ComposeRichText>(tags) {

    private var linkId = 0
    private var linkHandlers = mutableListOf<() -> Unit>()

    private var inlineContentId = 0
    private var inlineContentHandlers = mutableMapOf<String, InlineTextContent>()

    override fun render(rootNode: ThistleRootNode, context: Map<String, Any>): ComposeRichText {
        val annotatedString = buildAnnotatedString {
            renderToBuilder(rootNode, context)
        }
        return ComposeRichText(
            annotatedString,
            linkHandlers.toList(),
            inlineContentHandlers.toMap()
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun AnnotatedString.Builder.renderToBuilder(node: Node, context: Map<String, Any>) {
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
                        val tagArgs = openingTagNode.getValueMap(context)
                        val span: ThistleTagFactory<ComposeThistleRenderContext, ComposeSpanWrapper> = tags[tagName]!!

                        val renderContext = ComposeThistleRenderContext(
                            context = context,
                            args = tagArgs
                        )

                        val wrappedSpanInfo = span(renderContext)

                        if (wrappedSpanInfo.clickHandler != null) {
                            pushStringAnnotation(tag = "thistleLinkHandler", "$linkId")
                            linkHandlers.add(wrappedSpanInfo.clickHandler)
                            linkId++
                        }

                        if (wrappedSpanInfo.inlineContent != null) {
                            appendInlineContent("$inlineContentId")
                            inlineContentHandlers["$inlineContentId"] = wrappedSpanInfo.inlineContent
                            inlineContentId++
                        }

                        if (wrappedSpanInfo.spanStyle != null) {
                            withStyle(
                                style = wrappedSpanInfo.spanStyle
                            ) {
                                (node.content as ManyNode<Node>).nodeList.forEach {
                                    renderToBuilder(it, context)
                                }
                            }
                        } else {
                            (node.content as ManyNode<Node>).nodeList.forEach {
                                renderToBuilder(it, context)
                            }
                        }

                        if (wrappedSpanInfo.clickHandler != null) {
                            pop()
                        }
                    }
                    else -> error("unknown content in openingTagNode: $openingTagNode")
                }
            }
            else -> error("unknown node: $node")
        }
    }
}
