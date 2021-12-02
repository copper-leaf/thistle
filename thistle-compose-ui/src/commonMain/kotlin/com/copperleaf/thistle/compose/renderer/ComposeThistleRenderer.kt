package com.copperleaf.thistle.compose.renderer

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.NonTerminalNode
import com.copperleaf.kudzu.node.many.ManyNode
import com.copperleaf.kudzu.node.tag.TagNode
import com.copperleaf.kudzu.node.text.TextNode
import com.copperleaf.thistle.compose.util.ComposeRichText
import com.copperleaf.thistle.compose.util.ComposeSpanWrapper
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
        return kotlin.runCatching {
            val annotatedString = buildAnnotatedString {
                renderToBuilder(rootNode, context)
            }
            ComposeRichText.Success(
                rootNode,
                annotatedString,
                linkHandlers.toList(),
                inlineContentHandlers.toMap()
            )
        }.getOrElse {
            ComposeRichText.Failure(
                rootNode,
                it,
            )
        }
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
                            args = tagArgs,
                            content = node.content.textContent(context),
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

    // TODO: this is wildly inefficient, with something like exponential time-complexity. Only use for small inputs, and
    //  look for a better way to do it. I'm not sure if there is one since I can't get sub-strings directly from builder.
    //  Maybe make a custom Compose AnnotatedString builder that would let me do what I want?
    private fun Node.textContent(context: Map<String, Any>): String {
        return when(this) {
            is ThistleInterpolateNode -> getValue(context).toString()
            is TextNode -> text
            is NonTerminalNode -> { children.joinToString(separator = "") { it.textContent(context) } }
            else -> ""
        }
    }
}
