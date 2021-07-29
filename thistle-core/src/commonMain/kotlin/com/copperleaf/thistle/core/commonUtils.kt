package com.copperleaf.thistle.core

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.many.ManyNode
import com.copperleaf.kudzu.node.mapped.ValueNode
import com.copperleaf.kudzu.node.tag.TagNode
import com.copperleaf.kudzu.node.text.TextNode
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.kudzu.parser.ParserResult
import com.copperleaf.kudzu.parser.mapped.FlatMappedParser
import com.copperleaf.thistle.core.node.ThistleInterpolateNode
import com.copperleaf.thistle.core.node.ThistleRootNode
import com.copperleaf.thistle.core.node.ThistleValueMapNode
import com.copperleaf.thistle.core.node.ThistleValueNode
import com.copperleaf.thistle.core.parser.ThistleParser
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.ThistleRenderContext
import com.copperleaf.thistle.core.renderer.ThistleTagsArgs

inline fun <RenderContext : ThistleRenderContext, RendererResult : Any>
ThistleTagFactory<RenderContext, RendererResult>.checkArgs(
    renderContext: RenderContext,
    crossinline block: ThistleTagsArgs.() -> RendererResult
): RendererResult {
    val validator = ThistleTagsArgs(this, renderContext.args)
    val result = validator.block()
    validator.checkNoMoreArgs()
    return result
}

@ExperimentalStdlibApi
fun <T : Any> Parser<ValueNode<T>>.asThistleValueParser(): Parser<ThistleValueNode> {
    return FlatMappedParser(
        this
    ) {
        ThistleValueNode.StaticValue(
            it.value,
            it.context
        )
    }
}

@ExperimentalStdlibApi
internal fun checkParsedCorrectly(
    parser: ThistleParser<*, *, *>,
    parserContext: ParserContext,
    parserResult: ParserResult<ThistleRootNode>
): ThistleRootNode {
    check(parserResult.second.isEmpty()) { "$parserContext did not parse to completion" }
    checkValidNode(parser, parserResult.first)
    return parserResult.first
}

@ExperimentalStdlibApi
@Suppress("UNCHECKED_CAST")
private fun checkValidNode(
    parser: ThistleParser<*, *, *>,
    node: Node
) {
    when (node) {
        is TextNode -> {
        }
        is ThistleRootNode -> {
            node.nodeList.forEach {
                checkValidNode(parser, it)
            }
        }
        is ManyNode<*> -> {
            node.nodeList.forEach {
                checkValidNode(parser, it)
            }
        }
        is TagNode<*, *, *> -> {
            val tagName = node.opening.tagName
            val openingTagNode = node.opening.wrapped
            when (openingTagNode) {
                is ThistleInterpolateNode -> {
                }
                is ThistleValueMapNode -> {
                    check(tagName in parser.tagNames) { "Unknown tag: ${node.opening.tagName}" }
                    (node.content as ManyNode<Node>).nodeList.forEach {
                        checkValidNode(parser, it)
                    }
                }
                else -> error("unknown content in openingTagNode: $openingTagNode")
            }
        }
        else -> error("unknown node: $node")
    }
}

@Suppress("UNUSED_TYPEALIAS_PARAMETER")
typealias ThistleTagMap<RenderContext, TagRendererResult, ResultType> =
        Map<String, ThistleTagFactory<RenderContext, TagRendererResult>>
