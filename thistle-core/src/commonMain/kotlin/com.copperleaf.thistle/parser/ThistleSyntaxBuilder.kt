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

    fun literalFormat(
        parser: () -> Parser<ValueNode<Any>>
    ) {
        literals.add(parser())
    }

    fun customSyntax(syntax: (Parser<ValueNode<Map<String, Any>>>) -> ThistleSyntax) {
        this.syntax = syntax
    }

    fun customSyntax(
        openTagStartToken: Parser<*> = LiteralTokenParser("{{"),
        openTagEndToken: Parser<*> = LiteralTokenParser("}}"),
        closeTagStartToken: Parser<*> = LiteralTokenParser("{{/"),
        closeTagEndToken: Parser<*> = LiteralTokenParser("}}"),
    ) {
        syntax = {
            ThistleSyntax.Impl(
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

    val hexColorAsIntValueParser = MappedParser(
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
    }

    val unquotedStringValueParser = MappedParser(
        AnyTokenParser()
    ) {
        it.text
    }

    private val literals: MutableList<Parser<ValueNode<Any>>> = mutableListOf(
        BooleanLiteralParser() as Parser<ValueNode<Any>>,
        DoubleLiteralParser() as Parser<ValueNode<Any>>,
        IntLiteralParser() as Parser<ValueNode<Any>>,
        StringLiteralParser() as Parser<ValueNode<Any>>,
        CharLiteralParser() as Parser<ValueNode<Any>>,
        hexColorAsIntValueParser as Parser<ValueNode<Any>>,
        unquotedStringValueParser as Parser<ValueNode<Any>>,
    )

    private var syntax: (Parser<ValueNode<Map<String, Any>>>) -> ThistleSyntax = {
        ThistleSyntax.Impl(
            openTagStartToken = LiteralTokenParser("{{"),
            openTagEndToken = LiteralTokenParser("}}"),
            closeTagStartToken = LiteralTokenParser("{{/"),
            closeTagEndToken = LiteralTokenParser("}}"),
            attrMapParser = it
        )
    }

    private val tags: LinkedHashMap<String, () -> ThistleTag> = linkedMapOf()

    private fun buildAttrMapParser(): Parser<ValueNode<Map<String, Any>>> {
        val attrValue = FlatMappedParser(
            ExactChoiceParser(
                literals
            )
        ) { it.node }

        val attrParser: Parser<ValueNode<Pair<String, Any>>> = MappedParser(
            SequenceParser(
                IdentifierTokenParser(),
                CharInParser('='),
                attrValue
            )
        ) {
            val (key, _, value) = it.children
            key.text to (value as ValueNode<Any>).value
        }

        val attrListParser: Parser<ValueNode<Map<String, Any>>> = MappedParser(
            SeparatedByParser(
                term = attrParser,
                separator = RequiredWhitespaceParser()
            )
        ) {
            it.nodeList.map { it.value }.toMap()
        }

        val optionalAttrListParser: Parser<ValueNode<Map<String, Any>>> = MappedParser(
            MaybeParser(
                attrListParser
            )
        ) {
            it.node?.value ?: emptyMap()
        }

        return optionalAttrListParser
    }

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
}
