package com.copperleaf.thistle.android.parser

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import com.copperleaf.kudzu.node.mapped.ValueNode
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.kudzu.parser.ParserResult
import com.copperleaf.kudzu.parser.chars.CharInParser
import com.copperleaf.kudzu.parser.choice.ExactChoiceParser
import com.copperleaf.kudzu.parser.mapped.MappedParser
import com.copperleaf.kudzu.parser.sequence.SequenceParser
import com.copperleaf.kudzu.parser.text.IdentifierTokenParser
import com.copperleaf.kudzu.parser.text.LiteralTokenParser

@ExperimentalStdlibApi
class ResourceReferenceParser(
    private val uiContext: Context,
    private val packageName: String,
) : Parser<ValueNode<Any>> {

    override fun predict(input: ParserContext): Boolean {
        return parser.predict(input)
    }

    override val parse: DeepRecursiveFunction<ParserContext, ParserResult<ValueNode<Any>>>
        get() = parser.parse

    val parser by lazy {
        MappedParser(
            SequenceParser(
                CharInParser('@'),
                ExactChoiceParser(
                    LiteralTokenParser("string"),
                    LiteralTokenParser("color"),
                    LiteralTokenParser("drawable"),
                ),
                CharInParser('/'),
                IdentifierTokenParser()
            )
        ) {
            val (_, stringOrColorChoiceNode, _, resourceNameNode) = it.children

            val resourceType = stringOrColorChoiceNode.text

            val resourceId = uiContext.resources.getIdentifier(
                resourceNameNode.text,
                resourceType,
                packageName
            )

            when (resourceType) {
                "string" -> getStringResource(resourceId)
                "color" -> getColorResource(resourceId)
                "drawable" -> getDrawableResource(resourceId)
                else -> error("Unknown resource type: $resourceType")
            }
        }
    }

    private fun getStringResource(resourceId: Int): String {
        return uiContext.resources.getString(resourceId)
    }

    @Suppress("DEPRECATION")
    private fun getColorResource(resourceId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uiContext.resources.getColor(resourceId, uiContext.theme)
        } else {
            uiContext.resources.getColor(resourceId)
        }
    }

    private fun getDrawableResource(resourceId: Int): Drawable {
        return uiContext.resources.getDrawable(resourceId, uiContext.theme)
    }
}
