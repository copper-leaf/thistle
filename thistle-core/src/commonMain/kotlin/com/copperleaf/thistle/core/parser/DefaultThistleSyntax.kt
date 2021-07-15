package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.tag.TagNameNode
import com.copperleaf.kudzu.node.text.TextNode
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.mapped.FlatMappedParser
import com.copperleaf.kudzu.parser.predict.PredictionParser
import com.copperleaf.kudzu.parser.sequence.SequenceParser
import com.copperleaf.kudzu.parser.text.AnyTokenParser
import com.copperleaf.kudzu.parser.text.OptionalWhitespaceParser
import com.copperleaf.thistle.core.node.ThistleInterpolateNode
import com.copperleaf.thistle.core.node.ThistleValueMapNode

@ExperimentalStdlibApi
@Suppress("UNCHECKED_CAST")
class DefaultThistleSyntax(
    private val openTagStartToken: Parser<*>,
    private val openTagEndToken: Parser<*>,
    private val closeTagStartToken: Parser<*>,
    private val closeTagEndToken: Parser<*>,
    private val interpolateStartToken: Parser<*>,
    private val interpolateEndToken: Parser<*>,
    private val attrMapParser: Parser<ThistleValueMapNode>
) : ThistleSyntax {

    override fun tagStart(): Parser<TagNameNode<ThistleValueMapNode>> {
        return FlatMappedParser(
            SequenceParser(
                PredictionParser(
                    SequenceParser(
                        openTagStartToken,
                        OptionalWhitespaceParser(),
                        AnyTokenParser(),
                        OptionalWhitespaceParser(),
                    )
                ),
                attrMapParser,
                OptionalWhitespaceParser(),
                openTagEndToken,
            )
        ) { (nodeContext, tagNameStartNode, attrsNode, _, _) ->
            val (_, _, _, tagNameNode: TextNode) = tagNameStartNode

            TagNameNode(
                tagName = tagNameNode.text,
                wrapped = attrsNode,
                context = nodeContext
            )
        }
    }

    override fun tagEnd(): Parser<TagNameNode<Node>> {
        return PredictionParser(
            FlatMappedParser(
                SequenceParser(
                    closeTagStartToken,
                    OptionalWhitespaceParser(),
                    AnyTokenParser(),
                    OptionalWhitespaceParser(),
                    closeTagEndToken,
                )
            ) { (nodeContext, _, _, tagNameNode) ->
                TagNameNode(
                    tagName = tagNameNode.text,
                    wrapped = tagNameNode,
                    context = nodeContext
                )
            }
        )
    }

    override fun interpolate(): Parser<ThistleInterpolateNode> {
        return PredictionParser(
            FlatMappedParser(
                SequenceParser(
                    interpolateStartToken,
                    OptionalWhitespaceParser(),
                    AnyTokenParser(),
                    OptionalWhitespaceParser(),
                    interpolateEndToken,
                )
            ) {
                val (_, _, name, _, _) = it.children

                ThistleInterpolateNode(name.text, it.context)
            }
        )
    }
}
