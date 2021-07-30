@file:Suppress("UNUSED_VARIABLE", "UNUSED")
package com.copperleaf.thistle.console.docs

import com.copperleaf.thistle.console.ConsoleDefaults
import com.copperleaf.thistle.console.printlnStyledText
import com.copperleaf.thistle.console.ansi.AnsiEscapeCode
import com.copperleaf.thistle.console.renderer.ConsoleThistleRenderContext
import com.copperleaf.thistle.core.checkArgs
import com.copperleaf.thistle.core.parser.ThistleParser
import com.copperleaf.thistle.core.parser.ThistleTagFactory

/**
 * The snippets in this file are not actually tested now, but they are used in documentation.
 *
 * TODO: setup Robolectric to actually test these
 */
/* ktlint-disable max-line-length */
@Suppress("UNUSED_ANONYMOUS_PARAMETER")
@ExperimentalStdlibApi
class ConsoleSnippets {

    fun consoleBasicUsage() {
        // snippet::console-basic-usage[console]
        // create the Thistle parser. It's best to create this once and inject it wherever needed
        val thistle = ThistleParser(ConsoleDefaults) // add the default tags for rendering to the console

        // parse a formatted string to a ANSI escape codes, and render that to the console with `println()`
        printlnStyledText(
            thistle,
            "This is a {{b}}very important{{/b}}, {{red}}urgent{{/red}} message!"
        )
        // end::console-basic-usage
    }

    fun consoleCustomTags() {
        // snippet::console-custom-tags[console]
        // create a custom implementation of ThistleTag
        class CustomStyle : ThistleTagFactory<ConsoleThistleRenderContext, AnsiEscapeCode> {
            override fun invoke(renderContext: ConsoleThistleRenderContext): AnsiEscapeCode {
                // use checkArgs to safely pull properties from the input args and ensure incorrect args are not set
                return checkArgs(renderContext) {
                    val color: Int by int()

                    AnsiEscapeCode("")
                }
            }
        }

        val thistle = ThistleParser(ConsoleDefaults) {
            // register your custom tab with the Thistle parser
            tag("customStyle") { CustomStyle() }
        }

        printlnStyledText(
            thistle,
            "{{customStyle}}count:{{/customStyle}}"
        )
        // end::console-custom-tags
    }
}
/* ktlint-enable max-line-length */
