package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.mapped.ValueNode
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.mapped.FlatMappedParser
import com.copperleaf.kudzu.parser.mapped.MappedParser
import com.copperleaf.kudzu.parser.predict.PredictionParser
import com.copperleaf.kudzu.parser.sequence.SequenceParser
import com.copperleaf.kudzu.parser.text.AnyTokenParser
import com.copperleaf.kudzu.parser.text.LiteralTokenParser
import com.copperleaf.kudzu.parser.text.OptionalWhitespaceParser
import com.copperleaf.thistle.core.node.ThistleInterpolateNode
import com.copperleaf.thistle.core.node.ThistleTagStartNode
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

    override fun tagStart(tagName: String): Parser<ThistleTagStartNode> {
        return FlatMappedParser(
            SequenceParser(
                PredictionParser(
                    MappedParser(
                        SequenceParser(
                            openTagStartToken,
                            OptionalWhitespaceParser(),
                            LiteralTokenParser(tagName),
                            OptionalWhitespaceParser(),
                        )
                    ) {
                        val (_, _, tagNameNode, _) = it.children

                        tagNameNode.text
                    }
                ),
                attrMapParser,
                OptionalWhitespaceParser(),
                openTagEndToken,
            )
        ) {
            val (tagNameNode, attrsNode, _, _) = it.children

            ThistleTagStartNode(
                tagName = (tagNameNode as ValueNode<String>).value,
                tagArgs = attrsNode as ThistleValueMapNode,
                context = it.context
            )
        }
    }

    override fun tagEnd(tagName: String): Parser<Node> {
        return PredictionParser(
            SequenceParser(
                closeTagStartToken,
                OptionalWhitespaceParser(),
                LiteralTokenParser(tagName),
                OptionalWhitespaceParser(),
                closeTagEndToken,
            ) as Parser<Node>
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