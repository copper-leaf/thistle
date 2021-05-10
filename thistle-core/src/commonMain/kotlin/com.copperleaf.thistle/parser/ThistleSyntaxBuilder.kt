package com.copperleaf.thistle.parser

import com.copperleaf.kudzu.node.mapped.ValueNode
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.chars.CharInParser
import com.copperleaf.kudzu.parser.chars.HexDigitParser
import com.copperleaf.kudzu.parser.choice.ExactChoiceParser
import com.copperleaf.kudzu.parser.many.SeparatedByParser
import com.copperleaf.kudzu.parser.many.TimesParser
import com.copperleaf.kudzu.parser.mapped.FlatMappedParser
import com.copperleaf.kudzu.parser.mapped.MappedParser
import com.copperleaf.kudzu.parser.maybe.MaybeParser
import com.copperleaf.kudzu.parser.sequence.SequenceParser
import com.copperleaf.kudzu.parser.tag.TagBuilder
import com.copperleaf.kudzu.parser.text.AnyTokenParser
import com.copperleaf.kudzu.parser.text.IdentifierTokenParser
import com.copperleaf.kudzu.parser.text.LiteralTokenParser
import com.copperleaf.kudzu.parser.text.RequiredWhitespaceParser
import com.copperleaf.kudzu.parser.value.BooleanLiteralParser
import com.copperleaf.kudzu.parser.value.CharLiteralParser
import com.copperleaf.kudzu.parser.value.DoubleLiteralParser
import com.copperleaf.kudzu.parser.value.IntLiteralParser
import com.copperleaf.kudzu.parser.value.StringLiteralParser
import com.copperleaf.thistle.asThistleValueParser
import com.copperleaf.thistle.node.ThistleValueMapNode
import com.copperleaf.thistle.node.ThistleValueNode

@ExperimentalStdlibApi
class ThistleSyntaxBuilder {

// Public API
// ---------------------------------------------------------------------------------------------------------------------

    fun from(other: (ThistleSyntaxBuilder) -> Unit) {
        other(this)
    }

    fun tag(
        tagName: String,
        tag: () -> ThistleTag
    ) {
        tags[tagName] = tag
    }

    fun valueFormat(
        parser: () -> Parser<ThistleValueNode>
    ) {
        valueParsers.add(parser())
    }

    fun customSyntax(syntax: (Parser<ThistleValueMapNode>) -> ThistleSyntax) {
        this.syntax = syntax
    }

    fun customSyntax(
        openTagStartToken: Parser<*> = LiteralTokenParser("{{"),
        openTagEndToken: Parser<*> = LiteralTokenParser("}}"),
        closeTagStartToken: Parser<*> = LiteralTokenParser("{{/"),
        closeTagEndToken: Parser<*> = LiteralTokenParser("}}"),
    ) {
        syntax = {
            DefaultThistleSyntax(
                openTagStartToken = openTagStartToken,
                openTagEndToken = openTagEndToken,
                closeTagStartToken = closeTagStartToken,
                closeTagEndToken = closeTagEndToken,
                attrMapParser = it
            )
        }
    }

// Private Implementation
// ---------------------------------------------------------------------------------------------------------------------

    internal val valueParsers: MutableList<Parser<ThistleValueNode>> = mutableListOf(
        BooleanLiteralParser().asThistleValueParser(),
        DoubleLiteralParser().asThistleValueParser(),
        IntLiteralParser().asThistleValueParser(),
        StringLiteralParser().asThistleValueParser(),
        CharLiteralParser().asThistleValueParser(),
        hexColorAsIntValueParser,
        contextValueParser,
        unquotedStringValueValueParser,
    )

    private var syntax: (Parser<ThistleValueMapNode>) -> ThistleSyntax = {
        DefaultThistleSyntax(
            openTagStartToken = LiteralTokenParser("{{"),
            openTagEndToken = LiteralTokenParser("}}"),
            closeTagStartToken = LiteralTokenParser("{{/"),
            closeTagEndToken = LiteralTokenParser("}}"),
            attrMapParser = it
        )
    }

    private val tags: LinkedHashMap<String, () -> ThistleTag> = linkedMapOf()

    internal fun buildAttrValueParser(): Parser<ThistleValueNode> = FlatMappedParser(
        ExactChoiceParser(
            valueParsers
        )
    ) { it.node as ThistleValueNode }

    internal fun buildAttrParser(): Parser<ValueNode<Pair<String, ThistleValueNode>>> = MappedParser(
        SequenceParser(
            IdentifierTokenParser(),
            CharInParser('='),
            buildAttrValueParser()
        )
    ) {
        val (key, _, value) = it.children
        key.text to (value as ThistleValueNode)
    }

    internal fun buildAttrListParser(): Parser<ValueNode<Map<String, ThistleValueNode>>> = MappedParser(
        SeparatedByParser(
            term = buildAttrParser(),
            separator = RequiredWhitespaceParser()
        )
    ) {
        it.nodeList.map { it.value }.toMap()
    }

    internal fun buildAttrMapParser(): Parser<ThistleValueMapNode> = FlatMappedParser(
        MaybeParser(
            buildAttrListParser()
        )
    ) {
        ThistleValueMapNode(
            it.node?.value ?: emptyMap(),
            it.context
        )
    }

    internal fun buildSyntaxParser() : ThistleSyntax = syntax(buildAttrMapParser())

    fun build(): List<ThistleTagBuilder> {
        val syntax = syntax(buildAttrMapParser())

        return tags
            .entries
            .map { (name, builder) ->
                ThistleTagBuilder(
                    kudzuTagBuilder = TagBuilder(
                        name,
                        syntax.tagStart(name),
                        syntax.tagEnd(name)
                    ),
                    tag = builder()
                )
            }
    }

// Companion
// ---------------------------------------------------------------------------------------------------------------------

    companion object {
        val hexColorAsIntValueParser: Parser<ThistleValueNode> = MappedParser(
            SequenceParser(
                CharInParser('#'),
                TimesParser(
                    HexDigitParser(),
                    times = 6
                ),
            )
        ) {
            val (_, hexDigits) = it.children

            // TODO: does this need to be converted by the platform? Wrapped in some other manner?
            hexDigits.text.toInt(16) or -0x1000000 // Set the alpha value
        }.asThistleValueParser()

        val unquotedStringValueValueParser: Parser<ThistleValueNode> = MappedParser(AnyTokenParser()) { it.text }
            .asThistleValueParser()

        val contextValueParser: Parser<ThistleValueNode> = FlatMappedParser(
            SequenceParser(
                LiteralTokenParser("context."),
                AnyTokenParser(),
            )
        ) {
            val (_, mapKeyNode) = it.children
            ThistleValueNode.ContextValue(
                it.text,
                { context: Map<String, Any> ->
                    check(context.containsKey(mapKeyNode.text)) {
                        "Error: Context must contain value for key '${mapKeyNode.text}'"
                    }

                    context[mapKeyNode.text]!!
                },
                it.context
            )
        }
    }
}
