package com.copperleaf.thistle.console

import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.thistle.console.renderer.AnsiEscapeCode
import com.copperleaf.thistle.console.renderer.ConsoleThistleRenderer
import com.copperleaf.thistle.core.parser.ThistleParser

@ExperimentalStdlibApi
val ThistleParser<AnsiEscapeCode>.consoleRenderer: ConsoleThistleRenderer
    get() {
        return ConsoleThistleRenderer(tags)
    }

@ExperimentalStdlibApi
fun printlnStyledText(
    thistle: ThistleParser<AnsiEscapeCode>,
    input: String,
    context: Map<String, Any> = emptyMap()
) {
    val (rootNode, _) = thistle.parser.parse(ParserContext.fromString(input))
    val rendered = thistle.consoleRenderer.render(rootNode, context)
    println(rendered)
}
