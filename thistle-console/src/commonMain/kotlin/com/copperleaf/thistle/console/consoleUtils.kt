package com.copperleaf.thistle.console

import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.thistle.console.ansi.AnsiEscapeCode
import com.copperleaf.thistle.console.renderer.ConsoleThistleRenderContext
import com.copperleaf.thistle.core.parser.ThistleParser

@ExperimentalStdlibApi
public fun printlnStyledText(
    thistle: ThistleParser<ConsoleThistleRenderContext, AnsiEscapeCode, String>,
    input: String,
    context: Map<String, Any> = emptyMap()
) {
    val rootNode = thistle.parse(ParserContext.fromString(input))
    val rendered = thistle.newRenderer().render(rootNode, context)
    println(rendered)
}
