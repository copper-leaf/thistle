package com.copperleaf.thistle.parser

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.mapped.ValueNode
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.mapped.FlatMappedParser
import com.copperleaf.kudzu.parser.predict.PredictionParser
import com.copperleaf.kudzu.parser.sequence.SequenceParser
import com.copperleaf.kudzu.parser.text.LiteralTokenParser
import com.copperleaf.kudzu.parser.text.OptionalWhitespaceParser
import com.copperleaf.thistle.node.ThistleTagStartNode

@ExperimentalStdlibApi
interface ThistleSyntax {

    fun tagStart(tagName: String): Parser<ThistleTagStartNode>

    fun tagEnd(tagName: String): Parser<Node>

    @Suppress("UNCHECKED_CAST")
    class Impl(
        private val openTagStartToken: Parser<*>,
        private val openTagEndToken: Parser<*>,
        private val closeTagStartToken: Parser<*>,
        private val closeTagEndToken: Parser<*>,
        private val attrMapParser: Parser<ValueNode<Map<String, Any>>>
    ) : ThistleSyntax {

        override fun tagStart(tagName: String): Parser<ThistleTagStartNode> {
            return PredictionParser(
                FlatMappedParser(
                    SequenceParser(
                        openTagStartToken,
                        OptionalWhitespaceParser(),
                        LiteralTokenParser(tagName),
                        OptionalWhitespaceParser(),
                        attrMapParser,
                        OptionalWhitespaceParser(),
                        openTagEndToken,
                    )
                ) {
                    val (_, _, tagNameNode, _, attrsNode) = it.children

                    ThistleTagStartNode(
                        tagName = tagNameNode.text,
                        tagArgs = (attrsNode as ValueNode<Map<String, Any>>).value,
                        context = it.context
                    )
                }
            )
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
    }

    companion object {
        fun builder(block: ThistleSyntaxBuilder.() -> Unit = {}): List<ThistleTagBuilder> {
            return ThistleSyntaxBuilder().apply(block).build()
        }
    }
}
