package com.copperleaf.thistle.android

import android.annotation.SuppressLint
import android.content.Context
import com.copperleaf.kudzu.parser.chars.CharInParser
import com.copperleaf.kudzu.parser.choice.ExactChoiceParser
import com.copperleaf.kudzu.parser.mapped.MappedParser
import com.copperleaf.kudzu.parser.sequence.SequenceParser
import com.copperleaf.kudzu.parser.text.IdentifierTokenParser
import com.copperleaf.kudzu.parser.text.LiteralTokenParser
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.android.tags.BackgroundColor
import com.copperleaf.thistle.android.tags.ForegroundColor
import com.copperleaf.thistle.android.tags.Icon
import com.copperleaf.thistle.android.tags.Strikethrough
import com.copperleaf.thistle.android.tags.Style
import com.copperleaf.thistle.android.tags.Subscript
import com.copperleaf.thistle.android.tags.Superscript
import com.copperleaf.thistle.android.tags.Typeface
import com.copperleaf.thistle.android.tags.Underline
import com.copperleaf.thistle.android.tags.Url
import com.copperleaf.thistle.core.asThistleValueParser
import com.copperleaf.thistle.core.parser.ThistleSyntaxBuilder

@ExperimentalStdlibApi
@SuppressLint("NewApi")
object AndroidDefaults : ThistleSyntaxBuilder.Defaults<AndroidThistleRenderContext, Any> {

    /**
     * Adds the default set of Android tags to the [ThistleSyntaxBuilder]. Also add a value format for `@`-type
     * resources defined in Android XML Resources, matching the format typically used in layouts. It currently only
     * recognizes `@color/`, `@string/`, and `@drawable/` resources
     */
    operator fun invoke(
        context: Context,
        packageName: String
    ): ThistleSyntaxBuilder.Defaults<AndroidThistleRenderContext, Any> {
        return ThistleSyntaxBuilder.Defaults { builder ->
            with(builder) {
                apply(this)

                valueFormat {
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

                        val resourceId = context.resources.getIdentifier(
                            resourceNameNode.text,
                            resourceType,
                            packageName
                        )

                        when (resourceType) {
                            "string" -> context.resources.getString(resourceId)
                            "color" -> context.resources.getColor(resourceId, context.theme)
                            "drawable" -> context.resources.getDrawable(resourceId, context.theme)
                            else -> error("Unknown resource type: $resourceType")
                        }
                    }.asThistleValueParser()
                }
            }
        }
    }

    /**
     * Adds the default set of Android tags to the [ThistleSyntaxBuilder].
     */
    override fun apply(builder: ThistleSyntaxBuilder<AndroidThistleRenderContext, Any>) {
        with(builder) {
            tag("foreground") { ForegroundColor() }
            tag("background") { BackgroundColor() }

            tag("style") { Style() }

            tag("typeface") { Typeface() }
            tag("monospace") { Typeface(android.graphics.Typeface.MONOSPACE) }
            tag("sans") { Typeface(android.graphics.Typeface.MONOSPACE) }
            tag("serif") { Typeface(android.graphics.Typeface.MONOSPACE) }

            tag("strikethrough") { Strikethrough() }

            tag("subscript") { Subscript() }
            tag("superscript") { Superscript() }

            tag("url") { Url() }
            tag("icon") { Icon() }

            tag("b") { Style(android.graphics.Typeface.BOLD) }
            tag("i") { Style(android.graphics.Typeface.ITALIC) }
            tag("u") { Underline() }
        }
    }
}
