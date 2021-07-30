package com.copperleaf.thistle.core.parser

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.mapped.ValueNode
import com.copperleaf.kudzu.node.tag.TagNameNode
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.chars.CharInParser
import com.copperleaf.kudzu.parser.chars.HexDigitParser
import com.copperleaf.kudzu.parser.choice.ExactChoiceParser
import com.copperleaf.kudzu.parser.many.SeparatedByParser
import com.copperleaf.kudzu.parser.many.TimesParser
import com.copperleaf.kudzu.parser.mapped.FlatMappedParser
import com.copperleaf.kudzu.parser.mapped.MappedParser
import com.copperleaf.kudzu.parser.maybe.MaybeParser
import com.copperleaf.kudzu.parser.noop.NoopParser
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
import com.copperleaf.thistle.core.ThistleRendererFactory
import com.copperleaf.thistle.core.asThistleValueParser
import com.copperleaf.thistle.core.node.ThistleValueMapNode
import com.copperleaf.thistle.core.node.ThistleValueNode
import com.copperleaf.thistle.core.renderer.ThistleRenderContext

@ExperimentalStdlibApi
class ThistleSyntaxBuilder<RenderContext : ThistleRenderContext, Tag : Any, RichText : Any> {

    interface Defaults<RenderContext : ThistleRenderContext, Tag : Any, RichText : Any> {
        fun applyToBuilder(builder: ThistleSyntaxBuilder<RenderContext, Tag, RichText>)

        fun rendererFactory(): ThistleRendererFactory<RenderContext, Tag, RichText>
    }

// Public API
// ---------------------------------------------------------------------------------------------------------------------

    fun from(other: ThistleSyntaxBuilder.Defaults<RenderContext, Tag, RichText>) {
        other.applyToBuilder(this)
    }

    fun tag(
        tagName: String,
        tagFactory: () -> ThistleTagFactory<RenderContext, Tag>
    ) {
        tags[tagName] = tagFactory
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
        interpolateStartToken: Parser<*> = LiteralTokenParser("{"),
        interpolateEndToken: Parser<*> = LiteralTokenParser("}"),
    ) {
        syntax = {
            DefaultThistleSyntax(
                openTagStartToken = openTagStartToken,
                openTagEndToken = openTagEndToken,
                closeTagStartToken = closeTagStartToken,
                closeTagEndToken = closeTagEndToken,
                interpolateStartToken = interpolateStartToken,
                interpolateEndToken = interpolateEndToken,
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
            interpolateStartToken = LiteralTokenParser("{"),
            interpolateEndToken = LiteralTokenParser("}"),
            attrMapParser = it
        )
    }

    private val tags: LinkedHashMap<String, () -> ThistleTagFactory<RenderContext, Tag>> = linkedMapOf()

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

    internal fun buildSyntaxParser(): ThistleSyntax = syntax(buildAttrMapParser())

    private fun <T : Node> Parser<T>.wrapInTagName(tagName: String): Parser<TagNameNode<T>> {
        return FlatMappedParser(this) { TagNameNode(tagName, it, it.context) }
    }

    fun build(): Pair<TagBuilder<*, *>, Map<String, ThistleTagConfiguration<RenderContext, Tag>>> {
        val syntax = syntax(buildAttrMapParser())

        val interpolate = TagBuilder(
            "interpolate",
            syntax.interpolate().wrapInTagName("interpolate"),
            NoopParser().wrapInTagName("interpolate")
        )

        val tags = tags
            .entries
            .map { (name, builder) ->
                name to ThistleTagConfiguration(
                    kudzuTagBuilder = TagBuilder(
                        name,
                        syntax.tagStart(),
                        syntax.tagEnd()
                    ),
                    tagFactory = builder()
                )
            }
            .toMap()

        return interpolate to tags
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
