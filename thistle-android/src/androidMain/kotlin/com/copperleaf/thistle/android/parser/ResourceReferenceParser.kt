package com.copperleaf.thistle.android.parser

import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.kudzu.parser.ParserResult
import com.copperleaf.kudzu.parser.chars.CharInParser
import com.copperleaf.kudzu.parser.choice.ExactChoiceParser
import com.copperleaf.kudzu.parser.mapped.FlatMappedParser
import com.copperleaf.kudzu.parser.sequence.SequenceParser
import com.copperleaf.kudzu.parser.text.IdentifierTokenParser
import com.copperleaf.kudzu.parser.text.LiteralTokenParser

public class ResourceReferenceParser : Parser<ResourceReferenceNode<*>> {

    override fun predict(input: ParserContext): Boolean {
        return parser.predict(input)
    }

    override val parse: DeepRecursiveFunction<ParserContext, ParserResult<ResourceReferenceNode<*>>>
        get() = parser.parse

    val parser by lazy {
        FlatMappedParser(
            SequenceParser(
                CharInParser('@'),
                ExactChoiceParser(
                    LiteralTokenParser("string"),
                    LiteralTokenParser("color"),
                    LiteralTokenParser("drawable"),
                ),
                CharInParser('/'),
                IdentifierTokenParser(),
            )
        ) { (nodeContext, _, stringOrColorChoiceNode, _, resourceNameNode) ->
            val resourceType = stringOrColorChoiceNode.text
            val resourceName = resourceNameNode.text

            when (resourceType) {
                "string" -> StringResourceReferenceNode(resourceName, nodeContext)
                "color" -> ColorResourceReferenceNode(resourceName, nodeContext)
                "drawable" -> DrawableResourceReferenceNode(resourceName, nodeContext)
                else -> error("Unknown resource type: $resourceType")
            }
        }
    }
}
