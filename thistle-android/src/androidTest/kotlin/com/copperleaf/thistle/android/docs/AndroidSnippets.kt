package com.copperleaf.thistle.android.docs

import android.content.Context
import android.graphics.Color
import android.text.style.BackgroundColorSpan
import android.view.View
import android.widget.TextView
import com.copperleaf.kudzu.parser.mapped.MappedParser
import com.copperleaf.kudzu.parser.text.LiteralTokenParser
import com.copperleaf.thistle.android.AndroidDefaults
import com.copperleaf.thistle.android.applyStyledText
import com.copperleaf.thistle.android.renderer.AndroidThistleRenderContext
import com.copperleaf.thistle.android.tags.AndroidLink
import com.copperleaf.thistle.core.asThistleValueParser
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleParser
import com.copperleaf.thistle.core.parser.ThistleTagFactory
import com.copperleaf.thistle.core.renderer.int

/**
 * The snippets in this file are not actually tested now, but they are used in documentation.
 *
 * TODO: setup Robolectric to actually test these
 */
/* ktlint-disable max-line-length */
@Suppress("UNUSED_ANONYMOUS_PARAMETER")
@ExperimentalStdlibApi
class AndroidSnippets {

    class Binding(
        val textView: TextView
    )

    lateinit var binding: Binding
    lateinit var context: Context

    fun mainBasicUsage() {
        // snippet::main-basic-usage[]
        val thistle = ThistleParser(AndroidDefaults(context))
        binding.textView.applyStyledText(
            thistle,
            "This is a {{b}}very important{{/b}}, {{foreground color=#ff0000}}urgent{{/foreground}} message!"
        )
        // end::main-basic-usage
    }

    fun androidBasicUsage() {
        // snippet::android-basic-usage[android]
        // create the Thistle parser. It's best to create this once and inject it wherever needed
        val thistle = ThistleParser(AndroidDefaults(context)) // add the default tags for Android

        // parse a formatted string to a Spanned instance, and set that as the text of a TextView
        binding.textView.applyStyledText(
            thistle,
            "This is a {{b}}very important{{/b}}, {{foreground color=#ff0000}}urgent{{/foreground}} message!"
        )
        // end::android-basic-usage
    }

    fun androidCustomTags() {
        // snippet::android-custom-tags[android]
        // create a custom implementation of ThistleTag
        class CustomStyle : ThistleTagFactory<AndroidThistleRenderContext, Any> {
            override fun invoke(renderContext: AndroidThistleRenderContext): Any {
                // use checkArgs to safely pull properties from the input args and ensure incorrect args are not set
                return checkArgs(renderContext) {
                    val color: Int by int()

                    // return anything that can be set to a `SpannableStringBuilder`
                    BackgroundColorSpan(color)
                }
            }
        }

        val thistle = ThistleParser(AndroidDefaults(context)) {
            // register your custom tab with the Thistle parser
            tag("customStyle") { CustomStyle() }

            // the Link ThistleTag is useful for making portions of text clickable
            tag("inc") { AndroidLink { widget: View -> /* do something on link-click */ } }
            tag("dec") { AndroidLink { widget: View -> /* do something on link-click */ } }
        }

        binding.textView.applyStyledText(
            thistle,
            "{{inc}}+{{/inc}} {{customStyle}}count:{{/customStyle}} {{dec}}-{{/dec}}"
        )
        // end::android-custom-tags
    }

    fun androidCustomValueFormats() {
        // snippet::android-custom-value-formats[android]
        val thistle = ThistleParser(AndroidDefaults(context)) {
            valueFormat {
                MappedParser(
                    LiteralTokenParser("@color/red")
                ) { Color.RED }.asThistleValueParser()
            }
        }

        binding.textView.applyStyledText(
            thistle,
            "{{foreground color=@color/red}}|{{/foreground}}"
        )
        // end::android-custom-value-formats
    }

    fun androidCustomStartEndTokens() {
        // snippet::android-custom-start-end-tokens[android]
        val thistle = ThistleParser(AndroidDefaults(context)) {
            // customize syntax to use Django/Twig/Pebble-style tags
            customSyntax(
                openTagStartToken = LiteralTokenParser("{%"),
                openTagEndToken = LiteralTokenParser("%}"),
                closeTagStartToken = LiteralTokenParser("{%"),
                closeTagEndToken = LiteralTokenParser("%}"),
                interpolateStartToken = LiteralTokenParser("{{"),
                interpolateEndToken = LiteralTokenParser("}}"),
            )
        }

        binding.textView.applyStyledText(
            thistle,
            "{% inc %}Click Me!{% inc %}    {% foreground color=#ff0000%}|{% foreground %}    {% dec %}Don't Click Me!{% dec %}"
        )
        // end::android-custom-start-end-tokens
    }
}
/* ktlint-enable max-line-length */
